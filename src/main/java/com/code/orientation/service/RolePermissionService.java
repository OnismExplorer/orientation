package com.code.orientation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.entity.RolePermission;

import java.util.List;

/**
 * 角色权限服务
 *
 * @description 针对表【role_permission】的数据库操作Service
 * @createDate 2023-12-28 10:39:43
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 按角色获取权限名称
     *
     * @param roleId 角色 ID
     * @return {@link List}<{@link String}>
     */
    List<String> getPermissionNameByRole(Long roleId);

    /**
     * 按角色获取权限 ID
     *
     * @param roleIds 角色 ID
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermissionIdsByRole(List<Long> roleIds);

    /**
     * 解除绑定
     *
     * @param roleId       角色 ID
     * @param permissionId 权限 ID
     * @return {@link Boolean}
     */
    Boolean unbind(Long roleId, Long permissionId);
}
