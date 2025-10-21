package com.xiaofutest.service;

import com.xiaofutest.model.wx.*;
import com.xiaofutest.unit.JwtUtil;
import com.xiaofutest.unit.WxDataCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WxDataCryptUtil wxDataCryptUtil;

    // 使用内存缓存替代 Redis
    private ConcurrentHashMap<String, Object> memoryCache = new ConcurrentHashMap<>();
    // 使用内存缓存 sessionKey
    private ConcurrentHashMap<String, String> sessionKeyCache = new ConcurrentHashMap<>();

    /**
     * 更新用户手机号
     */
    private void updateUserPhoneNumber(String openid, String phoneNumber) {
        // 这里可以更新数据库中的用户手机号
        System.out.println("更新用户手机号: openid=" + openid + ", phone=" + phoneNumber);

        // 更新缓存中的用户信息
        // 实际项目中应该从数据库查询并更新
    }

    /**
     * 微信登录
     */
    public LoginResponse wxLogin(String code, UserInfo userInfo) {
        try {
            // 模拟获取openid（实际需要调用微信接口）
            String openid = generateOpenid(code);

            // 查找或创建用户
            User user = findOrCreateUser(openid, userInfo);

            // 生成token
            String token = jwtUtil.generateToken(openid);

            // 缓存用户信息到内存
            cacheUserInfo(token, user);

            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setToken(token);
            response.setUserInfo(userInfo);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return LoginResponse.fail("登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取手机号码
     */
    public PhoneNumberResponse getPhoneNumber(String token, String encryptedData, String iv) {
        try {
            // 从token中获取openid
            String openid = jwtUtil.getOpenid(token);
            if (openid == null) {
                return PhoneNumberResponse.fail("token无效");
            }

            // 从缓存获取sessionKey
            String sessionKey = sessionKeyCache.get(openid);
            if (sessionKey == null) {
                return PhoneNumberResponse.fail("sessionKey不存在，请重新登录");
            }

            // 解密手机号数据
            PhoneInfo phoneInfo = wxDataCryptUtil.decryptPhoneNumber(sessionKey, encryptedData, iv);

            // 更新用户手机号信息（这里可以保存到数据库）
            updateUserPhoneNumber(openid, phoneInfo.getPhoneNumber());

            return PhoneNumberResponse.success(phoneInfo.getPhoneNumber());

        } catch (Exception e) {
            e.printStackTrace();
            return PhoneNumberResponse.fail("获取手机号失败: " + e.getMessage());
        }
    }

    /**
     * 生成模拟的openid
     */
    private String generateOpenid(String code) {
        // 这里应该调用微信接口获取真实的openid
        // 暂时使用模拟数据
        return "mock_openid_" + System.currentTimeMillis();
    }

    /**
     * 查找或创建用户
     */
    private User findOrCreateUser(String openid, UserInfo userInfo) {
        User user = new User();
        user.setOpenid(openid);
        user.setNickName(userInfo.getNickName());
        user.setAvatarUrl(userInfo.getAvatarUrl());
        user.setGender(userInfo.getGender());
        user.setCountry(userInfo.getCountry());
        user.setProvince(userInfo.getProvince());
        user.setCity(userInfo.getCity());
        user.setCreateTime(new Date());

        // 这里可以保存到数据库
        // userRepository.save(user);

        return user;
    }

    /**
     * 缓存用户信息到内存
     */
    private void cacheUserInfo(String token, User user) {
        String key = "user:token:" + token;
        memoryCache.put(key, user);
    }

    /**
     * 根据token获取用户信息
     */
    public User getUserByToken(String token) {
        String key = "user:token:" + token;
        return (User) memoryCache.get(key);
    }

    /**
     * 退出登录
     */
    public void logout(String token) {
        String key = "user:token:" + token;
        memoryCache.remove(key);
    }
}