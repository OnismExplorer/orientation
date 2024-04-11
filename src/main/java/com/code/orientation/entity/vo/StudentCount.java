package com.code.orientation.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生人数
 *
 * @author HeXin
 * @date 2024/04/07
 */
@Data
public class StudentCount {
    /**
     * 总实际人数
     */
    @Schema(description = "实际总人数")
    private Integer total;

    /**
     * 已报道人数
     */
    @Schema(description = "已报道人数")
    private Integer reported;

    /**
     * 未报道人数
     */
    @Schema(description = "未报道人数")
    private Integer unreport;
}
