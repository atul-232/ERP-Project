package edu.univ.erp.auth.store;

import edu.univ.erp.auth.AuthUser;
import edu.univ.erp.data.DBConnection;
import java.sql.*;

public class AuthDataStore {

    public AuthUser findUserByUsername(String username) throws SQLException {
        System.out.println("[DEBUG] findUserByUsername called -> username=" + username);
        String sql = "SELECT user_id, username, role, password_hash, status, last_login FROM users_auth WHERE username = ?";

        try (Connection conn = DBConnection.getConnection("auth_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            System.out.println("[DEBUG] Executing query -> " + sql);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    AuthUser user = new AuthUser(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("password_hash"),
                            rs.getString("status"),
                            rs.getTimestamp("last_login")
                    );
                    System.out.println("[DEBUG] findUserByUsername success -> userId=" + user.getUserId());
                    return user;
                } else {
                    System.out.println("[DEBUG] findUserByUsername -> NO MATCH");
                }
            }
        }
        return null;
    }

    public void updateLastLogin(int userId) throws SQLException {
        System.out.println("[DEBUG] updateLastLogin called -> userId=" + userId);
        String sql = "UPDATE users_auth SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection("auth_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            System.out.println("[DEBUG] Executing update -> " + sql);
            int rows = pst.executeUpdate();
            System.out.println("[DEBUG] updateLastLogin affectedRows=" + rows);
        }
    }

    public int createUser(String username, String passwordHash, String role) throws SQLException {
        System.out.println("[DEBUG] createUser called -> username=" + username + ", role=" + role);
        String sql = "INSERT INTO users_auth (username, password_hash, role, status) VALUES (?, ?, ?, 'ACTIVE')";

        try (Connection conn = DBConnection.getConnection("auth_db");
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, username);
            pst.setString(2, passwordHash);
            pst.setString(3, role);
            System.out.println("[DEBUG] Executing insert -> " + sql);

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    System.out.println("[DEBUG] createUser success -> userId=" + userId);
                    return userId;
                } else {
                    System.out.println("[DEBUG] createUser FAILED -> no generated key");
                }
            }
        }

        throw new SQLException("Creating user failed, no ID obtained.");
    }

    public void deleteUser(int userId) throws SQLException {
        System.out.println("[DEBUG] deleteUser called -> userId=" + userId);
        String sql = "DELETE FROM users_auth WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection("auth_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            System.out.println("[DEBUG] Executing delete -> " + sql);
            int rows = pst.executeUpdate();
            System.out.println("[DEBUG] deleteUser affectedRows=" + rows);
        }
    }

    public void updatePassword(int userId, String newPasswordHash) throws SQLException {
        System.out.println("[DEBUG] updatePassword called -> userId=" + userId);
        String sql = "UPDATE users_auth SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection("auth_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, newPasswordHash);
            pst.setInt(2, userId);
            System.out.println("[DEBUG] Executing update -> " + sql);
            int rows = pst.executeUpdate();
            System.out.println("[DEBUG] updatePassword affectedRows=" + rows);
        }
    }
}
