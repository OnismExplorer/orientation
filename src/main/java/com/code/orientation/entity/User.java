package com.code.orientation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.code.orientation.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户
 */
@TableName(value = "`user`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;
    /**
     * 用户名
     */
    @TableField(value = "user_name")
    @Schema(description = "用户名")
    private String userName;
    /**
     * 密码
     */
    @TableField(value = "`password`")
    @Schema(description = "密码")
    private String password;
    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    @Schema(description = "用户昵称")
    private String nickname;
    /**
     * 头像
     */
    @TableField(value = "`avatar`")
    @Schema(description = "头像")
    private String avatar;
    /**
     * 邮箱地址
     */
    @TableField(value = "`email`")
    @Schema(description = "邮箱地址")
    private String email;
    /**
     * 手机号
     */
    @TableField(value = "phone")
    @Schema(description = "手机号")
    private String phone;
    /**
     * 用户类型(0：学生，1：非学生)
     */
    @TableField(value = "`type`")
    @Schema(description = "用户类型(0：学生，1：非学生)")
    private Integer type;
    /**
     * 用户状态(0：禁用 1：启用，默认为1)
     */
    @TableField(value = "`state`")
    @Schema(description = "用户状态(0：禁用 1：启用，默认为1)")
    private Integer state;
    @TableField(exist = false)
    @Schema(description = "角色名称")
    private List<String> roleNames;
    @TableField(exist = false)
    @Schema(description = "角色id")
    private List<Long> roleIds;
}