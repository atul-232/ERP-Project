package edu.univ.erp.api;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
        System.out.println("[DEBUG] ApiException thrown -> message=" + message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("[DEBUG] ApiException thrown -> message=" + message + ", cause=" + (cause != null ? cause.getMessage() : "null"));
    }
}
