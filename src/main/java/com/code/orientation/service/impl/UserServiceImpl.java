package com.code.orientation.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Role;
import com.code.orientation.entity.Student;
import com.code.orientation.entity.User;
import com.code.orientation.entity.UserRole;
import com.code.orientation.entity.dto.LoginParam;
import com.code.orientation.entity.dto.UserDTO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.mapper.UserMapper;
import com.code.orientation.service.RoleService;
import com.code.orientation.service.StudentService;
import com.code.orientation.service.UserRoleService;
import com.code.orientation.service.UserService;
import com.code.orientation.utils.PasswordUtils;
import com.code.orientation.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-12-28 10:39:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final RedisUtil redisUtil;


    private final UserRoleService  userRoleService;

    private final StudentService studentService;

    private final RoleService roleService;

    @Lazy
    @Autowired
    public UserServiceImpl(RedisUtil redisUtil, UserRoleService userRoleService, StudentService studentService, RoleService roleService) {
        this.redisUtil = redisUtil;
        this.userRoleService = userRoleService;
        this.studentService = studentService;
        this.roleService = roleService;
    }

    @Override
    public Boolean regist(UserDTO dto, String code) {
        // 从缓存中获取验证码
        if(redisUtil.getTime(RedisConstants.EMAIL_CODE.getKey() + dto.getEmail())==0){
            throw new CustomException(CodeEnum.CODE_EXPIRED);
        }
        if(!code.equals(redisUtil.getObject(RedisConstants.EMAIL_CODE.getKey() + dto.getEmail()).toString())){
            throw new CustomException(CodeEnum.CODE_ERROR);
        }
        Long count = lambdaQuery().eq(User::getUserName, dto.getUserName()).count();
        if(count > 0) {
            throw new CustomException(CodeEnum.USERNAME_EXIST);
        }
        count = lambdaQuery().eq(User::getEmail,dto.getEmail()).count();
        if(count > 0) {
            throw new CustomException(CodeEnum.EMAIL_EXIST);
        }
        User user = dto.toPo(User.class);
        boolean saved = save(user);
        // 添加角色
        Role role = roleService.lambdaQuery().eq(Role::getRoleKey, "user").one();
        bindRole(user.getId(), role.getId());
        user.setPassword(PasswordUtils.encrypt(user.getPassword()));
        return  saved;
    }

    @Override
    public String login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();
        User user = getUserByName(username);
        if(user == null){
            user = getUserByEmail(username);
            if(user == null){
                throw new CustomException(CodeEnum.USER_NOT_FOUND);
            }
        }
        if(PasswordUtils.match(password, user.getPassword())){
            throw new CustomException(CodeEnum.PASSWORD_ERROR);
        }
        StpUtil.login(user.getId());
        redisUtil.set(RedisConstants.USER_TOKEN, String.valueOf(user.getId()),StpUtil.getTokenValue());
        redisUtil.set(RedisConstants.USER,String.valueOf(user.getId()), JSON.toJSONString(user));
        return StpUtil.getTokenValue();
    }

    @Override
    public User getUserByName(String username) {
        return lambdaQuery().select(User::getId,User::getUserName,User::getPassword,User::getType,User::getAvatar).eq(User::getUserName,username).one();
    }

    @Override
    public Boolean bindRole(Long userId, Long roleId) {
        UserRole userRole = new UserRole().setUserId(userId).setRoleId(roleId);
        return userRoleService.save(userRole);
    }

    @Override
    public Boolean unbindRole(Long userId, Long roleId) {
        return userRoleService.unbind(userId,roleId);
    }

    @Override
    public User appendInfo(User user) {
        List<Long> roleIdsByUserId = userRoleService.getRoleIdsByUserId(user.getId());
        List<String> roleNamesByUserId = userRoleService.getRoleNamesByUserId(user.getId());
        return user.setRoleIds(roleIdsByUserId).setRoleNames(roleNamesByUserId);
    }

    @Override
    public Boolean delete(Long id) {
        // 判断该用户是否为学生
        Long count = studentService.lambdaQuery().eq(Student::getUid, id).count();
        if(count > 0){
            throw new CustomException(CodeEnum.FORBIDDEN_ERROR);
        }
        return removeById(id);
    }

    @Override
    public Result<Void> save(UserDTO instance) {
        // 密码加密
        instance.setPassword(PasswordUtils.encrypt(instance.getPassword()));
        // 保存
        User user = instance.toPo(User.class);
        // 添加权限
        boolean saved = save(user);
        // 添加角色
        Role role = roleService.lambdaQuery().eq(Role::getRoleKey, "user").one();
        bindRole(user.getId(), role.getId());
        return Result.isSuccess(saved);
    }

    private User getUserByEmail(String email){
        return lambdaQuery().select(User::getId,User::getUserName,User::getPassword,User::getType)
                .eq(User::getEmail,email)
                .one();
    }
}




