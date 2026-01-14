package edu.univ.erp.data;

public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("[DEBUG] DataAccessException thrown -> message=" + message + ", cause=" + (cause != null ? cause.getMessage() : "null"));
    }
}
