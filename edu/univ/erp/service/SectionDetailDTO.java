package edu.univ.erp.service;

public class SectionDetailDTO {

    private final int sectionId;
    private final String courseCode;
    private final String courseTitle;
    private final int credits;
    private final String dayTime;
    private final String room;
    private final String instructorName;
    private final int capacity;
    private final int enrolledCount;

    public SectionDetailDTO(int sectionId, String courseCode, String courseTitle, int credits,
                            String dayTime, String room, String instructorName,
                            int capacity, int enrolledCount) {

        System.out.println("[SectionDetailDTO] Constructor called");

        this.sectionId = sectionId;

        if (courseCode == null) {
            System.out.println("[SectionDetailDTO] courseCode is null");
            this.courseCode = "";
        } else {
            this.courseCode = courseCode;
        }

        if (courseTitle == null) {
            System.out.println("[SectionDetailDTO] courseTitle is null");
            this.courseTitle = "";
        } else {
            this.courseTitle = courseTitle;
        }

        this.credits = credits;

        if (dayTime == null) {
            System.out.println("[SectionDetailDTO] dayTime is null");
            this.dayTime = "";
        } else {
            this.dayTime = dayTime;
        }

        if (room == null) {
            System.out.println("[SectionDetailDTO] room is null");
            this.room = "";
        } else {
            this.room = room;
        }

        if (instructorName == null) {
            System.out.println("[SectionDetailDTO] instructorName is null");
            this.instructorName = "TBA";
        } else {
            this.instructorName = instructorName;
        }

        this.capacity = capacity;
        this.enrolledCount = enrolledCount;

        System.out.println(
                "[SectionDetailDTO] Created -> sectionId=" + sectionId +
                ", course=" + this.courseCode +
                ", credits=" + credits +
                ", instructor=" + this.instructorName +
                ", enrolled=" + enrolledCount + "/" + capacity
        );
    }

    public int getSectionId() {
        System.out.println("[SectionDetailDTO] getSectionId called");
        return sectionId;
    }

    public String getCourseCode() {
        System.out.println("[SectionDetailDTO] getCourseCode called");
        return courseCode;
    }

    public String getCourseTitle() {
        System.out.println("[SectionDetailDTO] getCourseTitle called");
        return courseTitle;
    }

    public int getCredits() {
        System.out.println("[SectionDetailDTO] getCredits called");
        return credits;
    }

    public String getDayTime() {
        System.out.println("[SectionDetailDTO] getDayTime called");
        return dayTime;
    }

    public String getRoom() {
        System.out.println("[SectionDetailDTO] getRoom called");
        return room;
    }

    public String getInstructorName() {
        System.out.println("[SectionDetailDTO] getInstructorName called");
        return instructorName;
    }

    public int getCapacity() {
        System.out.println("[SectionDetailDTO] getCapacity called");
        return capacity;
    }

    public int getEnrolledCount() {
        System.out.println("[SectionDetailDTO] getEnrolledCount called");
        return enrolledCount;
    }
}
