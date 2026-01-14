package edu.univ.erp.api.types;

public class CourseRow {
    private final String courseCode;
    private final String title;
    private final int credits;
    private final int capacity;
    private final String instructorName;

    public CourseRow(String courseCode, String title, int credits, int capacity, String instructorName) {
        System.out.println("[DEBUG] CourseRow constructor called -> courseCode=" + courseCode + ", title=" + title + ", credits=" + credits + ", capacity=" + capacity + ", instructorName=" + instructorName);
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.capacity = capacity;
        this.instructorName = instructorName;
        System.out.println("[DEBUG] CourseRow created");
    }

    public String getCourseCode() {
        System.out.println("[DEBUG] getCourseCode -> " + courseCode);
        return courseCode;
    }

    public String getTitle() {
        System.out.println("[DEBUG] getTitle -> " + title);
        return title;
    }

    public int getCredits() {
        System.out.println("[DEBUG] getCredits -> " + credits);
        return credits;
    }

    public int getCapacity() {
        System.out.println("[DEBUG] getCapacity -> " + capacity);
        return capacity;
    }

    public String getInstructorName() {
        System.out.println("[DEBUG] getInstructorName -> " + instructorName);
        return instructorName;
    }

    @Override
    public String toString() {
        String formatted = String.format("%s - %s (%d credits) - %s", courseCode, title, credits, instructorName);
        System.out.println("[DEBUG] toString -> " + formatted);
        return formatted;
    }
}
