package com.code.orientation.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TaskVO {
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务内容")
    private String content;

    @Schema(description = "任务开始时间")
    private Date start;

    @Schema(description = "任务结束时间")
    private Date end;

    @Schema(description = "任务积分")
    private Integer points;

    @Schema(description = "任务标准")
    private String scoreStandard;

    @Schema(description = "任务状态(0：未开始 1：进行中 2：已结束)")
    private Integer state;

    @Schema(description = "任务类型(0：主线任务 1：支线任务)")
    private Integer type;

    @Schema(description = "父任务(禁止为本任务id)")
    private String parent;
}
