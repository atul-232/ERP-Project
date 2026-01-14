package edu.univ.erp.service;

public class RegistrationException extends Exception {

    public RegistrationException(String message) {
        super(message);
        System.out.println("[RegistrationException] " + message);
    }
}
