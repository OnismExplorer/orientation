package com.code.orientation.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class StudentVO {
    /**
     * id
     */
    @TableId(value = "id")
    @Schema(description = "id")
    private Long id;

    /**
     * 真实名字
     */
    @TableField(value = "`name`")
    @Schema(description = "真实名字")
    private String name;

    /**
     * 0女,1男
     */
    @Schema(description = "0女,1男")
    private Character sex;
    /**
     * 身份证号
     */
    @Schema(description = "身份证号")
    private String identityCard;
    /**
     * 录取通知书
     */
    @Schema(description = "录取通知书")
    private String admissionLetter;
    /**
     * 联系方式
     */
    @Schema(description = "联系方式")
    private Map<String, String> contact;
    /**
     * 学院
     */
    @Schema(description = "学院")
    private String department;
    /**
     * 学生积分数
     */
    @Schema(description = "学生积分数")
    private Integer points;
    /**
     * 状态(0：未报道 1：已报道)
     */
    @Schema(description = "状态(0：未报道 1：已报道)")
    private String state;
    @Schema(description = "头像")
    private String avatar;
}
