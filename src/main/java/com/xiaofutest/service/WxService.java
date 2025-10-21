package com.xiaofutest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaofutest.model.wx.WxLoginResponse;
import com.xiaofutest.unit.HttpUtil;
import com.xiaofutest.unit.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxService {
    private String appid = PropertyUtils.getProperty("wx.miniapp.appid");

    private String secret = PropertyUtils.getProperty("wx.miniapp.secret");

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 通过code获取openid和session_key
     */
    public WxLoginResponse code2Session(String code) throws Exception {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, secret, code
        );

        String response = httpUtil.doGet(url);
        return objectMapper.readValue(response, WxLoginResponse.class);
    }

}
