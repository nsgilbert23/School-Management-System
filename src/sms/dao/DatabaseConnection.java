package sms.dao;
// Database connection configured for port 3307 - BelyseU and nsgilbert23
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3307/sms?useSSL=false&serverTimezone=UTC";    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        if (conn != null) {
            System.out.println("Database connected successfully!");
        }

        return conn;
    }

    // Login verification method
    public static boolean login(String email, String password) {

        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        boolean success = false;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                success = true;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return success;
    }
}