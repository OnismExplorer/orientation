package com.code.orientation.service;

import com.code.orientation.entity.Academy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.entity.vo.StudentCount;

/**
* @author HeXin
* @description 针对表【academy】的数据库操作Service
* @createDate 2024-04-06 22:43:51
*/
public interface AcademyService extends IService<Academy> {

    /**
     * 获取学生数量
     *
     * @param id 同上
     * @return {@link StudentCount}
     */
    StudentCount count(Long id);
}
