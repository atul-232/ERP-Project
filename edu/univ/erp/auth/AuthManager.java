package edu.univ.erp.auth;

import edu.univ.erp.auth.store.AuthDataStore;
import java.sql.SQLException;

public class AuthManager {

    private final AuthDataStore dataStore;
    private final PasswordHasher passwordHasher;

    public AuthManager() {
        this.dataStore = new AuthDataStore();
        this.passwordHasher = new PasswordHasher();
        System.out.println("[DEBUG] AuthManager initialized");
    }

    public Session login(String username, String password) throws AuthException {
        System.out.println("[DEBUG] login called -> username=" + username);
        try {
            AuthUser authUser = dataStore.findUserByUsername(username);
            System.out.println("[DEBUG] login -> findUserByUsername result=" + (authUser != null));

            if (authUser != null) {
                boolean passwordMatch = passwordHasher.checkPassword(password, authUser.getPasswordHash());
                System.out.println("[DEBUG] login -> passwordMatch=" + passwordMatch);

                if (passwordMatch) {
                    dataStore.updateLastLogin(authUser.getUserId());
                    int profileId = fetchProfileId(authUser.getUserId(), authUser.getRole());
                    Session session = new Session(authUser.getUserId(), authUser.getUsername(), authUser.getRole(), profileId);
                    System.out.println("[DEBUG] login SUCCESS -> userId=" + authUser.getUserId() + ", role=" + authUser.getRole() + ", profileId=" + profileId);
                    return session;
                }
            }

            System.out.println("[DEBUG] login FAILED -> invalid credentials");
            return null;

        } catch (SQLException e) {
            System.out.println("[DEBUG] login ERROR -> " + e.getMessage());
            throw new AuthException("Failed to process login due to a database error.", e);
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws AuthException {
        System.out.println("[DEBUG] changePassword called -> username=" + username);
        try {
            AuthUser user = dataStore.findUserByUsername(username);
            System.out.println("[DEBUG] changePassword -> findUserByUsername result=" + (user != null));

            if (user == null) {
                System.out.println("[DEBUG] changePassword FAILED -> user not found");
                throw new AuthException("User not found.", null);
            }

            boolean oldPasswordMatch = passwordHasher.checkPassword(oldPassword, user.getPasswordHash());
            System.out.println("[DEBUG] changePassword -> oldPasswordMatch=" + oldPasswordMatch);
            if (!oldPasswordMatch) {
                System.out.println("[DEBUG] changePassword FAILED -> incorrect current password");
                throw new IllegalArgumentException("Incorrect current password.");
            }

            String newHash = passwordHasher.hashPassword(newPassword);
            System.out.println("[DEBUG] changePassword -> new password hashed");
            dataStore.updatePassword(user.getUserId(), newHash);
            System.out.println("[DEBUG] changePassword SUCCESS -> userId=" + user.getUserId());

        } catch (SQLException e) {
            System.out.println("[DEBUG] changePassword ERROR -> " + e.getMessage());
            throw new AuthException("Database error while changing password.", e);
        }
    }

    private int fetchProfileId(int userId, String role) {
        System.out.println("[DEBUG] fetchProfileId called -> userId=" + userId + ", role=" + role);
        try {
            if ("STUDENT".equalsIgnoreCase(role)) {
                edu.univ.erp.data.StudentDAO studentDAO = new edu.univ.erp.data.StudentDAO();
                int profileId = studentDAO.findByUserId(userId).map(s -> s.getStudentId()).orElse(0);
                System.out.println("[DEBUG] fetchProfileId STUDENT -> profileId=" + profileId);
                return profileId;
            } else if ("INSTRUCTOR".equalsIgnoreCase(role)) {
                edu.univ.erp.data.InstructorDAO instructorDAO = new edu.univ.erp.data.InstructorDAO();
                int profileId = instructorDAO.findByUserId(userId).map(i -> i.getInstructorId()).orElse(0);
                System.out.println("[DEBUG] fetchProfileId INSTRUCTOR -> profileId=" + profileId);
                return profileId;
            } else {
                System.out.println("[DEBUG] fetchProfileId ADMIN -> profileId=0");
                return 0;
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] fetchProfileId ERROR -> " + e.getMessage());
            return 0;
        }
    }
}
