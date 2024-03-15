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
 * 任务日志
 */
@TableName(value = "task_log")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TaskLog extends BaseEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;
    /**
     * 任务id
     */
    @TableField(value = "task_id")
    @Schema(description = "任务id")
    private Long taskId;
    /**
     * 用户id
     */
    @TableField(value = "uid")
    @Schema(description = "用户id")
    private Long uid;
    /**
     * 提交的任务材料
     */
    @TableField(value = "material")
    @Schema(description = "提交的任务材料")
    private String material;
    /**
     * 任务完成时间
     */
    @TableField(value = "finish_time")
    @Schema(description = "任务完成时间")
    private Date finishTime;
    /**
     * 任务状态(0：未完成 1：已完成 2：进行中)
     */
    @TableField(value = "state")
    @Schema(description = "任务状态(0：未完成 1：已完成 2：进行中)")
    private Integer state;
}