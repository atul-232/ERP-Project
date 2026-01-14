package edu.univ.erp.auth;

public class Session {
    private final int userId;
    private final String username;
    private final String role;
    private final int profileId;

    public Session(int userId, String username, String role, int profileId) {
        System.out.println("[DEBUG] Session constructor called -> userId=" + userId + ", username=" + username + ", role=" + role + ", profileId=" + profileId);
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.profileId = profileId;
        System.out.println("[DEBUG] Session created");
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

    public int getProfileId() {
        System.out.println("[DEBUG] getProfileId -> " + profileId);
        return profileId;
    }
}
