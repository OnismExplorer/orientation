package com.code.orientation.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生注册DTO
 *
 * @author HeXin
 * @date 2024/03/21
 */
@Data
public class RegisterDTO {
    /**
     * 用户名
     */
    @Schema(description = "学生姓名")
    private String username;
    /**
     * 身份证
     */
    @Schema(description = "学生身份证号")
    private String identityCard;

    /**
     * 录取通知书编号
     */
    @Schema(description = "录取通知书编号")
    private String admissionLetter;

    /**
     * 学院名称
     */
    @Schema(description = "学院名称")
    private String departmentName;
}
