package com.yotam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/HealthManagement?serverTimezone=UTC";
    private static final String USER = "your_mysql_username";
    private static final String PASSWORD = "your_mysql_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加載 MySQL 驅動
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("無法載入 MySQL 驅動程式", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}