package com.CloudBike.controller.common;

import com.CloudBike.constant.MessageConstant;
import com.CloudBike.exception.BaseException;
import com.CloudBike.result.Result;
import com.CloudBike.utils.AliOssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final AliOssUtil aliOssUtil;

    /**
     * 文件上传接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException
    {
        try
        {
            log.info("文件上传:{}",file);
            String originalFilename = file.getOriginalFilename();
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            String upload = aliOssUtil.upload(file.getBytes(), UUID.randomUUID().toString() + extention);
            return Result.success(upload);
        } catch (IOException e)
        {
            throw new BaseException(MessageConstant.UPLOAD_FAILED);
        }
    }
}
