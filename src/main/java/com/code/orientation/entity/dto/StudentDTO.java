package com.code.orientation.entity.dto;

import com.code.orientation.entity.Student;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StudentDTO extends ConverEntity<Student> {
    private Long id;

    @Schema(description = "用户id")
    private Long uid;
    @Schema(description = "学生姓名")
    private String name;
    @Schema(description = "性别：0女，1男")
    private Integer sex;
    @Schema(description = "身份证号")
    private String indentityCard;
    @Schema(description = "录取通知书")
    private String admissionLetter;
    @Schema(description = "联系方式")
    private Map<String, String> contact;
    @Schema(description = "学院id")
    private Long departmentId;
    @Schema(description = "学生积分数")
    private Integer points;
    @Schema(description = "学生状态：0未报到，1已报到")
    private Integer state;

}
