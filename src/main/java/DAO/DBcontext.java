/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mac
 */
public class DBcontext {
    private Connection conn = null;
        public Connection getConnection() throws Exception {
        // Chuỗi kết nối JDBC cho MySQL
        String url = "jdbc:mysql://localhost:3306/learning_management" // dien ten DB to connect
                   + "?useSSL=false"
                   + "&serverTimezone=UTC"
                   + "&zeroDateTimeBehavior=CONVERT_TO_NULL";

        // Tài khoản đăng nhập MySQL
        String user = "root";
        String password = "Levandat2004^";

        // Load MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Trả về đối tượng kết nối
        return DriverManager.getConnection(url, user, password);
    }
    // Close the connection (optional, for manual cleanup if needed)
    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    
}
