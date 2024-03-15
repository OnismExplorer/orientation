package com.code.orientation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.orientation.entity.RolePermission;

import java.util.List;

/**
 * @description 针对表【role_permission】的数据库操作Mapper
 * @createDate 2023-12-28 10:39:43
 * @Entity com.code.orientation.entity.RolePermission
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 按角色选择权限名称
     *
     * @param roleId 角色 ID
     * @return {@link List}<{@link String}>
     */
    List<String> selectPermissionNameByRole(Long roleId);
}




