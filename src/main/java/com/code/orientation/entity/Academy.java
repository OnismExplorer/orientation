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

/**
 * 学院
 */
@TableName(value = "`academy`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Academy extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;
    /**
     * 学院名称
     */
    @TableField(value = "`name`")
    @Schema(description = "学院名称")
    private String name;

    /**
     * 学院logo
     */
    @TableField(value = "`coverd`")
    @Schema(description = "学院logo")
    private String coverd;
    /**
     * 学院描述
     */
    @TableField(value = "`illustration`")
    @Schema(description = "学院描述")
    private String illustration;
    /**
     * 学院学生数量
     */
    @TableField(value = "student_count")
    private Integer studentCount;
    /**
     * 优先级(数字越小优先级越小)
     */
    @TableField(value = "`priority`")
    @Schema(description = "优先级(数字越小优先级越小)")
    private Integer priority;
}