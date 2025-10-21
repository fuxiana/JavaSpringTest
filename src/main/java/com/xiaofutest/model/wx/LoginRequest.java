package com.xiaofutest.model.wx;

import lombok.Data;

@Data
public class LoginRequest {
    private String code;
    private UserInfo userInfo;
}
