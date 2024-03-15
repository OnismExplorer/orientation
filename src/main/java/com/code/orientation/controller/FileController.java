package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.code.orientation.common.Result;
import com.code.orientation.exception.CustomException;
import com.code.orientation.utils.AliYunProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 文件控制器
 *
 */
@Tag(name = "文件管理模块")
@RestController
@RequestMapping("/file")
@SaCheckLogin
public class FileController {
    /**
     * 上传
     *
     * @param file 文件
     * @return {@link Result}<{@link String}>
     */
    @Operation(summary = "上传文件",description = "上传图片的通用接口，只能上传图片，且图片大小不超过3M(如果嫌小可以在配置文件中改大，不过消耗的流量也会因此增大)，返回结果即为图片地址")
    @PostMapping("upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if(file.getSize() > 3145728L){
            throw new CustomException("图片大小最大为3M！");
        }
        String endPoint = AliYunProperties.END_POINT;
        String bucketName = AliYunProperties.BUCKET_NAME;
        String keyId = AliYunProperties.KEY_ID;
        String keySecret = AliYunProperties.KEY_SECRET;
        String foldername = AliYunProperties.FOLDER_NAME;
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename;
        if (originalFilename != null) {
            // 获取文件后缀名
            suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            if(!".jpg".equals(suffixName) && !".png".equals(suffixName) && !".jpeg".equals(suffixName)){
                throw new CustomException("图片格式错误！");
            }
        }
        String filename = foldername + "/" +new DateTime().toString("yyyy/MM/dd/") + UUID.randomUUID() + suffixName;
        String url;
//        创建oss对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
//        传输
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, file.getInputStream());
            ossClient.putObject(putObjectRequest);
            url = "https://" + bucketName + "." + endPoint + "/" + filename;
        } catch (IOException e) {
            throw new CustomException("文件上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return Result.success(url);
    }
}
