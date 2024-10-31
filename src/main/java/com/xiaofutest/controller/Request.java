package com.xiaofutest.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 请求的包
@RestController
public class Request {
    @RequestMapping("/request")
    public String request(HttpServletRequest request){
        System.out.println(request.getParameter("name"));
        return "request1111"+request.getParameter("name");
    }


}
