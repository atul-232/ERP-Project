package edu.univ.erp.domain;

public class Instructor {

    private int instructorId;
    private int userId;
    private String department;
    private String fullName;

    public Instructor() {
        System.out.println("[DEBUG] Instructor() default constructor called");
    }

    public Instructor(int instructorId, int userId, String department, String fullName) {
        System.out.println("[DEBUG] Instructor(full) constructor called -> instructorId=" + instructorId + ", userId=" + userId + ", department=" + department + ", fullName=" + fullName);
        this.instructorId = instructorId;
        this.userId = userId;
        this.department = department;
        this.fullName = fullName;
    }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) {
        System.out.println("[DEBUG] setInstructorId -> " + instructorId);
        this.instructorId = instructorId;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) {
        System.out.println("[DEBUG] setUserId -> " + userId);
        this.userId = userId;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        System.out.println("[DEBUG] setDepartment -> " + department);
        this.department = department;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        System.out.println("[DEBUG] setFullName -> " + fullName);
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
