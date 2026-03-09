package sms.dao;

import java.sql.*;
import sms.dao.DatabaseConnection;

public class UserDao {

    public boolean login(String username, String password) {

        boolean success = false;

        try {

            Connection con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                success = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
}