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
 * @TableName activity_log
 */
@TableName(value = "activity_log")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ActivityLog extends BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;
    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    @Schema(description = "活动id")
    private Long activityId;
    /**
     * 参与活动用户id
     */
    @TableField(value = "uid")
    @Schema(description = "参与活动用户id")
    private Long uid;
    /**
     * 提交材料
     */
    @TableField(value = "material")
    @Schema(description = "提交材料")
    private String material;
    /**
     * 活动完成时间
     */
    @TableField(value = "finish_time")
    @Schema(description = "活动完成时间")
    private Date finishTime;
    /**
     * 活动状态(0：未完成 1：已完成 2：进行中)
     */
    @TableField(value = "`state`")
    @Schema(description = "活动状态(0：未完成 1：已完成 2：进行中)")
    private Integer state;
}