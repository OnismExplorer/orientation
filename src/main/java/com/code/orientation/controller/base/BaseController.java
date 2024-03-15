package com.code.orientation.controller.base;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.common.Result;
import com.code.orientation.entity.base.BaseEntity;
import com.code.orientation.entity.base.ConverEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 基本控制器
 */
@SaCheckLogin
public abstract class
BaseController<S extends IService<E>, E extends BaseEntity,T extends ConverEntity<E>, K extends Serializable>
        extends AbstractController {
    /**
     * 服务
     */
    @Autowired
    protected S service;

    private final Class<E> entity;

    protected BaseController(){
        // 通过子类传递泛型类型参数
        entity = createInstance();
    }
    protected abstract Class<E> createInstance();


    /**
     * 获取服务
     *
     * @return {@link S}
     */
    public S getService() {
        return service;
    }


    /**
     * 保存
     *
     * @param instance 实例
     * @return {@link Result}<{@link Void}>
     */
    @SaCheckPermission("admin.add")
    @Operation(summary = "新增",description = "id不需要填写，直接删除id那行")
    @PostMapping()
    public Result<Void> save(@RequestBody T instance) {
        service.save(instance.toPo(entity));
        return success();
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@link Result}<{@link Void}>
     */
    @SaCheckPermission("admin.delete")
    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "主键") @PathVariable K id) {
        service.removeById(id);
        return success();
    }

    /**
     * 获取
     *
     * @param id id
     * @return {@link Result}<{@link E}>
     */
    @SaCheckPermission("admin.get")
    @Operation(summary = "id查询")
    @GetMapping("/{id}")
    public Result<E> get(@Parameter(description = "主键") @PathVariable K id) {
        return success(service.getById(id));
    }

    /**
     * 更新
     *
     * @param instance 实例
     * @return {@link Result}<{@link ?}>
     */
    @SaCheckPermission("admin.update")
    @Operation(summary = "更新")
    @PutMapping
    public Result<Void> update(@RequestBody T instance) {
        return isSuccess(service.updateById(instance.toPo(entity)));
    }

    /**
     * 列表
     *
     * @return {@link Result}<{@link List}<{@link E}>>
     */
    @SaCheckPermission("admin.get")
    @Operation(summary = "列表")
    @GetMapping("/")
    public Result<List<E>> list() {
        return success(service.list());
    }
}
