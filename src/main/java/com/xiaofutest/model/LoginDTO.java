package com.xiaofutest.model;

/**
 * @author :fuxianan
 * @date : 2024/11/29
 */
public class LoginDTO {
    /**
     *  id 标识
     */
    private Long id;

    /**
     *  账号
     */
    private String username;

    /**
     *  密码
     */
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
