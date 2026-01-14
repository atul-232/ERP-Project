package edu.univ.erp.service;

public class TimetableEntryDTO {
    private final int sectionId;
    private final String courseCode;
    private final String courseTitle;
    private final String dayTime;
    private final String room;
    private final String instructorName;

    public TimetableEntryDTO(int sectionId, String courseCode, String courseTitle,
                             String dayTime, String room, String instructorName) {
        System.out.println("[TimetableEntryDTO] Constructor called");

        this.sectionId = sectionId;

        if (courseCode == null) {
            System.out.println("[TimetableEntryDTO] courseCode is null");
            this.courseCode = "";
        } else {
            this.courseCode = courseCode;
        }

        if (courseTitle == null) {
            System.out.println("[TimetableEntryDTO] courseTitle is null");
            this.courseTitle = "";
        } else {
            this.courseTitle = courseTitle;
        }

        if (dayTime == null) {
            System.out.println("[TimetableEntryDTO] dayTime is null");
            this.dayTime = "";
        } else {
            this.dayTime = dayTime;
        }

        if (room == null) {
            System.out.println("[TimetableEntryDTO] room is null");
            this.room = "";
        } else {
            this.room = room;
        }

        if (instructorName == null) {
            System.out.println("[TimetableEntryDTO] instructorName is null");
            this.instructorName = "TBA";
        } else {
            this.instructorName = instructorName;
        }

        System.out.println("[TimetableEntryDTO] Created -> sectionId=" + sectionId +
                ", course=" + this.courseCode + ", instructor=" + this.instructorName);
    }

    public int getSectionId() {
        System.out.println("[TimetableEntryDTO] getSectionId called");
        return sectionId;
    }

    public String getCourseCode() {
        System.out.println("[TimetableEntryDTO] getCourseCode called");
        return courseCode;
    }

    public String getCourseTitle() {
        System.out.println("[TimetableEntryDTO] getCourseTitle called");
        return courseTitle;
    }

    public String getDayTime() {
        System.out.println("[TimetableEntryDTO] getDayTime called");
        return dayTime;
    }

    public String getRoom() {
        System.out.println("[TimetableEntryDTO] getRoom called");
        return room;
    }

    public String getInstructorName() {
        System.out.println("[TimetableEntryDTO] getInstructorName called");
        return instructorName;
    }
}
