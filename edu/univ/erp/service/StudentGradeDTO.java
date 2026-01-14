package edu.univ.erp.service;

import edu.univ.erp.domain.Student;
import java.util.Map;

public class StudentGradeDTO {
    private final Student student;
    private final int enrollmentId;
    private final Map<String, Double> scores;

    public StudentGradeDTO(Student student, int enrollmentId, Map<String, Double> scores) {
        System.out.println("[StudentGradeDTO] Constructor called");

        if (student == null) {
            System.out.println("[StudentGradeDTO] student is null");
        }
        this.student = student;

        this.enrollmentId = enrollmentId;

        if (scores == null || scores.isEmpty()) {
            System.out.println("[StudentGradeDTO] scores is null or empty");
            this.scores = Map.of();
        } else {
            this.scores = scores;
        }

        System.out.println("[StudentGradeDTO] Created -> enrollmentId="
                + this.enrollmentId + ", scoreCount=" + this.scores.size());
    }

    public Student getStudent() {
        System.out.println("[StudentGradeDTO] getStudent called");
        return student;
    }

    public int getEnrollmentId() {
        System.out.println("[StudentGradeDTO] getEnrollmentId called");
        return enrollmentId;
    }

    public Map<String, Double> getScores() {
        System.out.println("[StudentGradeDTO] getScores called");
        return scores;
    }
}
