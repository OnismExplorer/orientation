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
 * 任务
 */
@TableName(value = "`task`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Task extends BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
      * 任务名称
      */
    @TableField(value = "`name`")
    @Schema(description = "任务名称")
    private String name;

    /**
     * 任务内容
     */
    @TableField(value = "`content`")
    @Schema(description = "任务内容")
    private String content;
    /**
     * 开始时间
     */
    @TableField(value = "`start`")
    @Schema(description = "开始时间")
    private Date start;
    /**
     * 结束时间
     */
    @TableField(value = "`end`")
    @Schema(description = "结束时间")
    private Date end;
    /**
     * 积分数
     */
    @TableField(value = "`points`")
    @Schema(description = "积分数")
    private Integer points;
    /**
     * 计分标准(计分规则)
     */
    @TableField(value = "score_standard")
    @Schema(description = "计分标准")
    private String scoreStandard;
    /**
     * 任务状态(0：未开始 1：进行中 2：已结束)
     */
    @TableField(value = "`state`")
    @Schema(description = "任务状态(0：未开始 1：进行中 2：已结束)")
    private Integer state;
    /**
     * 任务类型(0：主线任务 1：支线任务)
     */
    @TableField(value = "`type`")
    @Schema(description = "任务类型(0：主线任务 1：支线任务，必须有父任务)")
    private Integer type;
    /**
     * 父任务id(禁止为本任务id)
     */
    @TableField(value = "parent_id")
    @Schema(description = "父任务id(禁止为本任务id)")
    private Long parentId;
}