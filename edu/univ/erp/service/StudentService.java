package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StudentService {

    private final CourseDAO courseDAO = new CourseDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    private final MaintenanceService maintenanceService = new MaintenanceService();

    public void registerForSection(int studentId, int sectionId) throws RegistrationException {
        System.out.println("[StudentService] registerForSection called -> studentId=" + studentId + ", sectionId=" + sectionId);

        if (maintenanceService.isMaintenanceModeOn()) {
            System.out.println("[StudentService] Registration blocked due to Maintenance Mode");
            throw new RegistrationException("Registration failed: System is in Maintenance Mode.");
        }

        Optional<Section> sectionOpt = sectionDAO.findById(sectionId);
        if (sectionOpt.isEmpty()) {
            System.out.println("[StudentService] Section not found");
            throw new RegistrationException("Section not found.");
        }
        Section section = sectionOpt.get();

        int enrolled = sectionDAO.getEnrolledCount(sectionId);
        if (enrolled >= section.getCapacity()) {
            System.out.println("[StudentService] Section is full");
            throw new RegistrationException("Registration failed: Section is full.");
        }

        if (enrollmentDAO.findByStudentAndSectionId(studentId, sectionId).isPresent()) {
            System.out.println("[StudentService] Duplicate enrollment detected");
            throw new RegistrationException("Registration failed: Already enrolled in this section.");
        }

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudentId(studentId);
        newEnrollment.setSectionId(sectionId);
        newEnrollment.setStatus("ENROLLED");
        enrollmentDAO.insert(newEnrollment);
        System.out.println("[StudentService] Registration successful");
    }

    public void dropSection(int studentId, int sectionId) throws RegistrationException {
        System.out.println("[StudentService] dropSection called -> studentId=" + studentId + ", sectionId=" + sectionId);

        if (maintenanceService.isMaintenanceModeOn()) {
            System.out.println("[StudentService] Drop blocked due to Maintenance Mode");
            throw new RegistrationException("Drop failed: System is in Maintenance Mode.");
        }

        Optional<Enrollment> enrollmentOpt = enrollmentDAO.findByStudentAndSectionId(studentId, sectionId);
        if (enrollmentOpt.isEmpty()) {
            System.out.println("[StudentService] Not enrolled in section");
            throw new RegistrationException("Cannot drop section: Not currently enrolled in this section.");
        }
        Enrollment enrollment = enrollmentOpt.get();

        if (isPastDropDeadline()) {
            System.out.println("[StudentService] Drop deadline passed");
            throw new RegistrationException("Cannot drop section: The drop deadline has passed.");
        }

        enrollmentDAO.updateStatus(enrollment.getEnrollmentId(), "DROPPED");
        System.out.println("[StudentService] Drop completed successfully");
    }

    public List<TimetableEntryDTO> getMyTimetable(int studentId) {
        System.out.println("[StudentService] getMyTimetable called -> studentId=" + studentId);

        List<TimetableEntryDTO> timetable = new ArrayList<>();
        List<Enrollment> enrollments = getMyEnrollments(studentId);

        if (enrollments.isEmpty()) {
            System.out.println("[StudentService] No active enrollments");
        }

        for (Enrollment enrollment : enrollments) {
            Optional<Section> sectionOpt = sectionDAO.findById(enrollment.getSectionId());
            if (sectionOpt.isEmpty()) continue;
            
            Section section = sectionOpt.get();
            Course course = courseDAO.findById(section.getCourseId()).orElse(new Course());
            Instructor instructor = instructorDAO.findByUserId(section.getInstructorId()).orElse(new Instructor());

            timetable.add(new TimetableEntryDTO(
                    section.getSectionId(),
                    course.getCourseCode(),
                    course.getTitle(),
                    section.getDayTime(),
                    section.getRoom(),
                    instructor.getFullName()
            ));
        }

        System.out.println("[StudentService] Timetable loaded -> entries=" + timetable.size());
        return timetable;
    }

    public Map<Course, String> getTranscriptData(int studentId) {
        System.out.println("[StudentService] getTranscriptData called -> studentId=" + studentId);

        List<Enrollment> enrollments = getMyEnrollments(studentId);
        if (enrollments.isEmpty()) {
            System.out.println("[StudentService] No enrollments, transcript empty");
            return Collections.emptyMap();
        }

        List<Integer> courseIds = new ArrayList<>();
        for (Enrollment e : enrollments) {
            sectionDAO.findById(e.getSectionId()).ifPresent(s -> courseIds.add(s.getCourseId()));
        }

        Map<Integer, Course> courseMap = courseDAO.findAll().stream()
                .filter(c -> courseIds.contains(c.getCourseId()))
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));

        Map<Course, String> transcript = enrollments.stream()
                .filter(e -> e.getFinalGrade() != null && !e.getFinalGrade().isEmpty())
                .filter(e -> sectionDAO.findById(e.getSectionId()).isPresent())
                .filter(e -> {
                    Section s = sectionDAO.findById(e.getSectionId()).get();
                    return courseMap.containsKey(s.getCourseId());
                })
                .collect(Collectors.toMap(
                        e -> courseMap.get(sectionDAO.findById(e.getSectionId()).get().getCourseId()),
                        Enrollment::getFinalGrade
                ));

        System.out.println("[StudentService] Transcript size = " + transcript.size());
        return transcript;
    }

    public List<Enrollment> getMyEnrollments(int studentId) {
        System.out.println("[StudentService] getMyEnrollments called -> studentId=" + studentId);

        try {
            List<Enrollment> result = enrollmentDAO.findByStudentId(studentId)
                    .stream()
                    .filter(e -> "ENROLLED".equalsIgnoreCase(e.getStatus()))
                    .toList();

            if (result.isEmpty()) {
                System.out.println("[StudentService] No active enrollments found");
            }
            return result;
        } catch (Exception e) {
            System.out.println("[StudentService] Error getting enrollments -> " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Grade> getGradesForEnrollment(int enrollmentId) {
        System.out.println("[StudentService] getGradesForEnrollment called -> enrollmentId=" + enrollmentId);
        try {
            return gradeDAO.findByEnrollmentId(enrollmentId);
        } catch (Exception e) {
            System.out.println("[StudentService] Error getting grades -> " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean isPastDropDeadline() {
        System.out.println("[StudentService] isPastDropDeadline check");
        return false;
    }
}
