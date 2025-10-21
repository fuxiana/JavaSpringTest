package com.xiaofutest.model.wx;

import lombok.Data;

@Data
public class PhoneNumberResponse {
    private boolean success;
    private String phoneNumber;
    private String message;

    public static PhoneNumberResponse success(String phoneNumber) {
        PhoneNumberResponse response = new PhoneNumberResponse();
        response.setSuccess(true);
        response.setPhoneNumber(phoneNumber);
        return response;
    }

    public static PhoneNumberResponse fail(String message) {
        PhoneNumberResponse response = new PhoneNumberResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}