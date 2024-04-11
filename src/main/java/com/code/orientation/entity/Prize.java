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
 * 奖品
 *
 * @TableName prize
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="prize")
@Data
@Accessors(chain = true)
public class Prize extends BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 奖品名称
     */
    @TableField(value = "`name`")
    private String name;

    @TableField(value = "`coverd`")
    @Schema(description = "奖品图片")
    private String coverd;

    /**
     * 奖品描述
     */
    @TableField(value = "`description`")
    private String description;

    /**
     * 奖品价格(所需积分数)
     */
    @TableField(value = "`price`")
    private Integer price;

    /**
     * 奖品库存
     */
    @TableField(value = "`inventory`")
    private Integer inventory;

    /**
     * 限制兑换数量
     */
    @TableField(value = "`limited`")
    private Integer limited;

    /**
     * 奖品状态(0：已售空 1：兑换中 2：已下架)
     */
    @TableField(value = "`state`")
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}