package com.code.orientation.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.exception.CustomException;
import com.code.orientation.handler.CustomSheetWriteHandler;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel 工具类
 *
 * @author HeXin
 * @date 2024/03/14
 */
public class ExcelUtil{

    private ExcelUtil(){

    }
    /**
     * 写出 Excel
     *
     * @param response 响应
     * @param name     名字
     * @param clazz    克拉兹
     * @param secures  保证
     * @param config   配置
     */
    public static void write(HttpServletResponse response, String name, Class<?> clazz, List secures, Consumer<ExcelWriterBuilder> config) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            var write = EasyExcelFactory.write(response.getOutputStream(), clazz);
            config.accept(write);
            write.autoCloseStream(Boolean.FALSE).sheet(name).doWrite(secures);
        } catch (Exception e) {
            throw new CustomException(CodeEnum.EXPORT_FAILED);
        }
    }

    public static void write(HttpServletResponse response, String name, Class<?> clazz, List secures) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 和easyexcel没有关系
            String fileName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            var write = EasyExcelFactory.write(response.getOutputStream(), clazz).registerWriteHandler(new CustomSheetWriteHandler());
            write.autoCloseStream(Boolean.FALSE).sheet(name).doWrite(secures);
        } catch (Exception e) {
            throw new CustomException(CodeEnum.EXPORT_FAILED);
        }
    }

}
