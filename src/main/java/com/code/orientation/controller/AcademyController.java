package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Academy;
import com.code.orientation.entity.dto.AcademyDTO;
import com.code.orientation.entity.vo.StudentCount;
import com.code.orientation.exception.CustomException;
import com.code.orientation.service.AcademyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学院管理
 * @author HeXin
 * @date 2024/04/10
 */
@Tag(name = "学院模块")
@RestController
@RequestMapping("/academy")
public class AcademyController extends BaseController<AcademyService, Academy, AcademyDTO, Long> {

    private final AcademyService academyService;

    @Autowired
    public AcademyController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @Override
    @Operation(summary = "保存学院信息",description = "保存学院信息")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody AcademyDTO instance) {
        // 判断学院名称是否重复
        Long count = academyService.lambdaQuery().eq(Academy::getName, instance.getName()).count();
        if(count > 0) {
            return Result.fail(294,"该学院已存在！");
        }
        return super.save(instance);
    }

    @Operation(summary = "分页查询学院信息",description = "key为搜索关键词，type为排序规则，默认为-1，按优先级排序。0按名称升序，1按名称降序，2与3为按学生总人数升降序")
    @GetMapping("/page")
    @SaCheckPermission("user.user.get")
    public Result<PageInfo<Academy>> page(@RequestParam(required = false,defaultValue = "1") Long pageNum,
                                       @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                       @RequestParam(required = false)String key,
                                       @RequestParam(required = false,defaultValue = "-1")Integer type){
        LambdaQueryChainWrapper<Academy> wrapper = academyService.lambdaQuery().like(StringUtils.isNotBlank(key), Academy::getName, key)
                .or()
                .like(StringUtils.isNotBlank(key), Academy::getIllustration, key);
        switch (type) {
            case -1 -> wrapper.orderByDesc(Academy::getPriority);
            case 0 -> wrapper.orderByAsc(Academy::getName);
            case 1 -> wrapper.orderByDesc(Academy::getName);
            case 2 -> wrapper.orderByAsc(Academy::getStudentCount);
            case 3 -> wrapper.orderByDesc(Academy::getStudentCount);
            default ->
                // 处理未知的 type，可以选择抛出异常或采取其他处理方式
                    throw new CustomException("Unknown type: " + type);
        }

        return Result.page(wrapper.page(new Page<>(pageNum, pageSize)));
    }

    @Operation(summary = "学院目前实际人数")
    @GetMapping("/student/count")
    public Result<StudentCount> getStudentCount(@RequestParam Long id) {
        return Result.success(academyService.count(id));
    }

    @Override
    protected Class<Academy> createInstance() {
        return Academy.class;
    }
}
