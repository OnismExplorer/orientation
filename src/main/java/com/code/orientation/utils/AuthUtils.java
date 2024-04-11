package com.code.orientation.utils;

import com.code.orientation.entity.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 权限工具类
 *
 * @author HeXin
 * @date 2024/04/11
 */
public class AuthUtils {
    private AuthUtils() {

    }
    /**
     * 创建树
     *
     * @param permissionList 权限列表
     * @return {@link List}<{@link Permission}>
     */
    public static List<Permission> createTree(List<Permission> permissionList) {
        ArrayList<Permission> tree = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission.getParentId() == 0) {
                addChild(permission, permissionList);
                tree.add(permission);
            }
        }
        return tree;
    }

    /**
     * 添加子项
     *
     * @param permission     准许
     * @param permissionList 权限列表
     */
    private static void addChild(Permission permission, List<Permission> permissionList) {
        ArrayList<Permission> tree = new ArrayList<>();
        for (Permission p : permissionList) {
            if (p.getParentId().equals(permission.getId())) {
                addChild(p, permissionList);
                tree.add(p);
            }
        }
        permission.setChildren(tree);
    }

    /**
     * 获取子密钥名称
     *
     * @param permission 准许
     * @param keys       按键
     * @return {@link Set}<{@link String}>
     */
    public static Set<String> getChildrenKeyName(Permission permission, Set<String> keys) {
        if (permission == null) {
            return keys;
        }
        keys.add(permission.getKeyName());
        if (permission.getChildren() == null || permission.getChildren().isEmpty()) {
            return keys;
        }
        permission.getChildren().forEach(p -> {
            keys.add(p.getKeyName());
            getChildrenKeyName(p, keys);
        });
        return keys;
    }

    public static Permission getTree(Long id, List<Permission> rootTree) {
        if (rootTree == null) {
            return null;
        }
        for (Permission permission : rootTree) {
            if (permission.getId().equals(id)) {
                return permission;
            } else {
                return getTree(id, permission.getChildren());
            }
        }
        return null;
    }
}
