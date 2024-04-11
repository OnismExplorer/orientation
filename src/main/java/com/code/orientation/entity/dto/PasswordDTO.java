package com.code.orientation.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 密码 DTO
 *
 * @author HeXin
 * @date 2024/04/11
 */
public record PasswordDTO(@Schema(description = "旧密码") String oldPassword, @Schema(description = "新密码") String newPassword,@Schema(description = "二次确认新密码") String confirmPassword) {
}
