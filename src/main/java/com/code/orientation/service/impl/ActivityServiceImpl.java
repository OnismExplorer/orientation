package com.code.orientation.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Activity;
import com.code.orientation.entity.ActivityLog;
import com.code.orientation.entity.PointsLog;
import com.code.orientation.entity.Student;
import com.code.orientation.entity.dto.ActivityDTO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.mapper.ActivityMapper;
import com.code.orientation.service.ActivityLogService;
import com.code.orientation.service.ActivityService;
import com.code.orientation.service.PointsLogService;
import com.code.orientation.service.StudentService;
import com.code.orientation.utils.QRCodeUtil;
import com.code.orientation.utils.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @description 针对表【activity】的数据库操作Service实现
 * @createDate 2023-12-28 10:39:43
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
        implements ActivityService {

    /**
     * 活动最大参与次数
     */
    private static final Integer MAX_PARTICIPATION_COUNT = 3;

    private final StudentService studentService;

    private final ActivityLogService activityLogService;

    private final PointsLogService pointsLogService;

    private final RedisUtil redisUtil;

    private final QRCodeUtil qrCodeUtil;

    @Autowired
    public ActivityServiceImpl(StudentService studentService, ActivityLogService activityLogService, PointsLogService pointsLogService, RedisUtil redisUtil, QRCodeUtil qrCodeUtil) {
        this.studentService = studentService;
        this.activityLogService = activityLogService;
        this.pointsLogService = pointsLogService;
        this.redisUtil = redisUtil;
        this.qrCodeUtil = qrCodeUtil;
    }

    @Override
    public Boolean finish(Long uid, Long id) {
        return finish(uid, id, null);
    }

    @Override
    public Boolean finish(Long uid, Long id, String material) {
        // 查询出该用户此任务的记录
        ActivityLog activityLog = activityLogService.lambdaQuery().eq(ActivityLog::getActivityId, id)
                .eq(ActivityLog::getUid, uid).one();
        if (activityLog == null) {
            throw new CustomException("无该用户记录！", 289);
        }
        // 获取该活动
        Activity activity = getById(id);
        if(activity == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_ACTIVITY);
        }
        // 判断活动是否已经过期
        if (activity.getEnd().before(new Date())) {
            throw new CustomException("活动已经结束！", 233);
        }
        if(activityLog.getState() == 1){
            throw new CustomException("请勿重复提交！",234);
        }
        if(activityLog.getState() == 0){
            throw new CustomException("该活动记录不存在！",235);
        }
        // 更新该用户状态
        activityLog.setMaterial(material)
                .setFinishTime(new Date())
                .setState(1);
        boolean b = activityLogService.updateById(activityLog);
        // 获取学生对象
        Student student = studentService.lambdaQuery().eq(Student::getUid, uid).one();
        // 判断该用户是否为学生
        if (student == null) {
            // 非学生则无需更新积分
            return b;
        }
        // 新增积分记录
        PointsLog pointsLog = new PointsLog()
                .setPoints(activity.getPoints())
                .setType(1)
                .setUid(uid)
                .setDescription(activity.getScoreStandard());
        pointsLogService.save(pointsLog);
        // 更新积分数
        student.setPoints(student.getPoints() + activity.getPoints());
        boolean c = studentService.updateById(student);
        return b && c;
    }

    @Override
    public void begin(Long id, Long uid) {
        // 获取该活动
        Activity activity = getActivity(id);
        // 判断该用户是否已经开启活动
        Long count = activityLogService.lambdaQuery().eq(ActivityLog::getActivityId, id).eq(ActivityLog::getUid, uid).count();
        if (count >= MAX_PARTICIPATION_COUNT) {
            // 用户一个活动最多取消开启三次，防止恶意刷库
            throw new CustomException("已达到最大开启额度！", 213);
        }
        // 判断活动人数是否已经达到限制
        Long number = activityLogService.lambdaQuery().eq(ActivityLog::getActivityId, id)
                .eq(ActivityLog::getState, 1)
                .and(wraper -> wraper.eq(ActivityLog::getState, 2)).count();
        if (number >= activity.getMaxNumber()) {
            throw new CustomException("活动人数已满！", 255);
        }
        if (count == 0) {
            // 开启活动
            ActivityLog activityLog = new ActivityLog().setUid(uid)
                    .setActivityId(id)
                    .setState(2);
            activityLogService.save(activityLog);
        } else {
            ActivityLog log = activityLogService.lambdaQuery().eq(ActivityLog::getActivityId, id).eq(ActivityLog::getUid, uid).one();
            if (log != null) {
                if (log.getState().equals(2)) {
                    throw new CustomException("请勿重复开启活动！", 264);
                } else if (log.getState().equals(1)) {
                    throw new CustomException("您已完成该活动！", 265);
                }
                activityLogService.updateById(log.setState(2));
            }
        }
        activity.setCount(activity.getCount() + 1);
        updateById(activity);
    }

    private Activity getActivity(Long id) {
        Activity activity = getById(id);
        if(activity == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_ACTIVITY);
        }
        // 判断活动是否已经开始
        if (activity.getStart().after(new Date())) {
            throw new CustomException("活动未开始！", 222);
        }
        // 判断活动是否已经过期
        if (activity.getEnd().before(new Date())) {
            throw new CustomException("活动已经结束！", 233);
        }
        return activity;
    }

    @Override
    public void cancel(Long id, Long uid) {
        // 获取该活动获取该活动
        Activity activity = getActivity(id);
        // 获取活动记录
        ActivityLog activityLog = activityLogService.lambdaQuery()
                .eq(ActivityLog::getActivityId, id)
                .eq(ActivityLog::getUid, uid).one();
        if (activityLog == null || activityLog.getState() != 2) {
            throw new CustomException();
        }
        activityLog.setState(0);
        activity.setCount(activity.getCount() - 1);
        updateById(activity);
        activityLogService.updateById(activityLog);
    }

    /**
     * 每天0点执行一次扫描
     */
    @Override
    public void scan() {
        // 获取该天将要开始的活动(按时间排序)
        List<Activity> upcoming = lambdaQuery().eq(Activity::getState,0)
                .between(Activity::getStart, DateUtil.today() + " 00:00:00",DateUtil.today() + " 23:59:59")
                .orderByAsc(Activity::getStart).list();
        // 获取该天将要结束的活动
        List<Activity> ending = lambdaQuery().not(service -> service.eq(Activity::getState, 2))
                .between(Activity::getEnd, DateUtil.today() + " 00:00:00",DateUtil.today() + " 23:59:59")
                .orderByAsc(Activity::getEnd).list();
        // 分别加入缓存中
        redisUtil.set(RedisConstants.ACTIVITY_UPCOMING, upcoming);
        redisUtil.set(RedisConstants.ACTIVITY_ENDING, ending);
    }

    @Override
    public Boolean save(ActivityDTO instance) {
        Activity activity = instance.toPo(Activity.class);
        if(activity.getStart() == null) {
            throw new CustomException("活动开始时间不能为空",288);
        }
        if(activity.getEnd() != null) {
            return save(activity);
        }
        if(activity.getType().equals(0)) {
            // 若为长期活动，则直接往后推迟100年
            activity.setEnd(DateUtil.offset(activity.getStart(), DateField.YEAR,100));
            return save(activity);
        }
        throw new CustomException("活动结束时间不能为空",288);
    }

    @Override
    public Boolean encode(Long id,HttpServletResponse response) {
        // 判断活动是否存在
        getActivity(id);
        // 添加二维码内容
        String content = RandomUtil.randomString(30);
        // 将内容缓存起来
        redisUtil.set(RedisConstants.ACTIVITY_ENCODE, String.valueOf(id),content);
        // 添加图片 logo(团队logo)
        String imagPath = "https://fingerbed.oss-cn-chengdu.aliyuncs.com/CSDN/202404092011157.png";
        try {
            qrCodeUtil.encode(content,imagPath,true,response.getOutputStream());
        } catch (IOException e) {
            throw new CustomException(e);
        }
        return true;
    }

    @Override
    public Boolean decode(Long id,String content) {
        // 从缓存中获取二维码信息
        String str = redisUtil.get(RedisConstants.ACTIVITY_ENCODE.getKey() + id, String.class);
        if(StringUtils.isBlank(str)) {
            throw new CustomException(CodeEnum.ENCODE_OVERDUE);
        }
        // 比对内容
        if(str.equals(content)) {
            return true;
        }
        throw new CustomException(CodeEnum.ENCODE_INVALID);
    }

}





