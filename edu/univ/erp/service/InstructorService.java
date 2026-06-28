package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class InstructorService {

    private final SectionDAO sectionDAO = new SectionDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    
    private final MaintenanceService maintenanceService = new MaintenanceService();

    public GradebookDTO getGradebookData(int sectionId) {
        System.out.println("[InstructorService] getGradebookData called -> sectionId = " + sectionId);
        List<Enrollment> enrollments = enrollmentDAO.findBySectionId(sectionId);

        if (enrollments.isEmpty()) {
            System.out.println("[InstructorService] No enrollments found.");
        }

        List<StudentGradeDTO> studentGrades = new ArrayList<>();
        List<String> components = new ArrayList<>(List.of("Assignment 1", "Assignment 2", "Midterm", "Final Exam"));

        for (Enrollment enrollment : enrollments) {
            Student student = studentDAO.findById(enrollment.getStudentId()).orElse(null);

            if (student == null) {
                System.out.println("[InstructorService] Student not found for enrollmentId = " + enrollment.getEnrollmentId());
                continue;
            }

            List<Grade> grades = gradeDAO.findByEnrollmentId(enrollment.getEnrollmentId());
            Map<String, Double> scoreMap = new HashMap<>();

            if (!grades.isEmpty()) {
                scoreMap = grades.stream()
                        .collect(Collectors.toMap(Grade::getComponent, Grade::getScore));
            } else {
                System.out.println("[InstructorService] No grades found for enrollmentId = " + enrollment.getEnrollmentId());
            }

            studentGrades.add(new StudentGradeDTO(student, enrollment.getEnrollmentId(), scoreMap, enrollment.getFinalGrade()));
        }

        System.out.println("[InstructorService] Gradebook loaded -> studentCount = " + studentGrades.size());
        return new GradebookDTO(components, studentGrades);
    }

    public SectionStatsDTO getSectionStatistics(int sectionId) {
        System.out.println("[InstructorService] getSectionStatistics called -> sectionId = " + sectionId);
        List<Enrollment> enrollments = enrollmentDAO.findBySectionId(sectionId);

        if (enrollments.isEmpty()) {
            System.out.println("[InstructorService] No enrollments found.");
            return new SectionStatsDTO(0, Collections.emptyMap());
        }

        List<Grade> allGrades = enrollments.stream()
                .flatMap(e -> gradeDAO.findByEnrollmentId(e.getEnrollmentId()).stream())
                .toList();

        if (allGrades.isEmpty()) {
            System.out.println("[InstructorService] No grades found in this section.");
        }

        Map<String, Double> averageScores = allGrades.stream()
                .collect(Collectors.groupingBy(Grade::getComponent, Collectors.averagingDouble(Grade::getScore)));

        System.out.println("[InstructorService] Section statistics calculated");
        return new SectionStatsDTO(enrollments.size(), averageScores);
    }

    public List<SectionInfoDTO> getMySections(int instructorUserId) {
        System.out.println("[InstructorService] getMySections called -> instructorUserId = " + instructorUserId);
        List<Section> sections = sectionDAO.findByInstructorId(instructorUserId);

        if (sections.isEmpty()) {
            System.out.println("[InstructorService] No assigned sections.");
        }

        List<SectionInfoDTO> sectionInfoList = new ArrayList<>();

        for (Section section : sections) {
            Course course = courseDAO.findById(section.getCourseId()).orElse(null);

            if (course == null) {
                System.out.println("[InstructorService] Course not found for sectionId = " + section.getSectionId());
                continue;
            }

            String displayName = String.format("%s - %s (%s %d)",
                    course.getCourseCode(),
                    course.getTitle(),
                    section.getSemester(),
                    section.getYear());

            sectionInfoList.add(new SectionInfoDTO(section, displayName));
        }

        System.out.println("[InstructorService] Loaded assigned sections -> count = " + sectionInfoList.size());
        return sectionInfoList;
    }

    public void submitGrade(int enrollmentId, String component, double score) {
        System.out.println("[InstructorService] submitGrade called -> enrId=" + enrollmentId + ", comp=" + component + ", score=" + score);

        if (maintenanceService.isMaintenanceModeOn()) {
            System.out.println("[InstructorService] Maintenance Mode ON. Grade submission blocked.");
            throw new RuntimeException("Grading failed: System is in Maintenance Mode.");
        }

        if (score < 0 || score > 100) {
            System.out.println("[InstructorService] Invalid score!");
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }

        Grade grade = new Grade();
        grade.setEnrollmentId(enrollmentId);
        grade.setComponent(component);
        grade.setScore(score);
        gradeDAO.save(grade);

        System.out.println("[InstructorService] Grade successfully submitted!");
    }

    public void calculateFinalGradesForSection(int sectionId) {
        System.out.println("[InstructorService] calculateFinalGradesForSection called -> sectionId = " + sectionId);

        if (maintenanceService.isMaintenanceModeOn()) {
            System.out.println("[InstructorService] Maintenance Mode ON. Final grade calculation blocked.");
            throw new RuntimeException("Calculation failed: System is in Maintenance Mode.");
        }

        List<Enrollment> enrollments = enrollmentDAO.findBySectionId(sectionId);

        if (enrollments.isEmpty()) {
            System.out.println("[InstructorService] No enrollments found. Final grade calculation skipped.");
            return;
        }

        for (Enrollment enrollment : enrollments) {
            List<Grade> grades = gradeDAO.findByEnrollmentId(enrollment.getEnrollmentId());
            double finalScore = calculateWeightedScore(grades);
            String letterGrade = convertScoreToLetterGrade(finalScore);

            enrollmentDAO.updateFinalGrade(enrollment.getEnrollmentId(), letterGrade);
        }

        System.out.println("[InstructorService] Final grades successfully computed!");
    }

    private double calculateWeightedScore(List<Grade> grades) {
        Map<String, Double> scores = grades.stream()
                .collect(Collectors.toMap(Grade::getComponent, Grade::getScore, (a, b) -> b));

        double a1 = scores.getOrDefault("Assignment 1", 0.0);
        double a2 = scores.getOrDefault("Assignment 2", 0.0);
        double mid = scores.getOrDefault("Midterm", 0.0);
        double fin = scores.getOrDefault("Final Exam", 0.0);

        return (a1 * 0.10) + (a2 * 0.20) + (mid * 0.30) + (fin * 0.40);
    }

    private String convertScoreToLetterGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        return "F";
    }
}
