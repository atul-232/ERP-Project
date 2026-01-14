package edu.univ.erp.api.admin;

import edu.univ.erp.api.ApiException;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Student;
import edu.univ.erp.service.AdminService;
import java.util.List;

public class AdminAPI {

    private final AdminService adminService;

    public AdminAPI() {
        this.adminService = new AdminService();
        System.out.println("[DEBUG] AdminAPI initialized");
    }

    public String addStudentUser(String username, String password, Student studentProfile) {
        System.out.println("[DEBUG] addStudentUser called -> username=" + username + ", profile=" + studentProfile);
        try {
            adminService.addStudentUser(username, password, studentProfile);
            String msg = "Student user created successfully";
            System.out.println("[DEBUG] addStudentUser success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] addStudentUser FAILED -> " + e.getMessage());
            throw new ApiException("Failed to create student user: " + e.getMessage(), e);
        }
    }

    public String addInstructorUser(String username, String password, Instructor instructorProfile) {
        System.out.println("[DEBUG] addInstructorUser called -> username=" + username + ", profile=" + instructorProfile);
        try {
            adminService.addInstructorUser(username, password, instructorProfile);
            String msg = "Instructor user created successfully";
            System.out.println("[DEBUG] addInstructorUser success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] addInstructorUser FAILED -> " + e.getMessage());
            throw new ApiException("Failed to create instructor user: " + e.getMessage(), e);
        }
    }

    public String createCourse(Course course) {
        System.out.println("[DEBUG] createCourse called -> course=" + course);
        try {
            adminService.createCourse(course);
            String msg = "Course created successfully: " + course.getCourseCode();
            System.out.println("[DEBUG] createCourse success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] createCourse FAILED -> " + e.getMessage());
            throw new ApiException("Failed to create course: " + e.getMessage(), e);
        }
    }

    public String createSection(Section section) {
        System.out.println("[DEBUG] createSection called -> section=" + section);
        try {
            adminService.createSection(section);
            String msg = "Section created successfully";
            System.out.println("[DEBUG] createSection success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] createSection FAILED -> " + e.getMessage());
            throw new ApiException("Failed to create section: " + e.getMessage(), e);
        }
    }

    public String assignInstructorToSection(int sectionId, int instructorUserId) {
        System.out.println("[DEBUG] assignInstructorToSection called -> sectionId=" + sectionId + ", instructorUserId=" + instructorUserId);
        try {
            adminService.assignInstructorToSection(sectionId, instructorUserId);
            String msg = "Instructor assigned to section successfully";
            System.out.println("[DEBUG] assignInstructorToSection success -> " + msg);
            return msg;
        } catch (Exception e) {
            System.out.println("[DEBUG] assignInstructorToSection FAILED -> " + e.getMessage());
            throw new ApiException("Failed to assign instructor: " + e.getMessage(), e);
        }
    }

    public List<Instructor> getAllInstructors() {
        System.out.println("[DEBUG] getAllInstructors called");
        try {
            List<Instructor> list = adminService.getAllInstructors();
            System.out.println("[DEBUG] getAllInstructors success -> count=" + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("[DEBUG] getAllInstructors FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve instructors: " + e.getMessage(), e);
        }
    }

    public List<Student> getAllStudents() {
        System.out.println("[DEBUG] getAllStudents called");
        try {
            List<Student> list = adminService.getAllStudents();
            System.out.println("[DEBUG] getAllStudents success -> count=" + list.size());
            return list;
        } catch (Exception e) {
            System.out.println("[DEBUG] getAllStudents FAILED -> " + e.getMessage());
            throw new ApiException("Failed to retrieve students: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int userId) {
        System.out.println("[DEBUG] deleteUser called -> userId=" + userId);
        try {
            adminService.deleteUser(userId);
            System.out.println("[DEBUG] deleteUser success");
        } catch (Exception e) {
            System.out.println("[DEBUG] deleteUser FAILED -> " + e.getMessage());
            throw new ApiException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    public void updateStudent(Student student) {
        System.out.println("[DEBUG] updateStudent called -> student=" + student);
        try {
            adminService.updateStudent(student);
            System.out.println("[DEBUG] updateStudent success");
        } catch (Exception e) {
            System.out.println("[DEBUG] updateStudent FAILED -> " + e.getMessage());
            throw new ApiException("Failed to update student: " + e.getMessage(), e);
        }
    }

    public void updateInstructor(Instructor instructor) {
        System.out.println("[DEBUG] updateInstructor called -> instructor=" + instructor);
        try {
            adminService.updateInstructor(instructor);
            System.out.println("[DEBUG] updateInstructor success");
        } catch (Exception e) {
            System.out.println("[DEBUG] updateInstructor FAILED -> " + e.getMessage());
            throw new ApiException("Failed to update instructor: " + e.getMessage(), e);
        }
    }

    public void deleteCourse(int courseId) {
        System.out.println("[DEBUG] deleteCourse called -> courseId=" + courseId);
        try {
            adminService.deleteCourse(courseId);
            System.out.println("[DEBUG] deleteCourse success");
        } catch (Exception e) {
            System.out.println("[DEBUG] deleteCourse FAILED -> " + e.getMessage());
            throw new ApiException("Failed to delete course: " + e.getMessage(), e);
        }
    }

    public void updateCourse(Course course) {
        System.out.println("[DEBUG] updateCourse called -> course=" + course);
        try {
            adminService.updateCourse(course);
            System.out.println("[DEBUG] updateCourse success");
        } catch (Exception e) {
            System.out.println("[DEBUG] updateCourse FAILED -> " + e.getMessage());
            throw new ApiException("Failed to update course: " + e.getMessage(), e);
        }
    }
}
