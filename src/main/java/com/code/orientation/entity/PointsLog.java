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
 * 积分日志
 */
@TableName(value = "points_log")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PointsLog extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;
    /**
     * 用户id
     */
    @TableField(value = "uid")
    @Schema(description = "用户id")
    private Long uid;
    /**
     * 本次所加积分数
     */
    @TableField(value = "`points`")
    @Schema(description = "本次所加积分数")
    private Integer points;
    /**
     * 加分说明
     */
    @TableField(value = "`description`")
    @Schema(description = "加分说明")
    private String description;
    /**
     * 积分类型(0：任务积分 1：活动积分)
     */
    @TableField(value = "`type`")
    @Schema(description = "积分类型(0：任务积分 1：活动积分)")
    private Integer type;
}