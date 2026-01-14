package edu.univ.erp.domain;

public class Grade {
    private int gradeId;
    private int enrollmentId;
    private String component;
    private double score;

    public Grade() {
        System.out.println("[DEBUG] Grade() default constructor called");
    }

    public Grade(int gradeId, int enrollmentId, String component, double score) {
        System.out.println("[DEBUG] Grade(full) constructor called -> gradeId=" + gradeId + ", enrollmentId=" + enrollmentId + ", component=" + component + ", score=" + score);
        this.gradeId = gradeId;
        this.enrollmentId = enrollmentId;
        this.component = component;
        this.score = score;
    }

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) {
        System.out.println("[DEBUG] setGradeId -> " + gradeId);
        this.gradeId = gradeId;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) {
        System.out.println("[DEBUG] setEnrollmentId -> " + enrollmentId);
        this.enrollmentId = enrollmentId;
    }

    public String getComponent() { return component; }
    public void setComponent(String component) {
        System.out.println("[DEBUG] setComponent -> " + component);
        this.component = component;
    }

    public double getScore() { return score; }
    public void setScore(double score) {
        System.out.println("[DEBUG] setScore -> " + score);
        this.score = score;
    }
}
