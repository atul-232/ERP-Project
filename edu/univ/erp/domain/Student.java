package edu.univ.erp.domain;
public class Student {

    private int studentId; 
    private int userId;
    private String fullName;
    private String rollNo;
    private String program;
    private int year;

    public Student() {
        System.out.println("[DEBUG] Student() default constructor called");
    }

    public Student(int studentId, int userId, String fullName, String rollNo, String program, int year) {
        System.out.println("[DEBUG] Student(full) constructor called -> studentId=" + studentId + 
                ", userId=" + userId + ", fullName=" + fullName + ", rollNo=" + rollNo + 
                ", program=" + program + ", year=" + year);
        this.studentId = studentId;
        this.userId = userId;
        this.fullName = fullName;
        this.rollNo = rollNo;
        this.program = program;
        this.year = year;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        System.out.println("[DEBUG] setStudentId -> " + studentId);
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        System.out.println("[DEBUG] setUserId -> " + userId);
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        System.out.println("[DEBUG] setFullName -> " + fullName);
        this.fullName = fullName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        System.out.println("[DEBUG] setRollNo -> " + rollNo);
        this.rollNo = rollNo;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        System.out.println("[DEBUG] setProgram -> " + program);
        this.program = program;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        System.out.println("[DEBUG] setYear -> " + year);
        this.year = year;
    }

    @Override
    public String toString() {
        return fullName + " (" + rollNo + ")";
    }
}
