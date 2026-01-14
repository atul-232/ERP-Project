package edu.univ.erp.domain;

public class Section {

    private int sectionId;
    private int courseId;
    private int instructorId;
    private String dayTime;
    private String room;
    private int capacity;
    private String semester;
    private int year;

    public Section() {
        System.out.println("[DEBUG] Section() default constructor called");
    }

    public Section(int sectionId, int courseId, int instructorId, String dayTime, String room, int capacity, String semester, int year) {
        System.out.println("[DEBUG] Section(full) constructor called -> sectionId=" + sectionId + ", courseId=" + courseId + ", instructorId=" + instructorId + ", semester=" + semester + ", year=" + year);
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.dayTime = dayTime;
        this.room = room;
        this.capacity = capacity;
        this.semester = semester;
        this.year = year;
    }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) {
        System.out.println("[DEBUG] setSectionId -> " + sectionId);
        this.sectionId = sectionId;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) {
        System.out.println("[DEBUG] setCourseId -> " + courseId);
        this.courseId = courseId;
    }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) {
        System.out.println("[DEBUG] setInstructorId -> " + instructorId);
        this.instructorId = instructorId;
    }

    public String getDayTime() { return dayTime; }
    public void setDayTime(String dayTime) {
        System.out.println("[DEBUG] setDayTime -> " + dayTime);
        this.dayTime = dayTime;
    }

    public String getRoom() { return room; }
    public void setRoom(String room) {
        System.out.println("[DEBUG] setRoom -> " + room);
        this.room = room;
    }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) {
        System.out.println("[DEBUG] setCapacity -> " + capacity);
        this.capacity = capacity;
    }

    public String getSemester() { return semester; }
    public void setSemester(String semester) {
        System.out.println("[DEBUG] setSemester -> " + semester);
        this.semester = semester;
    }

    public int getYear() { return year; }
    public void setYear(int year) {
        System.out.println("[DEBUG] setYear -> " + year);
        this.year = year;
    }
}
