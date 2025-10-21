package com.xiaofutest.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaofutest.model.wx.PhoneInfo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@Component
public class WxDataCryptUtil {

    @Value("${wx.miniapp.appid}")
    private String appId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 解密微信加密数据
     */
    public String decrypt(String sessionKey, String encryptedData, String iv) {
        try {
            // Base64解码
            byte[] sessionKeyBytes = Base64.decodeBase64(sessionKey);
            byte[] encryptedDataBytes = Base64.decodeBase64(encryptedData);
            byte[] ivBytes = Base64.decodeBase64(iv);

            // 初始化AES算法
            SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivBytes);

            // 解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            byte[] resultBytes = cipher.doFinal(encryptedDataBytes);

            return new String(resultBytes, "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException("数据解密失败", e);
        }
    }

    /**
     * 解密手机号数据
     */
    public PhoneInfo decryptPhoneNumber(String sessionKey, String encryptedData, String iv) {
        try {
            String decryptedData = decrypt(sessionKey, encryptedData, iv);
            return objectMapper.readValue(decryptedData, PhoneInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("手机号解密失败", e);
        }
    }
}