package com.code.orientation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.orientation.entity.Permission;

import java.util.List;

/**
 * @author HeXin
 * @description 针对表【permission】的数据库操作Mapper
 * @createDate 2023-12-28 10:39:43
 * @Entity com.code.orientation.entity.Permission
 */

public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 获取子节点
     *
     * @param parentId 父 ID
     * @return {@link List}<{@link Permission}>
     */
    List<Permission> getChildren(String parentId);

    /**
     * 按用户获取权限
     *
     * @param id 编号
     * @return {@link List}<{@link Permission}>
     */
    List<Permission> getPermissionByUser(Long id);
}




