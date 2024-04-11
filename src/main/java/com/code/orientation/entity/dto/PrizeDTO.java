package com.code.orientation.entity.dto;

import com.code.orientation.entity.Prize;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 奖品 DTO
 *
 * @author HeXin
 * @date 2024/04/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PrizeDTO extends ConverEntity<Prize> {
    private Long id;
    /**
     * 奖品名称
     */
    @Schema(description = "奖品名称")
    private String name;

    @Schema(description = "奖品图片")
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
