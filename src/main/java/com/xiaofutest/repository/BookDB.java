package com.xiaofutest.repository;

import com.xiaofutest.model.LogisticsDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDB {


    public static List<LogisticsDTO> query(){
        String sql = "SELECT * FROM test_db.book";
        Connection connection = RequestDB.getConnection();

        List<LogisticsDTO>  bookList  = new ArrayList<LogisticsDTO>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                bookList.add(new LogisticsDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("year"),
                        resultSet.getString("author"),
                        resultSet.getString("protagonist")
                        ));
            }
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bookList;
    }

    public static  Boolean add (LogisticsDTO logisticsDTO){
        String sql = "INSERT INTO `test_db`.`book` (`title`, `year`, `author`, `protagonist`) VALUES (?,?,?,?)";
        Boolean b = null;
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, logisticsDTO.getTitle());
            preparedStatement.setString(2, logisticsDTO.getYear());
            preparedStatement.setString(3, logisticsDTO.getAuthor());
            preparedStatement.setString(4, logisticsDTO.getProtagonist());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("成功插入 " + rowsAffected + " 行数据。");
            b = true;
        } catch (SQLException e) {
            System.out.println("插入数据失败！" + e.getMessage());
        }

        return  b;
    }

    public static  Boolean edit (LogisticsDTO logisticsDTO){
        String sql = "UPDATE `test_db`.`book` SET  `title` = ? , `year` =  ?, `author` = ?, `protagonist` = ?   WHERE id = ? ";
        Boolean b = null;
        Connection connection = RequestDB.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, logisticsDTO.getTitle());
            preparedStatement.setString(2, logisticsDTO.getYear());
            preparedStatement.setString(3, logisticsDTO.getAuthor());
            preparedStatement.setString(4, logisticsDTO.getProtagonist());
            preparedStatement.setInt(5, logisticsDTO.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("成功插入 " + rowsAffected + " 行数据。");
            b = true;
        } catch (SQLException e) {
            System.out.println("插入数据失败！" + e.getMessage());
        }

        return  b;
    }
}
