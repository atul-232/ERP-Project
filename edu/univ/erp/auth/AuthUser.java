package edu.univ.erp.auth;

import java.sql.Timestamp;

public class AuthUser {
    private final int userId;
    private final String username;
    private final String role;
    private final String passwordHash;
    private final String status;
    private final Timestamp lastLogin;

    public AuthUser(int userId, String username, String role, String passwordHash, String status, Timestamp lastLogin) {
        System.out.println("[DEBUG] AuthUser constructor called -> userId=" + userId + ", username=" + username + ", role=" + role + ", status=" + status + ", lastLogin=" + lastLogin);
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
        this.status = status;
        this.lastLogin = lastLogin;
        System.out.println("[DEBUG] AuthUser created");
    }

    public int getUserId() {
        System.out.println("[DEBUG] getUserId -> " + userId);
        return userId;
    }

    public String getUsername() {
        System.out.println("[DEBUG] getUsername -> " + username);
        return username;
    }

    public String getRole() {
        System.out.println("[DEBUG] getRole -> " + role);
        return role;
    }

    public String getPasswordHash() {
        System.out.println("[DEBUG] getPasswordHash -> " + passwordHash);
        return passwordHash;
    }

    public String getStatus() {
        System.out.println("[DEBUG] getStatus -> " + status);
        return status;
    }

    public Timestamp getLastLogin() {
        System.out.println("[DEBUG] getLastLogin -> " + lastLogin);
        return lastLogin;
    }
}
