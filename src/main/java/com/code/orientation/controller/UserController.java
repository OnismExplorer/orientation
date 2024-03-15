package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.User;
import com.code.orientation.entity.dto.LoginParam;
import com.code.orientation.entity.dto.PasswordDTO;
import com.code.orientation.entity.dto.UserDTO;
import com.code.orientation.entity.vo.UserInfo;
import com.code.orientation.entity.vo.UserVO;
import com.code.orientation.service.RoleService;
import com.code.orientation.service.UserService;
import com.code.orientation.utils.EmailUtils;
import com.code.orientation.utils.PasswordUtils;
import com.code.orientation.utils.RedisUtil;
import com.code.orientation.utils.ValidateCodeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户模块")
@RestController
@SaCheckLogin
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User, UserDTO, Long> {
    @Override
    @PostMapping()
    public Result<Void> save(@RequestBody  UserDTO instance) {
        return super.save(instance.setPassword(PasswordUtils.encrypt(instance.getPassword())));
    }

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Operation(summary = "登录")
    @SaIgnore
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginParam param){
        return Result.success(userService.login(param));
    }

    @Operation(summary = "退出登录")
    @SaIgnore
    @GetMapping("/logout")
    public Result<String> logout(){
        StpUtil.logout(StpUtil.getLoginId());
        return Result.success();
    }

    @Operation(summary = "发送验证码")
    @SaIgnore
    @GetMapping("/sendCode")
    public Result<String> sendCode(@RequestParam("email") String email){
        EmailUtils.isValidEmail(email);
        String code = ValidateCodeUtils.generateValidateCodeUtils(6).toString();
        // 调用邮箱服务发送验证码
        emailUtils.sendMailMessage(email,code);
        // 将生成的验证码存入Redis
        redisUtil.set(RedisConstants.EMAIL_CODE,email,code);
        return Result.success();
    }

    @Operation(summary = "注册账号")
    @SaIgnore
    @PostMapping("/regist")
    public Result regist(@RequestBody UserDTO dto, @RequestParam("code") String code){
        return Result.isSuccess(userService.regist(dto,code));
    }

    @Operation(summary = "获取用户信息（登录时自动获取）")
    @GetMapping("/info")
    public Result<UserInfo> getUserInfo(){
        String id = StpUtil.getLoginIdAsString();
        List<String> roleNames = roleService.getRoleNameByUser(Long.parseLong(id));
        redisUtil.set(RedisConstants.USER_ROLE.getKey()+id,roleNames);
        User user =  redisUtil.get(RedisConstants.USER.getKey() + id,User.class);
        UserInfo info = new UserInfo().setName(user.getUserName())
        .setRoleNames(roleNames)
        .setId(id)
        .setAvatar(user.getAvatar());
        return Result.success(info);
    }

    @Operation(summary = "绑定角色")
    @SaCheckPermission("admin.user.add")
    @PostMapping("/bind/{userId}/{roleId}")
    public Result<Void> bind(@PathVariable Long userId, @PathVariable Long roleId){
        return Result.isSuccess(userService.bindRole(userId,roleId));
    }

    @Operation(summary = "解除角色绑定")
    @SaCheckPermission("admin.user.delete")
    @DeleteMapping("/unbind/{userId}/{roleId}")
    public Result<Void> unbind(@PathVariable Long userId, @PathVariable Long roleId){
        return Result.isSuccess(userService.unbindRole(userId,roleId));
    }

    @Override
    @Operation(summary="根据id获取用户信息（后台）")
    @SaCheckPermission("admin.user.get")
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable  Long id) {
        return super.get(id);
    }

    @Operation(summary="根据id获取用户信息（前台）")
    @SaCheckPermission("user.get")
    @GetMapping("/front/{id}")
    public Result<UserVO> getById(@PathVariable Long id){
        User user = userService.getById(id);
        UserVO vo = new UserVO().setAvatar(user.getAvatar())
                .setUid(user.getId())
                .setNickname(user.getNickname())
                .setUsername(user.getUserName());
        return success(vo);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/update/password")
    public Result<String> updatePassword(@RequestBody PasswordDTO passwordDTO){
        User user = userService.getById(StpUtil.getLoginIdAsString());
        if(!PasswordUtils.match(passwordDTO.oldPassword(),user.getPassword())){
            return Result.fail(CodeEnum.PASSWORD_ERROR);
        }
        if(!passwordDTO.confirmPassword().equals(passwordDTO.newPassword())){
            return  Result.fail(CodeEnum.PASSWORD_NOT_MATCH);
        }
        return Result.isSuccess(userService.updateById(user.setPassword(PasswordUtils.encrypt(passwordDTO.newPassword()))));
    }

    @Operation(summary = "重置密码(后台)")
    @SaCheckPermission("admin.user.update")
    @PatchMapping("/reset/password")
    public Result<Void> resetPassword (@RequestParam Long uid,@RequestParam String password){
        return Result.isSuccess(userService.lambdaUpdate().eq(User::getId,uid)
                .set(User::getPassword,PasswordUtils.encrypt(password))
                .update());
    }

    @Operation(summary = "分页查询")
    @SaCheckPermission("admin.user.get")
    @GetMapping("/page")
    public Result<PageInfo<User>> page(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                       @RequestParam(required = false, defaultValue = "10") Long pageSize,
                                       @RequestParam(required = false) String key,
                                       @RequestParam(required = false, defaultValue = "-1") Integer type,
                                       @RequestParam(required = false, defaultValue = "0") Integer withRole) {
        IPage<User> userPage = userService.lambdaQuery().eq(!type.equals(-1), User::getType, type).like(StringUtils.isNotBlank(key), User::getNickname, key).page(new Page<>(pageNum, pageSize));
        if (!withRole.equals(0)) {
            userPage = userPage.convert(userService::appendInfo);
        }
        return Result.page(userPage);
    }

    @Operation(summary = "修改用户")
    @PutMapping("/update")
    public Result<Void> update(@RequestBody User user){
        User u = userService.getById(user.getId());
        if(!PasswordUtils.match(user.getPassword(), u.getPassword())){
            user.setPassword(PasswordUtils.encrypt(user.getPassword()));
        }
        return Result.isSuccess(userService.updateById(user));
    }
    @Override
    protected Class<User> createInstance() {
        return User.class;
    }
}
