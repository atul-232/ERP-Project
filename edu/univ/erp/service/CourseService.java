package edu.univ.erp.service;

import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.data.InstructorDAO;
import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Section;
import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();

    public List<SectionDetailDTO> getSectionDetailsForCatalog() {
        System.out.println("[CourseService] getSectionDetailsForCatalog called");
        List<SectionDetailDTO> sectionDetails = new ArrayList<>();
        List<Section> sections = sectionDAO.findAll();

        if (sections.isEmpty()) {
            System.out.println("[CourseService] No sections found.");
        }

        for (Section section : sections) {
            Course course = courseDAO.findById(section.getCourseId()).orElse(null);
            Instructor instructor = instructorDAO.findByUserId(section.getInstructorId()).orElse(null);
            int enrolledCount = sectionDAO.getEnrolledCount(section.getSectionId());

            if (course == null) {
                System.out.println("[CourseService] Course not found for sectionId=" + section.getSectionId());
                continue;
            }
            if (instructor == null) {
                System.out.println("[CourseService] Instructor not assigned for sectionId=" + section.getSectionId());
            }

            sectionDetails.add(new SectionDetailDTO(
                    section.getSectionId(),
                    course.getCourseCode(),
                    course.getTitle(),
                    course.getCredits(),
                    section.getDayTime(),
                    section.getRoom(),
                    instructor != null ? instructor.getFullName() : "TBA",
                    section.getCapacity(),
                    enrolledCount
            ));
        }
        System.out.println("[CourseService] Section details loaded -> count=" + sectionDetails.size());
        return sectionDetails;
    }

    public List<Course> getAllCourses() {
        System.out.println("[CourseService] getAllCourses called");
        List<Course> courses = courseDAO.findAll();
        if (courses.isEmpty()) {
            System.out.println("[CourseService] No courses found.");
        }
        return courses;
    }

    public Course getCourseById(int courseId) {
        System.out.println("[CourseService] getCourseById called -> ID=" + courseId);
        return courseDAO.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + courseId + " not found."));
    }

    public List<Section> getAllSections() {
        System.out.println("[CourseService] getAllSections called");
        List<Section> sections = sectionDAO.findAll();
        if (sections.isEmpty()) {
            System.out.println("[CourseService] No sections found.");
        }
        return sections;
    }
}
