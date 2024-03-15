package com.code.orientation.controller.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;

/**
 * 抽象控制器
 */
public abstract class AbstractController {
    public Result<Void> isSuccess(Boolean isSuccess) {
        return Result.isSuccess(isSuccess);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return {@link Result}<{@link T}>
     */
    public <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 成功
     *
     * @return {@link Result}<{@link T}>
     */
    public <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 失败
     *
     * @return {@link Result}
     */
    public Result<?> fail() {
        return Result.fail();
    }

    /**
     * 失败
     *
     * @param message 消息
     * @return {@link Result}<{@link ?}>
     */
    public Result fail(String message) {
        return Result.fail(message);
    }

    /**
     * 失败
     *
     * @param codeEnum 结果枚举
     * @return {@link Result}<{@link Void}>
     */
    public <T> Result<T> fail(CodeEnum codeEnum) {
        return Result.fail(codeEnum);
    }


    /**
     * 页
     *
     * @param page 页
     * @return {@link Result}<{@link PageInfo}<{@link P}>>
     */
    public <P> Result<PageInfo<P>> page(IPage<P> page) {
        return Result.page(page);
    }

    /**
     * 后果
     *
     * @param codeEnum 结果枚举
     * @return {@link Result}<{@link ?}>
     */
    public Result<?> result(CodeEnum codeEnum) {
        return Result.result(codeEnum);
    }
}
