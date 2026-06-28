package edu.univ.erp.service;

import java.io.Serializable;

public class StudentGradeDetailDTO implements Serializable {
    private final int enrollmentId;
    private final String courseCode;
    private final String courseTitle;
    private final String finalGrade;
    private final int credits;

    public StudentGradeDetailDTO(int enrollmentId, String courseCode, String courseTitle, String finalGrade, int credits) {
        this.enrollmentId = enrollmentId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.finalGrade = finalGrade;
        this.credits = credits;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public String getFinalGrade() { return finalGrade; }
    public int getCredits() { return credits; }
}
