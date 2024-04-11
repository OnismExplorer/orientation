package com.code.orientation.entity.dto;

import com.code.orientation.entity.Academy;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 学院 DTO
 *
 * @author HeXin
 * @date 2024/04/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AcademyDTO extends ConverEntity<Academy> {
    private Long id;

    @Schema(description = "学院名称")
    private String name;

    @Schema(description = "学院logo")
    private String coverd;

    @Schema(description = "学院描述")
    private String illustration;

    @Schema(description = "学院学生数量")
    private Integer studentCount;

    @Schema(description = "学院优先级(数字越小，优先级越低)")
    private Integer priority;
}
