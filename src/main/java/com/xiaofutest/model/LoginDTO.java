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


    /**
     * token
     */
    private String token;

    public LoginDTO(Long id, String username, String password, String token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
