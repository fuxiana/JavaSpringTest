package com.xiaofutest.model;

public class OssDTO {

    /**
     *  id 标识
     */
    private Long id;

    /**
     *  KeyID
     */
    private  String accessKeyId;

    /**
     *  KeySecret
     */
    private  String accessKeySecret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
