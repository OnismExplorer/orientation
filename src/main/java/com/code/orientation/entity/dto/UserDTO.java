package com.code.orientation.entity.dto;

import com.code.orientation.entity.User;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserDTO extends ConverEntity<User> {
    private Long id;
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "用户类型(0：学生，1：非学生)")
    private Integer type;
    @Schema(description = "用户状态(0：禁用 1：启用，默认为1)")
    private Integer state;

}