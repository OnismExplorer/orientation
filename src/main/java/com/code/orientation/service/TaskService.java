package com.code.orientation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.common.Result;
import com.code.orientation.entity.Task;
import com.code.orientation.entity.vo.TaskVO;

/**
 * @description 针对表【task】的数据库操作Service
 * @createDate 2023-12-28 10:39:44
 */
public interface TaskService extends IService<Task> {

    /**
     * 页
     *
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param key      键
     * @param type     类型
     * @return {@link IPage}<{@link TaskVO}>
     */
    IPage<TaskVO> page(Long pageNum, Long pageSize, String key, Integer type);

    /**
     * 开始
     *
     * @param uid uid
     * @param id  编号
     * @return {@link Result}<{@link String}>
     */
    Result<String> begin(Long uid, Long id);

    /**
     * 完成
     * 完成任务
     *
     * @param id       编号
     * @param uid      uid
     * @param material 材料
     * @return {@link Result}<{@link String}>
     */
    Result<String> finish(Long id, Long uid,String material);

    /**
     * 检测
     *
     * @param id  任务id
     * @param uid 用户id
     * @return {@link Integer}
     */
    Integer detect(Long id, Long uid);
}
