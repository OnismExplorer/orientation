package com.code.orientation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.code.orientation.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 兑换日志
 *
 * @TableName exchange_log
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="exchange_log")
@Data
@Accessors(chain = true)
public class ExchangeLog extends BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    /**
     * 奖品id
     */
    @TableField(value = "prize_id")
    @Schema(description = "奖品id")
    private Long prizeId;

    /**
     * 用户id
     */
    @TableField(value = "uid")
    @Schema(description = "用户id")
    private Long uid;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}