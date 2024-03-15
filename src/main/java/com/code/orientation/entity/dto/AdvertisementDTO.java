package com.code.orientation.entity.dto;

import com.code.orientation.entity.Advertisement;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AdvertisementDTO extends ConverEntity<Advertisement> {
    private Long id;

    @Schema(description = "赞助商名称")
    private String name;

    @Schema(description = "赞助商简介")
    private String description;

    @Schema(description = "赞助商logo")
    private String logo;

    @Schema(description = "赞助商链接")
    private String link;

    @Schema(description = "优先级")
    private Integer priority;
}
