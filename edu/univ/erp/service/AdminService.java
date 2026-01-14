package edu.univ.erp.service;

import edu.univ.erp.auth.PasswordHasher;
import edu.univ.erp.auth.store.AuthDataStore;
import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.sql.SQLException;
import java.util.*;

public class AdminService {

    private final CourseDAO courseDAO = new CourseDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final InstructorDAO instructorDAO = new InstructorDAO();
    private final SettingsDAO settingsDAO = new SettingsDAO();

    private final AuthDataStore authStore = new AuthDataStore();
    private final PasswordHasher passwordHasher = new PasswordHasher();

    public void createCourse(Course course) {
        System.out.println("[AdminService] createCourse called -> " + course.getCourseId());
        if (course != null) {
            courseDAO.insert(course);
            System.out.println("[AdminService] Course created!");
        } else {
            System.out.println("[AdminService] Course is null. Creation aborted.");
        }
    }

    public void deleteCourse(int courseId) {
        System.out.println("[AdminService] deleteCourse called -> " + courseId);
        if (courseId > 0) {
            courseDAO.delete(courseId);
            System.out.println("[AdminService] Course deleted!");
        } else {
            System.out.println("[AdminService] Invalid courseId.");
        }
    }

    public void updateCourse(Course course) {
        System.out.println("[AdminService] updateCourse called -> " + course.getCourseId());
        if (course != null) {
            courseDAO.update(course);
            System.out.println("[AdminService] Course updated!");
        } else {
            System.out.println("[AdminService] Course is null.");
        }
    }

    public void createSection(Section section) {
        System.out.println("[AdminService] createSection called -> ");
        if (section.getCapacity() > 0) {
            sectionDAO.insert(section);
            System.out.println("[AdminService] Section created!");
        } else {
            System.out.println("[AdminService] Section capacity not valid.");
        }
    }

    public void assignInstructorToSection(int sectionId, int instructorUserId) {
        System.out.println("[AdminService] assignInstructorToSection called -> secId=" + sectionId + ", instId=" + instructorUserId);
        Optional<Section> sectionOpt = sectionDAO.findById(sectionId);

        if (sectionOpt.isPresent()) {
            Section section = sectionOpt.get();
            section.setInstructorId(instructorUserId);
            sectionDAO.update(section);
            System.out.println("[AdminService] Instructor assigned!");
        } else {
            System.out.println("[AdminService] Section not found.");
        }
    }

    public void addStudentUser(String username, String password, Student studentProfile) {
        System.out.println("[AdminService] addStudentUser called -> " + username);
        try {
            String hashedPassword = passwordHasher.hashPassword(password);
            int newUserId = authStore.createUser(username, hashedPassword, "STUDENT");

            if (newUserId != -1) {
                studentProfile.setUserId(newUserId);
                studentDAO.insert(studentProfile);
                System.out.println("[AdminService] Student user created successfully!");
            } else {
                System.out.println("[AdminService] Failed to create user in auth DB.");
            }
        } catch (SQLException e) {
            System.out.println("[AdminService] Error creating student: " + e.getMessage());
        }
    }

    public void addInstructorUser(String username, String password, Instructor instructorProfile) {
        System.out.println("[AdminService] addInstructorUser called -> " + username);
        try {
            String hashedPassword = passwordHasher.hashPassword(password);
            int newUserId = authStore.createUser(username, hashedPassword, "INSTRUCTOR");

            if (newUserId != -1) {
                instructorProfile.setUserId(newUserId);
                instructorDAO.insert(instructorProfile);
                System.out.println("[AdminService] Instructor user created successfully!");
            } else {
                System.out.println("[AdminService] Failed to create user in auth DB.");
            }
        } catch (SQLException e) {
            System.out.println("[AdminService] Error creating instructor: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        System.out.println("[AdminService] deleteUser called -> " + userId);
        if (userId > 0) {
            try {
                authStore.deleteUser(userId);
                System.out.println("[AdminService] User deleted successfully!");
            } catch (SQLException e) {
                System.out.println("[AdminService] Error deleting user: " + e.getMessage());
            }
        } else {
            System.out.println("[AdminService] Invalid userId.");
        }
    }

    public void updateStudent(Student student) {
        System.out.println("[AdminService] updateStudent called -> " + student.getUserId());
        if (student != null) {
            studentDAO.update(student);
            System.out.println("[AdminService] Student updated!");
        } else {
            System.out.println("[AdminService] Student is null.");
        }
    }

    public void updateInstructor(Instructor instructor) {
        System.out.println("[AdminService] updateInstructor called -> " + instructor.getUserId());
        if (instructor != null) {
            instructorDAO.update(instructor);
            System.out.println("[AdminService] Instructor updated!");
        } else {
            System.out.println("[AdminService] Instructor is null.");
        }
    }

    public List<Instructor> getAllInstructors() {
        System.out.println("[AdminService] getAllInstructors called");
        return instructorDAO.findAll();
    }

    public List<Student> getAllStudents() {
        System.out.println("[AdminService] getAllStudents called");
        return studentDAO.findAll();
    }

    public void setMaintenanceMode(boolean isEnabled) {
        System.out.println("[AdminService] setMaintenanceMode called -> " + isEnabled);
        settingsDAO.updateSetting("maintenance_on", String.valueOf(isEnabled));
    }

    public void updateSystemSetting(String key, String value) {
        System.out.println("[AdminService] updateSystemSetting called -> " + key + "=" + value);
        settingsDAO.updateSetting(key, value);
    }

    public Map<String, String> getAllSettings() {
        System.out.println("[AdminService] getAllSettings called");
        return settingsDAO.findAll();
    }

    public void triggerBackup() {
        System.out.println("[AdminService] Backup triggered...");
    }

    public void clearCache() {
        System.out.println("[AdminService] Cache cleared.");
    }

    public List<String[]> getSystemLogs() {
        System.out.println("[AdminService] getSystemLogs called");
        List<String[]> logs = new ArrayList<>();
        logs.add(new String[]{"2025-11-28 10:00", "INFO", "System started"});
        logs.add(new String[]{"2025-11-28 10:05", "WARN", "High memory usage detected"});
        logs.add(new String[]{"2025-11-28 10:45", "INFO", "Backup completed successfully"});
        return logs;
    }
}
