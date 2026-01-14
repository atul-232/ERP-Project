package edu.univ.erp.domain;

public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private int sectionId;
    private String status;
    private String finalGrade;

    public Enrollment() {
        System.out.println("[DEBUG] Enrollment() default constructor called");
    }

    public Enrollment(int enrollmentId, int studentId, int sectionId, String status, String finalGrade) {
        System.out.println("[DEBUG] Enrollment(full) constructor called -> id=" + enrollmentId + ", studentId=" + studentId + ", sectionId=" + sectionId);
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) {
        System.out.println("[DEBUG] setEnrollmentId -> " + enrollmentId);
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) {
        System.out.println("[DEBUG] setStudentId -> " + studentId);
        this.studentId = studentId;
    }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) {
        System.out.println("[DEBUG] setSectionId -> " + sectionId);
        this.sectionId = sectionId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        System.out.println("[DEBUG] setStatus -> " + status);
        this.status = status;
    }

    public String getFinalGrade() { return finalGrade; }
    public void setFinalGrade(String finalGrade) {
        System.out.println("[DEBUG] setFinalGrade -> " + finalGrade);
        this.finalGrade = finalGrade;
    }
}
