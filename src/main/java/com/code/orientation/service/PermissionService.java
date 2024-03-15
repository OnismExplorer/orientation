package com.code.orientation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.entity.Permission;

import java.util.List;

/**
 * 权限服务
 *
 * @description 针对表【permission】的数据库操作Service
 * @createDate 2023-12-28 10:39:43
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取子权限
     *
     * @param parentId 父 ID
     * @return {@link List}<{@link Permission}>
     */
    List<Permission> getChildren(String parentId);

    /**
     * 按角色获取权限
     *
     * @param roleId 角色 ID
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermissionByRole(Long roleId);

    /**
     * 按用户获取权限
     *
     * @param id 编号
     * @return {@link List}<{@link String}>
     */
    List<String> getPermissionByUser(Long id);

    /**
     * 删除权限
     *
     * @param id 编号
     * @return {@link Boolean}
     */
    Boolean removePermission(Long id);
}
