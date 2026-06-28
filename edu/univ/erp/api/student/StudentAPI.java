package edu.univ.erp.api.student;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Enrollment;
import edu.univ.erp.domain.Grade;
import edu.univ.erp.service.RegistrationException;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.service.TimetableEntryDTO;
import edu.univ.erp.api.ApiException;

import java.util.List;
import java.util.Map;

public class StudentAPI {

    private final StudentService studentService;

    public StudentAPI() {
        this.studentService = new StudentService();
        System.out.println("[DEBUG] StudentAPI initialized");
    }

    public String registerForSection(int studentId, int sectionId) {
        System.out.println("[DEBUG] registerForSection called -> studentId=" + studentId + ", sectionId=" + sectionId);
        try {
            studentService.registerForSection(studentId, sectionId);
            String msg = "Successfully registered for section " + sectionId;
            System.out.println("[DEBUG] registerForSection success -> " + msg);
            return msg;
        } catch (RegistrationException e) {
            System.out.println("[DEBUG] registerForSection FAILED (business) -> " + e.getMessage());
            throw new ApiException("Registration failed: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("[DEBUG] registerForSection ERROR -> " + e.getMessage());
            throw new ApiException("System error during registration: " + e.getMessage(), e);
        }
    }

    public String dropSection(int studentId, int sectionId) {
        System.out.println("[DEBUG] dropSection called -> studentId=" + studentId + ", sectionId=" + sectionId);
        try {
            studentService.dropSection(studentId, sectionId);
            String msg = "Successfully dropped section " + sectionId;
            System.out.println("[DEBUG] dropSection success -> " + msg);
            return msg;
        } catch (RegistrationException e) {
            System.out.println("[DEBUG] dropSection FAILED (business) -> " + e.getMessage());
            throw new ApiException("Drop failed: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("[DEBUG] dropSection ERROR -> " + e.getMessage());
            throw new ApiException("System error during drop: " + e.getMessage(), e);
        }
    }

    public List<TimetableEntryDTO> getMyTimetable(int studentId) {
        System.out.println("[DEBUG] getMyTimetable called -> studentId=" + studentId);
        try {
            List<TimetableEntryDTO> timetable = studentService.getMyTimetable(studentId);
            System.out.println("[DEBUG] getMyTimetable success -> count=" + timetable.size());
            return timetable;
        } catch (Exception e) {
            System.out.println("[DEBUG] getMyTimetable FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve timetable: " + e.getMessage(), e);
        }
    }

    public List<Enrollment> getMyGrades(int studentId) {
        System.out.println("[DEBUG] getMyGrades called -> studentId=" + studentId);
        try {
            List<Enrollment> enrollments = studentService.getMyEnrollments(studentId);
            System.out.println("[DEBUG] getMyGrades success -> enrollments=" + enrollments.size());
            return enrollments;
        } catch (Exception e) {
            System.out.println("[DEBUG] getMyGrades FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve grades: " + e.getMessage(), e);
        }
    }

    public List<edu.univ.erp.service.StudentGradeDetailDTO> getMyGradeDetails(int studentId) {
        System.out.println("[DEBUG] getMyGradeDetails called -> studentId=" + studentId);
        try {
            List<edu.univ.erp.service.StudentGradeDetailDTO> details = studentService.getMyGradeDetails(studentId);
            System.out.println("[DEBUG] getMyGradeDetails success -> details=" + details.size());
            return details;
        } catch (Exception e) {
            System.out.println("[DEBUG] getMyGradeDetails FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve grade details: " + e.getMessage(), e);
        }
    }

    public List<Grade> getGradesForEnrollment(int enrollmentId) {
        System.out.println("[DEBUG] getGradesForEnrollment called -> enrollmentId=" + enrollmentId);
        try {
            List<Grade> grades = studentService.getGradesForEnrollment(enrollmentId);
            System.out.println("[DEBUG] getGradesForEnrollment success -> grades=" + grades.size());
            return grades;
        } catch (Exception e) {
            System.out.println("[DEBUG] getGradesForEnrollment FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve grade details: " + e.getMessage(), e);
        }
    }

    public Map<Course, String> getTranscriptData(int studentId) {
        System.out.println("[DEBUG] getTranscriptData called -> studentId=" + studentId);
        try {
            Map<Course, String> transcript = studentService.getTranscriptData(studentId);
            System.out.println("[DEBUG] getTranscriptData success -> entries=" + transcript.size());
            return transcript;
        } catch (Exception e) {
            System.out.println("[DEBUG] getTranscriptData FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve transcript data: " + e.getMessage(), e);
        }
    }

    public edu.univ.erp.domain.Student getStudentProfile(int studentId) {
        System.out.println("[DEBUG] getStudentProfile called -> studentId=" + studentId);
        try {
            edu.univ.erp.domain.Student student = studentService.getStudentProfile(studentId);
            System.out.println("[DEBUG] getStudentProfile success -> student=" + (student != null ? student.getFullName() : "null"));
            return student;
        } catch (Exception e) {
            System.out.println("[DEBUG] getStudentProfile FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve student profile: " + e.getMessage(), e);
        }
    }
}
