package com.xiaofutest.repository;

import java.sql.Connection;
import java.sql.DriverManager;

public class RequestDB {
    public static Connection getConnection() {
        // 数据库连接URL，格式为：jdbc:子协议:子名称
        String url = "jdbc:mysql://121.40.222.45/test_db?useUnicode=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        // 数据库用户名与密码
        String user = "root";
        String password = "fuxianan19961003";

        Connection conn = null;

        try {
            // 加载JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立连接
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("数据库链接成功");
        } catch (Exception e) {
            System.out.println("数据库链接失败");
        }
        return conn;
    }
}
