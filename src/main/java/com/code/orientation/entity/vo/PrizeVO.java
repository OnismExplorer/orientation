package com.code.orientation.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 奖品 Vo
 *
 * @author HeXin
 * @date 2024/04/10
 */
@Data
public class PrizeVO {
    /**
     * id
     */
    @Schema(description = "id")
    private Long id;
    /**
     * 奖品名称
     */
    @Schema(description = "奖品名称")
    private String name;

    @Schema(description = "奖品logo")
    private String coverd;

    /**
     * 奖品描述
     */
    @Schema(description = "奖品描述")
    private String description;

    /**
     * 奖品价格(所需积分数)
     */
    @Schema(description = "奖品价格(所需积分数)")
    private Integer price;

    /**
     * 奖品库存
     */
    @Schema(description = "奖品库存")
    private Integer inventory;

    /**
     * 限制兑换数量
     */
    @Schema(description = "限制兑换数量")
    private Integer limited;

    /**
     * 奖品状态(0：已售空 1：兑换中 2：已下架)
     */
    @Schema(description = "奖品状态(0：已售空 1：兑换中 2：已下架)")
    private Integer state;
}
