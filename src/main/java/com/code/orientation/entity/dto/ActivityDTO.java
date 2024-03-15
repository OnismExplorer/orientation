package com.code.orientation.entity.dto;

import com.code.orientation.entity.Activity;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityDTO extends ConverEntity<Activity> {

    private Long id;
    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "活动内容")
    private String content;

    @Schema(description = "开始时间")
    private Date start;

    @Schema(description = "结束时间")
    private Date end;

    @Schema(description = "活动积分")
    private Integer points;

    @Schema(description = "活动类型(0：长期活动 1：临时活动)")
    private Integer type;

    @Schema(description = "评分标准")
    private String scoreStandard;

    @Schema(description = "活动状态(0：未开始 1：进行中 2：已结束)")
    private Integer state;

    @Schema(description = "最大参与人数")
    private Integer maxNumber;
}
