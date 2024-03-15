package com.code.orientation.schedule;

import com.alibaba.fastjson2.JSON;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Activity;
import com.code.orientation.service.ActivityService;
import com.code.orientation.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 活动定时任务
 *
 * @author HeXin
 * @date 2024/03/05
 */
@Component
public class ActivityTimer {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 初始化(项目启动时执行一遍定时任务)
     */
    @PostConstruct
    @Transactional
    public void init() {
        scan();
        autoUpdate();
    }

    /**
     * 每天0点执行一次扫描(周期半个小时)
     */
    @Async
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scan() {
        activityService.scan();
    }

    /**
     * 每分钟检测缓存队列并实时修改状态
     */
    @Async
    @Scheduled(fixedRate = 60000)
    public void autoUpdate() {
        String up = redisUtil.getString(RedisConstants.ACTIVITY_UPCOMING.getKey());
        String end = redisUtil.getString(RedisConstants.ACTIVITY_ENDING.getKey());
        // 待修改列表
        List<Activity> upcoming = Collections.emptyList();
        List<Activity> ending = Collections.emptyList();
        if (StringUtils.isNotBlank(up)) {
            upcoming = JSON.parseArray(up).toList(Activity.class);
        }
        if (StringUtils.isNotBlank(end)) {
            ending = JSON.parseArray(end).toList(Activity.class);
        }
        if (!upcoming.isEmpty()) {
            updateUpComing(upcoming);
        }

        if (!ending.isEmpty()) {
            updateEnding(ending);
        }
    }

    /**
     * 更新任务
     *
     * @param activityList 即将到来
     */
    private void updateUpComing(List<Activity> activityList) {
        // 更新活动状态
        int index = 0;
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            if (activity.getStart().before(new Date())) {
                activity.setState(1);
                index = i;
            } else {
                break;
            }
        }
        // 移除已经修改过
        update(activityList, index);
    }

    /**
     * 自动更新
     *
     * @param activityList 活动列表
     * @param index        指数
     */
    private void update(List<Activity> activityList, int index) {
        List<Activity> updating = new ArrayList<>(activityList.subList(0, index + 1));
        activityList.removeAll(activityList.subList(0, index + 1));

        if (!activityList.isEmpty()) {
            redisUtil.set(RedisConstants.ACTIVITY_UPCOMING, activityList);
        } else {
            redisUtil.remove(RedisConstants.ACTIVITY_UPCOMING.getKey());
        }
        activityService.updateBatchById(updating);
    }

    /**
     * 更新结束
     *
     * @param activityList 活动列表
     */
    private void updateEnding(List<Activity> activityList) {
        // 更新活动状态
        int index = 0;
        for (int i = 0; i < activityList.size(); i++) {
            Activity activity = activityList.get(i);
            if (activity.getEnd().before(new Date())) {
                activity.setState(2);
                index = i;
            } else {
                break;
            }
        }
        // 移除已经修改过
        update(activityList, index);
    }
}
