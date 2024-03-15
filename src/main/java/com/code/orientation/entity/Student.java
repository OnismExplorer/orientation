package com.code.orientation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.code.orientation.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @TableName student
 */
@TableName(value = "`student`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 真实名字
     */
    @TableField(value = "`name`")
    @Schema(description = "真实名字")
    private String name;

    /**
     * 0女,1男
     */
    @TableField(value = "`sex`")
    @Schema(description = "0女,1男")
    private Integer sex;
    /**
     * 身份证号
     */
    @TableField(value = "identity_card")
    @Schema(description = "身份证号")
    private String identityCard;
    /**
     * 录取通知书
     */
    @TableField(value = "admission_letter")
    @Schema(description = "录取通知书")
    private String admissionLetter;
    /**
     * 联系方式
     */
    @TableField(value = "`contact`", typeHandler = JacksonTypeHandler.class)
    @Schema(description = "联系方式")
    private Map<String, String> contact = Collections.emptyMap();
    /**
     * 学院
     */
    @TableField(value = "department_id")
    @Schema(description = "学院")
    private Long departmentId;
    /**
     * 学生积分数
     */
    @TableField(value = "points")
    @Schema(description = "学生积分数")
    private Integer points;
    /**
     * 状态(0：未报道 1：已报道)
     */
    @TableField(value = "`state`")
    @Schema(description = "状态(0：未报道 1：已报道)")
    private Integer state;
    /**
     * 用户id
     */
    @TableField(value = "uid")
    @Schema(description = "用户id")
    private Long uid;
}