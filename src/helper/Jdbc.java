/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author duann
 */
public class Jdbc {
    
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl = "jdbc:sqlserver://localhost:1433;databaseName=polypro1";
    private static String username = "sa";
    private static String password = "songlong";

//    nạp driver
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//     xây dựng PreparedStatement
//     sql là câ lệnh truy vấn sql nó có thể có hoặc không có tham số, nó có thể là 1 thủ tục lưu trữ
    public static PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        Connection connection = DriverManager.getConnection(dburl, username, password);
        PreparedStatement statment = null;
        if (sql.trim().startsWith("{")) {
            statment = connection.prepareCall(sql);
        } else {
            statment = connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            statment.setObject(i + 1, args[i]);
        }
        return statment;
    }

//    thực hiện các thao tác sql insert, update, delete hoặc thủ tục truy vấn dữ liệu
//    sql là câu lệnh sql có thể chứa tham số. nó có thể là một lời gọi thủ tục lưu trữ
//    args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql
    public static void executeUpdate(String sql, Object... args) {
        try {
            PreparedStatement statement = preparedStatement(sql, args);
            try {
                statement.executeUpdate();
            } finally {
                statement.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    các câu lệnh select hoặc thủ tục truy vấn dữ liệu
//    sql là câu lệnh sql có thể chứa các tham số, nó có thể là một lời gọi thủ tục lưu trữ
//    args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql
    public static ResultSet executeQuery(String sql, Object... args) {
        try {
            PreparedStatement statement = preparedStatement(sql, args);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
