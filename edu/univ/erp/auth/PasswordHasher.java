package edu.univ.erp.auth;

import com.password4j.Password;

public class PasswordHasher {

    public String hashPassword(String plainTextPassword) {
        System.out.println("[DEBUG] hashPassword called");
        String hash = Password.hash(plainTextPassword).withBcrypt().getResult();
        System.out.println("[DEBUG] hashPassword created hash");
        return hash;
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        System.out.println("[DEBUG] checkPassword called -> plainNull=" + (plainTextPassword == null) + ", hashNull=" + (hashedPassword == null));

        if (plainTextPassword == null || hashedPassword == null) {
            System.out.println("[DEBUG] checkPassword -> false (null input)");
            return false;
        }

        boolean match = Password.check(plainTextPassword, hashedPassword).withBcrypt();
        System.out.println("[DEBUG] checkPassword -> match=" + match);
        return match;
    }
}
