package com.xiaofutest.model.wx;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String openid;
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String country;
    private String province;
    private String city;
    private Date createTime;
}
