package com.xiaofutest.model.wx;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String token;
    private UserInfo userInfo;
    private String message;

    public static LoginResponse fail(String s) {
        return null;
    }
}
