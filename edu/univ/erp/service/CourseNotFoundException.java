package edu.univ.erp.service;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String message) {
        super(message);
        System.out.println("[CourseNotFoundException] " + message);
    }
}
