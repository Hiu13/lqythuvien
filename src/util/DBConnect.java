
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích dùng để kết nối cơ sở dữ liệu MySQL.
 */
public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3307/quanlythuvien";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // đổi nếu bạn có mật khẩu

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
