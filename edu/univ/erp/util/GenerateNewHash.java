package edu.univ.erp.util;

import com.password4j.Password;

public class GenerateNewHash {

    public static void main(String[] args) {
        System.out.println("[GenerateNewHash] Starting password hashing process...");

        String passwordToHash = "password";
        System.out.println("[GenerateNewHash] Raw password → " + passwordToHash);

        String newHash = Password.hash(passwordToHash).withBcrypt().getResult();
        System.out.println("[GenerateNewHash] Hash generated successfully.");

        System.out.println("\n--- Your New Password Hash ---");
        System.out.println("Password: '" + passwordToHash + "'");
        System.out.println("Hash: " + newHash);
        System.out.println("\n[ACTION REQUIRED] Copy the hash above and paste it into your users_auth table.");
        
        System.out.println("[GenerateNewHash] Process complete.");
    }
}
