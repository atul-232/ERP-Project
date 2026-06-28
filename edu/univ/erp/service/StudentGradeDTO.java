package edu.univ.erp.service;

import edu.univ.erp.domain.Student;
import java.util.Map;

public class StudentGradeDTO {
    private final Student student;
    private final int enrollmentId;
    private final Map<String, Double> scores;
    private final String finalGrade;

    public StudentGradeDTO(Student student, int enrollmentId, Map<String, Double> scores, String finalGrade) {
        System.out.println("[StudentGradeDTO] Constructor called");

        if (student == null) {
            System.out.println("[StudentGradeDTO] student is null");
        }
        this.student = student;
        this.enrollmentId = enrollmentId;
        this.finalGrade = finalGrade;

        if (scores == null || scores.isEmpty()) {
            System.out.println("[StudentGradeDTO] scores is null or empty");
            this.scores = Map.of();
        } else {
            this.scores = scores;
        }

        System.out.println("[StudentGradeDTO] Created -> enrollmentId="
                + this.enrollmentId + ", scoreCount=" + this.scores.size() + ", finalGrade=" + finalGrade);
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

    public String getFinalGrade() {
        System.out.println("[StudentGradeDTO] getFinalGrade called");
        return finalGrade;
    }
}
