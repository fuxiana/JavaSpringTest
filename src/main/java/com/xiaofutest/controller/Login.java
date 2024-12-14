package com.xiaofutest.controller;

import com.xiaofutest.Config.token.PassToken;
import com.xiaofutest.model.LoginDTO;
import com.xiaofutest.repository.LoginDB;
import com.xiaofutest.unit.BaseResponse;
import com.xiaofutest.unit.BusinessException;
import com.xiaofutest.unit.ErrorCode;
import com.xiaofutest.unit.ResultUtils;
import com.xiaofutest.Config.token.AdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/submit")
    @PassToken
    public BaseResponse<String> submit(HttpServletRequest request, @RequestBody LoginDTO loginDTO){
        if(ObjectUtils.isEmpty(loginDTO.getUsername())){
            throw  new BusinessException(ErrorCode.NULL_ERROR, "账号不能为空");
        }
        if(ObjectUtils.isEmpty(loginDTO.getPassword())){
            throw  new BusinessException(ErrorCode.NULL_ERROR, "密码不能为空");
        }

        Boolean aBoolean = LoginDB.login(loginDTO);
        if(!aBoolean){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码错误，请重新输入");
        }
        LoginDTO loginDTO1 = AdminServiceImpl.getMsg(loginDTO);
        return ResultUtils.success(loginDTO1.getToken());
    }

    @PostMapping("/register")
    @PassToken
    public BaseResponse<Boolean> register(HttpServletRequest request, @RequestBody LoginDTO loginDTO){
        if(ObjectUtils.isEmpty(loginDTO.getUsername())){
            throw  new BusinessException(ErrorCode.NULL_ERROR, "账号不能为空");
        }
        if(ObjectUtils.isEmpty(loginDTO.getPassword())){
            throw  new BusinessException(ErrorCode.NULL_ERROR, "密码不能为空");
        }

        Boolean aBoolean = LoginDB.query(loginDTO);
        if(aBoolean){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败，已有相同账号");
        }
        return ResultUtils.success(aBoolean);
    }
}
