package com.xiaofutest.model.wx;

import lombok.Data;

@Data
public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String country;
    private String province;
    private String city;
}
