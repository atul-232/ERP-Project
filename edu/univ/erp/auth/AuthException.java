package edu.univ.erp.auth;

public class AuthException extends Exception {

    public AuthException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("[DEBUG] AuthException thrown -> message=" + message + ", cause=" + (cause != null ? cause.getMessage() : "null"));
    }
}
