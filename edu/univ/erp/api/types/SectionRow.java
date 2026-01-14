package edu.univ.erp.api.types;

public class SectionRow {
    private final int sectionId;
    private final String courseCode;
    private final String courseTitle;
    private final String dayTime;
    private final String room;
    private final int capacity;
    private final int enrolled;
    private final String instructorName;
    private final String semester;
    private final int year;

    public SectionRow(int sectionId, String courseCode, String courseTitle, String dayTime,
                      String room, int capacity, int enrolled, String instructorName,
                      String semester, int year) {
        System.out.println("[DEBUG] SectionRow constructor called -> sectionId=" + sectionId +
                ", courseCode=" + courseCode + ", courseTitle=" + courseTitle +
                ", dayTime=" + dayTime + ", room=" + room + ", capacity=" + capacity +
                ", enrolled=" + enrolled + ", instructorName=" + instructorName +
                ", semester=" + semester + ", year=" + year);

        this.sectionId = sectionId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.dayTime = dayTime;
        this.room = room;
        this.capacity = capacity;
        this.enrolled = enrolled;
        this.instructorName = instructorName;
        this.semester = semester;
        this.year = year;

        System.out.println("[DEBUG] SectionRow created");
    }

    public int getSectionId() {
        System.out.println("[DEBUG] getSectionId -> " + sectionId);
        return sectionId;
    }

    public String getCourseCode() {
        System.out.println("[DEBUG] getCourseCode -> " + courseCode);
        return courseCode;
    }

    public String getCourseTitle() {
        System.out.println("[DEBUG] getCourseTitle -> " + courseTitle);
        return courseTitle;
    }

    public String getDayTime() {
        System.out.println("[DEBUG] getDayTime -> " + dayTime);
        return dayTime;
    }

    public String getRoom() {
        System.out.println("[DEBUG] getRoom -> " + room);
        return room;
    }

    public int getCapacity() {
        System.out.println("[DEBUG] getCapacity -> " + capacity);
        return capacity;
    }

    public int getEnrolled() {
        System.out.println("[DEBUG] getEnrolled -> " + enrolled);
        return enrolled;
    }

    public String getInstructorName() {
        System.out.println("[DEBUG] getInstructorName -> " + instructorName);
        return instructorName;
    }

    public String getSemester() {
        System.out.println("[DEBUG] getSemester -> " + semester);
        return semester;
    }

    public int getYear() {
        System.out.println("[DEBUG] getYear -> " + year);
        return year;
    }

    public int getAvailableSeats() {
        int seats = capacity - enrolled;
        System.out.println("[DEBUG] getAvailableSeats -> " + seats);
        return seats;
    }

    public boolean isFull() {
        boolean full = enrolled >= capacity;
        System.out.println("[DEBUG] isFull -> " + full);
        return full;
    }

    @Override
    public String toString() {
        String formatted = String.format("%s - %s (%s %d) - %s - Seats: %d/%d",
                courseCode, courseTitle, semester, year, instructorName, enrolled, capacity);
        System.out.println("[DEBUG] toString -> " + formatted);
        return formatted;
    }
}
