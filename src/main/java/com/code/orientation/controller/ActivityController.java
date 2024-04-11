package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Activity;
import com.code.orientation.entity.dto.ActivityDTO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理
 * @author HeXin
 * @date 2024/04/10
 */
@Tag(name = "活动模块")
@RestController
@SaCheckLogin
@RequestMapping("/activity")
public class ActivityController extends BaseController<ActivityService, Activity, ActivityDTO, Long> {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Operation(summary = "获取活动分页信息",description = "key为搜索关键词，type为排序规则，默认为-1，按活动开始时间倒序排序，0按开始时间升序排序，1和2为按积分数升降序排序")
    @GetMapping("/page")
    public Result<PageInfo<Activity>> page(@RequestParam(required = false,defaultValue = "1") Long pageNum,
                                           @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                           @RequestParam(required = false)String key,
                                           @RequestParam(required = false,defaultValue = "-1")Integer type){
        LambdaQueryChainWrapper<Activity> wrapper = activityService.lambdaQuery().like(StringUtils.isNotBlank(key), Activity::getName, key)
                .or()
                .like(StringUtils.isNotBlank(key), Activity::getContent, key);
        switch (type) {
            case -1 -> wrapper.orderByDesc(Activity::getStart);
            case 0 -> wrapper.orderByAsc(Activity::getStart);
            case 1 -> wrapper.orderByAsc(Activity::getPoints);
            case 2 -> wrapper.orderByDesc(Activity::getPoints);
            default ->
                // 处理未知的 type
                    throw new CustomException("Unknown type: " + type);
        }

        return Result.page(wrapper.page(new Page<>(pageNum, pageSize)));
    }

    @Operation(summary = "开启活动",description = "每人每个活动至多只能开启三次(包括取消再开启)")
    @PostMapping("/begin/{id}/{uid}")
    public Result<String> begin(@PathVariable Long id,@PathVariable Long uid){
        activityService.begin(id,uid);
        return Result.success();
    }

    @Operation(summary = "完成活动")
    @PostMapping("/finish/{uid}/{id}")
    public Result<Void> finish(@PathVariable Long uid,@PathVariable Long id,
                               @RequestParam(required = false)String material){
        if(StringUtils.isNotBlank(material)){
            return Result.isSuccess(activityService.finish(uid,id,material));
        }
        return Result.isSuccess(activityService.finish(uid,id));
    }

    @Operation(summary = "取消活动")
    @PutMapping("/cancel/{id}/{uid}")
    public Result<Void> cancel(@PathVariable Long id,@PathVariable Long uid){
        activityService.cancel(id,uid);
        return Result.success();
    }
    @Override
    protected Class<Activity> createInstance() {
        return Activity.class;
    }

    @Override
    @Operation(summary = "新增活动")
    @PostMapping()
    public Result<Void> save(@RequestBody ActivityDTO instance) {
        return Result.isSuccess(activityService.save(instance));
    }

    @Operation(summary = "生成活动二维码",description = "id为活动id")
    @GetMapping("/encode")
    @SaIgnore
    public Result<String> encode(@RequestParam Long id,HttpServletResponse response){
        return Result.isSuccess(activityService.encode(id,response));
    }

    @Operation(summary = "校验二维码",description = "id为活动id，content为二维码扫描返回结果")
    @GetMapping("/decode")
    public Result<String> decode(@RequestParam Long id,@RequestParam String content) {
        return Result.isSuccess(activityService.decode(id,content));
    }
}
