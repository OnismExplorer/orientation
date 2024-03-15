package com.code.orientation.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充配置
 */
@Component
public class UnifiedFieldFillHandel implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long id = StpUtil.getLoginId(1L);
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        //创建者
        this.setFieldValByName("createBy", id, metaObject);
        // 更新者
        this.setFieldValByName("updateBy", id, metaObject);
        // 逻辑删除默认置0
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long id;
        if(System.getProperty("os.name").contains("Linux")){
            id = 0L;
        } else {
            id = StpUtil.getLoginId(1L);
        }
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("updateBy", id, metaObject);
    }
}
