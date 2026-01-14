package edu.univ.erp.domain;

public class Course {

    private int courseId;
    private String courseCode;
    private String title;
    private int credits;
    private String department;

    public Course() {
        System.out.println("[DEBUG] Course() default constructor called");
    }

    public Course(int courseId, String courseCode, String title, int credits, String department) {
        System.out.println("[DEBUG] Course(full) constructor called -> id=" + courseId + ", code=" + courseCode);
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.department = department;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) {
        System.out.println("[DEBUG] setCourseId -> " + courseId);
        this.courseId = courseId;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) {
        System.out.println("[DEBUG] setCourseCode -> " + courseCode);
        this.courseCode = courseCode;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        System.out.println("[DEBUG] setTitle -> " + title);
        this.title = title;
    }

    public int getCredits() { return credits; }
    public void setCredits(int credits) {
        System.out.println("[DEBUG] setCredits -> " + credits);
        this.credits = credits;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        System.out.println("[DEBUG] setDepartment -> " + department);
        this.department = department;
    }

    @Override
    public String toString() {
        return title + " (" + courseCode + ")";
    }
}
