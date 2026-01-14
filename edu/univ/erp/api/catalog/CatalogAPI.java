package edu.univ.erp.api.catalog;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.CourseService;
import edu.univ.erp.service.SectionDetailDTO;
import java.util.List;
import edu.univ.erp.api.ApiException;

public class CatalogAPI {

    private final CourseService courseService;

    public CatalogAPI() {
        this.courseService = new CourseService();
        System.out.println("[DEBUG] CatalogAPI initialized");
    }

    public List<Course> getAllCourses() {
        System.out.println("[DEBUG] getAllCourses called");
        try {
            List<Course> courses = courseService.getAllCourses();
            System.out.println("[DEBUG] getAllCourses success -> count=" + courses.size());
            return courses;
        } catch (Exception e) {
            System.out.println("[DEBUG] getAllCourses FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve courses: " + e.getMessage(), e);
        }
    }

    public Course getCourseById(int courseId) {
        System.out.println("[DEBUG] getCourseById called -> courseId=" + courseId);
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                System.out.println("[DEBUG] getCourseById result -> NULL");
            } else {
                System.out.println("[DEBUG] getCourseById success -> courseCode=" + course.getCourseCode());
            }
            return course;
        } catch (Exception e) {
            System.out.println("[DEBUG] getCourseById FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve course: " + e.getMessage(), e);
        }
    }

    public List<SectionDetailDTO> getSectionDetailsForCatalog() {
        System.out.println("[DEBUG] getSectionDetailsForCatalog called");
        try {
            List<SectionDetailDTO> sections = courseService.getSectionDetailsForCatalog();
            System.out.println("[DEBUG] getSectionDetailsForCatalog success -> count=" + sections.size());
            return sections;
        } catch (Exception e) {
            System.out.println("[DEBUG] getSectionDetailsForCatalog FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve section details: " + e.getMessage(), e);
        }
    }

    public List<Course> searchCourses(String query) {
        System.out.println("[DEBUG] searchCourses called -> query=" + query);
        try {
            List<Course> allCourses = courseService.getAllCourses();
            System.out.println("[DEBUG] searchCourses -> total courses=" + allCourses.size());

            List<Course> result = allCourses.stream()
                    .filter(course -> 
                        course.getCourseCode().toLowerCase().contains(query.toLowerCase()) ||
                        course.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .toList();

            System.out.println("[DEBUG] searchCourses result -> matched=" + result.size());
            return result;
        } catch (Exception e) {
            System.out.println("[DEBUG] searchCourses FAILED -> " + e.getMessage());
            throw new ApiException("Failed to search courses: " + e.getMessage(), e);
        }
    }
}
