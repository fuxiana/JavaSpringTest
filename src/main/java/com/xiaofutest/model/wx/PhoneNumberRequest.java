package com.xiaofutest.model.wx;

import lombok.Data;

@Data
public class PhoneNumberRequest {
    private String encryptedData;
    private String iv;
}