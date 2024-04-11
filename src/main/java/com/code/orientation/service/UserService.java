package com.code.orientation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.common.Result;
import com.code.orientation.entity.User;
import com.code.orientation.entity.dto.LoginParam;
import com.code.orientation.entity.dto.UserDTO;

/**
 * @author HeXin
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-12-28 10:39:44
 */
public interface UserService extends IService<User> {

    Boolean regist(UserDTO dto, String code);

    String login(LoginParam param);

    User getUserByName(String username);

    Boolean bindRole(Long userId, Long roleId);

    Boolean unbindRole(Long userId, Long roleId);

    User appendInfo(User user);

    /**
     * 删除用户
     *
     * @param id 编号
     * @return {@link Boolean}
     */
    Boolean delete(Long id);

    /**
     * 新增
     *
     * @param instance 实例
     * @return {@link Result}<{@link Void}>
     */
    Result<Void> save(UserDTO instance);
}
