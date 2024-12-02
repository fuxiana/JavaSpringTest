package com.xiaofutest.controller;

import com.xiaofutest.model.LoginDTO;
import com.xiaofutest.repository.LoginDB;
import com.xiaofutest.unit.BaseResponse;
import com.xiaofutest.unit.BusinessException;
import com.xiaofutest.unit.ErrorCode;
import com.xiaofutest.unit.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :fuxianan
 * @date : 2024/11/29
 */

@RestController
@RequestMapping("/login")
public class Login {

    @PostMapping("/submit")
    public BaseResponse<Boolean> submit(HttpServletRequest request, @RequestBody LoginDTO loginDTO){

        if(ObjectUtils.isEmpty(loginDTO.getUsername())){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能为空");
        }
        if(ObjectUtils.isEmpty(loginDTO.getPassword())){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }

        Boolean aBoolean = LoginDB.login(loginDTO);

        return ResultUtils.success(aBoolean);
    }
}
