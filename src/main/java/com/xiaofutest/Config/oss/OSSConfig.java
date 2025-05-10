package com.xiaofutest.Config.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xiaofutest.unit.PropertyUtils;

import static com.xiaofutest.repository.OSSUploader.getOssConfig;

public class OSSConfig {

    public static final String BUCKET_NAME = PropertyUtils.getProperty("spring.oss.BUCKET_NAME");
    public static final String ENDPOINT =  PropertyUtils.getProperty("spring.oss.ENDPOINT");
    public static final String ACCESS_KEY_ID =  getOssConfig(1).getAccessKeyId();
    public static final String ACCESS_KEY_SECRET = getOssConfig(1).getAccessKeySecret();

    // 创建 OSS 客户端实例
    public static OSS createOSSClient() {
        return new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }
}