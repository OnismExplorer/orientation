package com.code.orientation.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class StudentExcelDTO {

    /**
     * 名字
     */
    @ExcelProperty("学生姓名")
    @Schema(description = "学生姓名")
    @ColumnWidth(12)
    private String name;

    /**
     * 性别
     */
    @ExcelProperty("性别")
    @Schema(description = "性别")
    @ColumnWidth(10)
    private String sex;

    /**
     * 身份证号码
     */
    @ExcelProperty("身份证号码")
    @Schema(description = "身份证号码")
    @ColumnWidth(50)
    private String identityCard;

    /**
     * 所属学院
     */
    @ExcelProperty("所属学院")
    @Schema(description = "所属学院")
    @ColumnWidth(20)
    private String department;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    @Schema(description = "手机号")
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty("邮箱地址")
    @Schema(description = "邮箱地址")
    @ColumnWidth(20)
    private String email;
}
