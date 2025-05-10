package com.xiaofutest.controller;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.xiaofutest.Config.oss.OSSConfig;
import com.xiaofutest.repository.BookDB;
import com.xiaofutest.repository.OSSUploader;
import com.xiaofutest.unit.BaseResponse;
import com.xiaofutest.unit.ErrorCode;
import com.xiaofutest.unit.ResultUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("/oss")
public class UploadController {
    OSS ossClient = OSSConfig.createOSSClient();
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return  ResultUtils.error(ErrorCode.PARAMS_ERROR,"文件不能为空");
        }
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String url = OSSUploader.uploadInputStream(fileName, file.getInputStream());
            return  ResultUtils.success(fileName);
        } catch (IOException e) {
            System.out.print(e);
            e.printStackTrace();
            return  ResultUtils.error(ErrorCode.SYSTEM_ERROR,"上传失败");
        }
    }

    // 后端生成临时下载链接
    @GetMapping("/download/{fileName}")
    public BaseResponse<String> downloadFile(
            @PathVariable String fileName) {

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                OSSConfig.BUCKET_NAME,
                fileName,
                HttpMethod.GET
        );
        request.setExpiration(new Date(System.currentTimeMillis() + 3600 * 24000)); // 24小时有效
        String downloadUrl = fixUrlSpaces(ossClient.generatePresignedUrl(request).toString());
        return ResultUtils.success(downloadUrl);
    }

    /**
     * 修复 URL 中的非法空格
     * @param rawUrl 原始包含空格的 URL
     * @return 修正后的合法 URL
     */
    public static String fixUrlSpaces(String rawUrl) {
        // 替换协议后的非法空格
        String fixed = rawUrl
                .replaceAll("(?i)http:\\s+//", "http://")
                .replaceAll("(?i)https:\\s+//", "https://");

        // 替换路径中的其他空格（保留已编码的 %20）
        fixed = fixed.replaceAll("(?<!%20)(\\s+)", "%20");

        return fixed;
    }
}