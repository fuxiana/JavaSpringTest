package com.xiaofutest.controller;

import com.xiaofutest.Config.token.PassToken;
import com.xiaofutest.model.wx.*;
import com.xiaofutest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * 微信登录
     */
    @PostMapping("/wxlogin")
    @PassToken
    public LoginResponse wxLogin(@RequestBody LoginRequest request) {
        return userService.wxLogin(request.getCode(), request.getUserInfo());
    }

    /**
     * 验证token
     */
    @PostMapping("/verify")
    @PassToken
    public Map<String, Object> verifyToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        // 去除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        User user = userService.getUserByToken(token);
        if (user != null) {
            result.put("success", true);
            result.put("userInfo", user);
        } else {
            result.put("success", false);
            result.put("message", "token无效");
        }

        return result;
    }

    /**
     * 获取手机号码
     */
    @PostMapping("/getPhoneNumber")
    public PhoneNumberResponse getPhoneNumber(
            @RequestHeader("Authorization") String token,
            @RequestBody PhoneNumberRequest request) {

        // 去除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return userService.getPhoneNumber(
                token,
                request.getEncryptedData(),
                request.getIv()
        );
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @PassToken
    public Map<String, Object> logout(@RequestHeader("Authorization") String token) {
        // 去除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        userService.logout(token);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "退出成功");
        return result;
    }
}
