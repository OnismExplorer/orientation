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
 * 广告
 */
@TableName(value = "`advertisement`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Advertisement extends BaseEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;
    /**
     * 赞助商名称
     */
    @TableField(value = "`name`")
    @Schema(description = "赞助商名称")
    private String name;
    /**
     * 赞助商描述
     */
    @TableField(value = "`description`")
    @Schema(description = "赞助商描述")
    private String description;
    /**
     * 赞助商logo
     */
    @TableField(value = "`logo`")
    @Schema(description = "赞助商logo")
    private String logo;
    /**
     * 赞助商友链
     */
    @TableField(value = "`link`")
    @Schema(description = "赞助商友链")
    private String link;
    /**
     * 优先级(数字越小优先级越小)
     */
    @TableField(value = "`priority`")
    @Schema(description = "优先级(数字越小优先级越小)")
    private Integer priority;
}