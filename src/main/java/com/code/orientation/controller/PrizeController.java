package com.code.orientation.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Prize;
import com.code.orientation.entity.dto.PrizeDTO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.service.PrizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 奖品管理
 *
 * @author HeXin
 * @date 2024/04/11
 */
@Tag(name = "奖品模块", description = "积分兑换奖品")
@RestController
@RequestMapping("/prize")
public class PrizeController extends BaseController<PrizeService, Prize, PrizeDTO, Long> {

    private final PrizeService prizeService;

    @Autowired
    public PrizeController(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @Operation(summary = "获取奖品分页列表", description = "type为排序规则，默认(-1)按库存升序，0按库存降序，1按价格升序，2按价格降序")
    @GetMapping("/page")
    public Result<PageInfo<Prize>> page(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                        @RequestParam(required = false, defaultValue = "10") Long pageSize,
                                        @RequestParam(required = false) String key,
                                        @RequestParam(required = false, defaultValue = "-1") Integer type) {
        LambdaQueryChainWrapper<Prize> wrapper = prizeService.lambdaQuery().like(StringUtils.isNotBlank(key), Prize::getName, key)
                .or()
                .like(StringUtils.isNotBlank(key), Prize::getDescription, key);
        switch (type) {
            case -1 -> wrapper.orderByAsc(Prize::getInventory);
            case 0 -> wrapper.orderByDesc(Prize::getInventory);
            case 1 -> wrapper.orderByAsc(Prize::getPrice);
            case 2 -> wrapper.orderByDesc(Prize::getPrice);
            default ->
                // 处理未知的 type
                    throw new CustomException("Unknown type: " + type);
        }
        return Result.page(wrapper.page(new Page<>(pageNum, pageSize)));
    }

    @Operation(summary = "积分兑换")
    @PostMapping("/exchange/{uid}/{id}")
    public Result<String> exchange(@PathVariable Long uid,@PathVariable Long id) {
        return prizeService.exchange(uid,id);
    }

    @Override
    protected Class<Prize> createInstance() {
        return Prize.class;
    }
}
