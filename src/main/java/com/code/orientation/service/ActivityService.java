package com.code.orientation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.entity.Activity;
import com.code.orientation.entity.dto.ActivityDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @description 针对表【activity】的数据库操作Service
 * @createDate 2023-12-28 10:39:43
 * @date 2024/01/24
 */
public interface ActivityService extends IService<Activity> {

    /**
     * 完成活动
     * @param uid
     * @param id
     * @return {@link Boolean}
     */
    Boolean finish(Long uid, Long id);

    /**
     * @param uid 用户id
     * @param id id
     * @param material 提交的材料
     * @return {@link Boolean}
     */
    Boolean finish(Long uid, Long id, String material);

    /**
     * 开启活动
     * @param id id
     * @param uid 用户id
     */
    void begin(Long id, Long uid);

    /**
     * 取消活动
     * @param id
     * @param uid
     */
    void cancel(Long id, Long uid);

    /**
     * 扫描活动(定时任务，将该天即将开始的活动加入缓存)
     */
    void scan();


    /**
     * 保存活动
     *
     * @param instance 实例
     * @return {@link Boolean}
     */
    Boolean save(ActivityDTO instance);

    /**
     * 生成二维码
     *
     * @param id       活动id
     * @param response 响应
     * @return {@link Boolean}
     */
    Boolean encode(Long id,HttpServletResponse response);

    /**
     * 解析二维码
     *
     * @param id      同上
     * @param content 内容
     * @return {@link Boolean}
     */
    Boolean decode(Long id,String content);

    /**
     * 检测用户活动状态
     *
     * @param id  活动id
     * @param uid UID
     * @return {@link Integer}
     */
    Integer detect(Long id, Long uid);
}
