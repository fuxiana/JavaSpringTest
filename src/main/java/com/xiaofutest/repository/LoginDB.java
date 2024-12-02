package com.xiaofutest.repository;

import com.xiaofutest.model.LoginDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author :xs
 * @date : 2024/11/29
 */
public class LoginDB {
    public static Boolean login(LoginDTO loginDTO){
        String sql = "INSERT INTO `test_db`.`user` WHERE username = ?, password = ?";
        Boolean b = null;
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginDTO.getUsername());
            preparedStatement.setString(2, loginDTO.getPassword());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("成功插入 " + rowsAffected + " 行数据。");
            b = true;
        } catch (SQLException e) {
            System.out.println("插入数据失败！" + e.getMessage());
        }
        return  b;
    }
}
