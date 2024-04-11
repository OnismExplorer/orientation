package com.code.orientation.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.entity.*;
import com.code.orientation.entity.vo.TaskVO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.mapper.TaskMapper;
import com.code.orientation.service.PointsLogService;
import com.code.orientation.service.StudentService;
import com.code.orientation.service.TaskLogService;
import com.code.orientation.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 针对表【task】的数据库操作Service实现
 * @createDate 2023-12-28 10:39:44
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
        implements TaskService {

    private final StudentService studentService;

    private final TaskLogService taskLogService;

    private final PointsLogService pointsLogService;

    @Autowired
    public TaskServiceImpl(StudentService studentService, TaskLogService taskLogService, PointsLogService pointsLogService) {
        this.studentService = studentService;
        this.taskLogService = taskLogService;
        this.pointsLogService = pointsLogService;
    }

    @Override
    public IPage<TaskVO> page(Long pageNum, Long pageSize, String key, Integer type) {
        LambdaQueryChainWrapper<Task> wrapper = lambdaQuery().like(StringUtils.isNotBlank(key), Task::getName, key)
                .or()
                .like(StringUtils.isNotBlank(key), Task::getContent, key)
                .or()
                .like(StringUtils.isNotBlank(key), Task::getScoreStandard, key);
        switch (type) {
            case -1 -> wrapper.orderByDesc(Task::getPoints);
            case 0 -> wrapper.orderByAsc(Task::getStart);
            case 1 -> wrapper.orderByDesc(Task::getStart);
            case 2 -> wrapper.orderByAsc(Task::getEnd);
            case 3 -> wrapper.orderByDesc(Task::getEnd);
            case 4 -> wrapper.orderByAsc(Task::getPoints);
            default ->
                // 处理未知的 type
                    throw new CustomException("Unknown type: " + type);
        }
        IPage<Task> page = wrapper.page(new Page<>(pageNum, pageSize));
        // 查询父任务
        Map<Long, String> parentMap = lambdaQuery().select(Task::getId, Task::getName).in(CollUtil.isNotEmpty(page.getRecords().stream().map(Task::getParentId).toList()),Task::getId, page.getRecords().stream().map(Task::getParentId).toList()).list()
                .stream().collect(Collectors.toMap(Task::getId, Task::getName));
        return page.convert(task -> new TaskVO()
                .setId(task.getId())
                .setName(task.getName())
                .setParent(parentMap.getOrDefault(task.getParentId(),"无"))
                .setState(task.getState())
                .setStart(task.getStart())
                .setEnd(task.getEnd())
                .setPoints(task.getPoints())
                .setScoreStandard(task.getScoreStandard())
                .setContent(task.getContent())
                .setType(task.getType()));
    }

    @Override
    public Result<String> begin(Long uid, Long id) {
        Task task = getById(id);
        if(task == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_TASK);
        }
        // 判断该任务是否在时间范围内
        if(DateUtil.compare(task.getStart(),new Date()) > 0 && task.getStart() != null){
            return Result.fail(262,"任务未开始！");
        } else if(DateUtil.compare(new Date(),task.getEnd()) > 0 && task.getEnd() != null){
            return Result.fail(263,"任务时间已过");
        }
        // 判断该任务是否有父任务
        if(task.getParentId() != null || task.getType().equals(1)){
            return Result.fail(265,"请先完成任务："+getById(task.getParentId()).getName());
        }
        // 查看用户是否正在进行该任务
        Long count = taskLogService.lambdaQuery().eq(TaskLog::getUid, uid).eq(TaskLog::getTaskId, id).count();
        if(count > 0){
            return Result.fail(264,"请勿重复开启任务！");
        }
        // 开启任务
        TaskLog log = new TaskLog().setTaskId(id).setUid(uid).setState(2);
        return Result.isSuccess(taskLogService.save(log));
    }

    @Override
    public Result<String> finish(Long id, Long uid,String material) {
        // 判断任务是否超时
        Task task = getById(id);
        if(task == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_TASK);
        }
        if(DateUtil.compare(new Date(),task.getEnd()) > 0 && task.getEnd() != null){
            return Result.fail(288,"任务已过期！");
        }
        // 判断用户是否正在参加此任务
        TaskLog taskLog = taskLogService.lambdaQuery().eq(TaskLog::getTaskId, id).eq(TaskLog::getUid, uid).one();
        if(taskLog == null){
            return Result.fail(256,"未参与任务！");
        }
        // 完成任务，修改状态
        taskLog.setState(1).setMaterial(material).setFinishTime(new Date());
        boolean b = taskLogService.updateById(taskLog);
        // 学生添加对应分数
        Student student = studentService.lambdaQuery().eq(Student::getUid, uid).one();
        student.setPoints(student.getPoints() + task.getPoints());
        // 更新积分记录
        PointsLog pointsLog = new PointsLog()
                .setDescription(task.getScoreStandard())
                .setUid(uid)
                .setType(0)
                .setPoints(task.getPoints());
        pointsLogService.save(pointsLog);
        boolean c = studentService.updateById(student);
        return Result.isSuccess(b && c);
    }

    @Override
    public Integer detect(Long id, Long uid) {
        // 判断活动是否存在
        Long count = lambdaQuery().eq(Task::getId, id).count();
        if(count == 0) {
            throw new CustomException(CodeEnum.NOT_FOUND_ACTIVITY);
        }
        // 判断是否有该活动记录
        TaskLog taskLog = taskLogService.lambdaQuery().eq(TaskLog::getUid, uid).eq(TaskLog::getId, id).one();
        if(taskLog == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_RECORDING);
        }
        return taskLog.getState() == 1 ? 1 : 0;
    }
}




