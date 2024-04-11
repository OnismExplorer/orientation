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
import java.util.Date;

/**
 * 活动
 */
@TableName(value = "`activity`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Activity extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 活动名称
     */
    @TableField(value = "`name`")
    @Schema(description = "活动名称")
    private String name;

    /**
     * 活动内容
     */
    @TableField(value = "`content`")
    @Schema(description = "活动内容")
    private String content;

    /**
     * 活动封面
     */
    @TableField(value = "`coverd`")
    @Schema(description = "活动封面")
    private String coverd;

    /**
     * 活动开始时间
     */
    @TableField(value = "`start`")
    @Schema(description = "活动开始时间")
    private Date start;

    /**
     * 活动结束时间
     */
    @TableField(value = "`end`")
    @Schema(description = "活动结束时间")
    private Date end;

    /**
     * 积分数
     */
    @TableField(value = "`points`")
    @Schema(description = "积分数")
    private Integer points;

    /**
     * 活动类型(0：长期活动 1：临时活动)
     */
    @TableField(value = "`type`")
    @Schema(description = "活动类型(0：长期活动 1：临时活动)")
    private Integer type;

    /**
     * 计分标准
     */
    @TableField(value = "score_standard")
    @Schema(description = "计分标准")
    private String scoreStandard;

    /**
     * 活动状态(0：未开始 1：进行中 2：已结束)
     */
    @TableField(value = "`state`")
    @Schema(description = "活动状态(0：未开始 1：进行中 2：已结束)")
    private Integer state;

    /**
     * 活动最大人数限制
     */
    @TableField(value = "max_number")
    @Schema(description = "活动最大人数限制")
    private Integer maxNumber;

    /**
     * 已参与活动人数
     */
    @TableField(value = "`count`")
    @Schema(description = "已参与活动人数")
    private Integer count;
}