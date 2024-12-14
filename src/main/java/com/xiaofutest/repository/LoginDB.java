package com.xiaofutest.repository;

import com.xiaofutest.model.LoginDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author :xs
 * @date : 2024/11/29
 */
public class LoginDB {
    public static Boolean login(LoginDTO loginDTO){
        String sql = "SELECT * FROM `test_db`.`user` WHERE username=? AND password=?";
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginDTO.getUsername());
            preparedStatement.setString(2, loginDTO.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LoginDTO selectByUsernameAndPwd(String userName, String passWord){
        String sql = "SELECT * FROM `test_db`.`user` WHERE username=? AND password=?";
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2, passWord);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) { // 移动到结果集的下一条记录
                return new LoginDTO((long) rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("token"));
            }else {
                return  null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LoginDTO getById(String id){
        String sql = "SELECT * FROM `test_db`.`user` WHERE id=?";
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) { // 移动到结果集的下一条记录
                return new LoginDTO((long) rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("token"));
            }else {
                return  null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean query(LoginDTO loginDTO){
        String sql = "SELECT * FROM `test_db`.`user` WHERE username=?";
        Boolean b = false;
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginDTO.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    private boolean registerUser(LoginDTO loginDTO) throws SQLException {
        String sql = "INSERT INTO `test_db`.`user` (username, password) VALUES (?, ?)";
        Connection connection = RequestDB.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, loginDTO.getUsername());
        stmt.setString(2, loginDTO.getPassword());
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        return rowsAffected > 0;
    }


}
