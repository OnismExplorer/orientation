package com.code.orientation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.orientation.entity.UserRole;

import java.util.List;

/**
 * @author HeXin
 * @description 针对表【user_role】的数据库操作Mapper
 * @createDate 2023-12-28 10:39:44
 * @Entity com.code.orientation.entity.UserRole
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 按用户 ID 选择角色名称
     * @param id 编号
     * @return {@link List}<{@link String}>
     */
    List<String> selectRoleNamesByUserId(Long id);
}




