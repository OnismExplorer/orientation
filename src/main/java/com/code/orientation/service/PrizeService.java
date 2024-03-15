package com.code.orientation.service;

import com.code.orientation.common.Result;
import com.code.orientation.entity.Prize;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author HeXin
* @description 针对表【prize】的数据库操作Service
* @createDate 2024-01-14 16:18:12
*/
public interface PrizeService extends IService<Prize> {


    /**
     * 交换
     *
     * @param uid uid
     * @param id  编号
     * @return {@link Result}<{@link String}>
     */
    Result<String> exchange(Long uid,Long id);
}
