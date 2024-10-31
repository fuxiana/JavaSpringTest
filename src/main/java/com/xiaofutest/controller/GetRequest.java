package com.xiaofutest.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 请求的包
@RestController
public class GetRequest {
    @RequestMapping("/getRequest/{id}")
    public String getRequest(@PathVariable Integer id){
        System.out.println(id);
        return "getRequest"+ id;
    }
}
