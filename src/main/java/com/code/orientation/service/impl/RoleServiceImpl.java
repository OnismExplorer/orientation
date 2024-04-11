package com.code.orientation.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Role;
import com.code.orientation.entity.RolePermission;
import com.code.orientation.entity.UserRole;
import com.code.orientation.mapper.RoleMapper;
import com.code.orientation.service.RolePermissionService;
import com.code.orientation.service.RoleService;
import com.code.orientation.service.UserRoleService;
import com.code.orientation.utils.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-12-28 10:39:43
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    private final UserRoleService userRoleService;

    private final RolePermissionService rolePermissionService;

    @Autowired
    public RoleServiceImpl(UserRoleService userRoleService, RolePermissionService rolePermissionService) {
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
    }

    @Override
    @Cache(constants = RedisConstants.USER_ROLE)
    public List<String> getRoleNameByUser(Long id) {
        return getRoleByUser(id).stream().map(Role::getRoleName).toList();
    }

    @Override
    public List<Role> getRoleByUser(Long id) {
        List<Long> roleIdList = userRoleService.lambdaQuery().select(UserRole::getRoleId)
                .eq(UserRole::getUserId, id).list()
                .stream().map(UserRole::getRoleId).toList();
        if(CollUtil.isEmpty(roleIdList)){
            return Collections.emptyList();
        }
        return lambdaQuery().in(Role::getId,roleIdList).list();
    }

    @Override
    public Boolean saveRole(Role role) {
        boolean saved = save(role);
        if(saved && !role.getPermissions().isEmpty()){
            return bindPermission(role.getId(),role.getPermissions());
        }
        return saved;
    }

    private boolean  bindPermission(Long roleId,List<Long> permissionIdList){
        unbindPermission(roleId);
        List<RolePermission> rolePermissionList = permissionIdList.stream()
                .map(permission -> new RolePermission().setRoleId(roleId).setPermissionId(permission)).toList();
        return rolePermissionService.saveBatch(rolePermissionList);
    }

    private void unbindPermission(Long roleId){
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        rolePermissionService.remove(wrapper);
    }

    @Override
    public Role appendPermission(Role role) {
        List<String> permissionNameByRole = rolePermissionService.getPermissionNameByRole(role.getId());
        List<Long> permissionIds = rolePermissionService.getPermissionIdsByRole(ListUtil.of(role.getId()));
        return role.setPermissionNames(permissionNameByRole).setPermissions(permissionIds);
    }

    @Override
    public Boolean bindPermission(Long roleId, Long permissionId) {
        RolePermission rolePermission = new RolePermission().setPermissionId(permissionId).setRoleId(roleId);
        return rolePermissionService.save(rolePermission);
    }

    @Override
    public Boolean unbindPermission(Long roleId, Long permissionId) {
        return rolePermissionService.unbind(roleId,permissionId);
    }

    @Override
    public void removeRole(Long roleId) {
        unbindPermission(roleId);
        removeById(roleId);
    }

    @Override
    public Boolean updateRole(Role role) {
        if(!role.getPermissions().isEmpty()){
            return bindPermission(role.getId(),role.getPermissions());
        }
        return updateById(role);
    }
}




