package com.code.orientation.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.service.PermissionService;
import com.code.orientation.service.RoleService;
import com.code.orientation.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 鉴权接口
 *
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    private final PermissionService permissionService;

    private final RoleService roleService;

    private final RedisUtil redisUtil;

    @Autowired
    public StpInterfaceImpl(PermissionService permissionService, RoleService roleService, RedisUtil redisUtil) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.redisUtil = redisUtil;
    }

    @Override
//    @Cache(constants = RedisConstants.USER_PERMISSION)
    public List<String> getPermissionList(Object loginId, String loginType) {
        long id = StpUtil.getLoginIdAsLong();
        List<String> permissions = permissionService.getPermissionByUser(id);
        if(permissions.isEmpty()){
            return Collections.emptyList();
        }
        redisUtil.set(RedisConstants.USER_PERMISSION.getKey()+id,permissions);
        return permissions;
    }

    @Override
//    @Cache(constants = RedisConstants.USER_ROLE,key = )
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleNames =  roleService.getRoleNameByUser(Long.parseLong(loginId.toString()));
        if(CollUtil.isEmpty(roleNames)){
            return Collections.emptyList();
        }
        redisUtil.set(RedisConstants.USER_ROLE.getKey()+loginId,roleNames);
        return roleNames;
    }
}
