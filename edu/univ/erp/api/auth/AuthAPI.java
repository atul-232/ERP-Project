package edu.univ.erp.api.auth;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.auth.AuthException;
import edu.univ.erp.auth.AuthManager;
import edu.univ.erp.auth.Session;

public class AuthAPI {

    private final AuthManager authManager;

    public AuthAPI() {
        this.authManager = new AuthManager();
        System.out.println("[DEBUG] AuthAPI initialized");
    }

    public Session login(String username, String password) throws AuthException {
        System.out.println("[DEBUG] login called -> username=" + username);

        if (username == null || username.trim().isEmpty()) {
            System.out.println("[DEBUG] login FAILED -> username empty");
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (password == null || password.isEmpty()) {
            System.out.println("[DEBUG] login FAILED -> password empty");
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Session session = authManager.login(username, password);
        System.out.println("[DEBUG] login returned session -> " + session);

        if (session == null) {
            System.out.println("[DEBUG] login FAILED -> invalid credentials");
            throw new IllegalArgumentException("Invalid username or password");
        } else {
            System.out.println("[DEBUG] login SUCCESS -> userId=" + session.getUserId() + ", role=" + session.getRole());
            return session;
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        System.out.println("[DEBUG] changePassword called -> username=" + username);

        if (newPassword == null || newPassword.length() < 6) {
            System.out.println("[DEBUG] changePassword FAILED -> weak password");
            throw new ApiException("New password must be at least 6 characters long.", null);
        }

        try {
            authManager.changePassword(username, oldPassword, newPassword);
            System.out.println("[DEBUG] changePassword SUCCESS");
        } catch (IllegalArgumentException e) {
            System.out.println("[DEBUG] changePassword FAILED -> " + e.getMessage());
            throw new ApiException(e.getMessage(), e);
        } catch (AuthException e) {
            System.out.println("[DEBUG] changePassword ERROR -> " + e.getMessage());
            throw new ApiException("Failed to change password: " + e.getMessage(), e);
        }
    }

    public boolean isSystemAvailable() {
        System.out.println("[DEBUG] isSystemAvailable called");
        boolean available = true;
        System.out.println("[DEBUG] isSystemAvailable result=" + available);
        return available;
    }
}
