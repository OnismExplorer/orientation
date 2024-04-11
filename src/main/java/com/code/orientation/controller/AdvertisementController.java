package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Advertisement;
import com.code.orientation.entity.dto.AdvertisementDTO;
import com.code.orientation.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 广告管理
 *
 * @author HeXin
 * @date 2024/04/11
 */
@Tag(name = "广告模块")
@RestController
@RequestMapping("/ad")
public class AdvertisementController
        extends BaseController<AdvertisementService, Advertisement, AdvertisementDTO, Long> {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @Operation(summary = "赞助商分页信息",description = "key为搜索关键词，默认按优先级排序，没有别的排序方式")
    @GetMapping("/page")
    public Result<PageInfo<Advertisement>> page(@RequestParam(required = false,defaultValue = "1") Long pageNum,
                                                @RequestParam(required = false,defaultValue = "10")Long pageSize,
                                                @RequestParam(required = false) String key){
        IPage<Advertisement> page = advertisementService.lambdaQuery().like(StringUtils.isNotBlank(key), Advertisement::getName, key)
                .or()
                .like(StringUtils.isNotBlank(key), Advertisement::getDescription, key)
                .orderByDesc(Advertisement::getPriority)
                .page(new Page<>(pageNum, pageSize));
        return Result.page(page);
    }

    /**
     * 获取赞助商信息
     *
     * @param id 编号
     * @return {@link Result}<{@link AdvertisementDTO}>
     */
    @Operation(summary = "获取赞助商信息(前台)",description = "前台展示赞助商信息")
    @GetMapping("/info/{id}")
    @SaIgnore
    public Result<AdvertisementDTO> getInfo(@PathVariable Long id){
        Advertisement advertisement = advertisementService.getById(id);
        AdvertisementDTO ad = new AdvertisementDTO()
                .setName(advertisement.getName())
                .setDescription(advertisement.getDescription())
                .setLogo(advertisement.getLogo())
                .setLink(advertisement.getLink())
                .setPriority(advertisement.getPriority());
        return Result.success(ad);
    }

    @Override
    protected Class<Advertisement> createInstance() {
        return  Advertisement.class;
    }

}
