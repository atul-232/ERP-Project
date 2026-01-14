package edu.univ.erp.access;

import edu.univ.erp.auth.Session;
import edu.univ.erp.domain.UserRole;

public class AccessManager {

    public static boolean isAdmin(Session s) {
        boolean result = (s != null && UserRole.ADMIN.name().equalsIgnoreCase(s.getRole()));
        System.out.println("[DEBUG] isAdmin -> session=" + s + " result=" + result);
        return result;
    }

    public static boolean isInstructor(Session s) {
        boolean result = (s != null && UserRole.INSTRUCTOR.name().equalsIgnoreCase(s.getRole()));
        System.out.println("[DEBUG] isInstructor -> session=" + s + " result=" + result);
        return result;
    }

    public static boolean isStudent(Session s) {
        boolean result = (s != null && UserRole.STUDENT.name().equalsIgnoreCase(s.getRole()));
        System.out.println("[DEBUG] isStudent -> session=" + s + " result=" + result);
        return result;
    }

    private static boolean isModificationAllowed(Session s, boolean isMaintenanceModeOn) {
        System.out.println("[DEBUG] isModificationAllowed called -> session=" + s + ", maintenance=" + isMaintenanceModeOn);

        boolean allowed;
        if (s == null) {
            allowed = false;
        } else if (isAdmin(s)) {
            allowed = true;
        } else if (isMaintenanceModeOn) {
            allowed = false;
        } else {
            allowed = true;
        }

        System.out.println("[DEBUG] isModificationAllowed -> result=" + allowed);
        return allowed;
    }

    public static boolean canModifyStudentData(Session s, int studentUserId, boolean isMaintenanceModeOn) {
        System.out.println("[DEBUG] canModifyStudentData -> session=" + s + ", studentUserId=" + studentUserId + ", maintenance=" + isMaintenanceModeOn);

        boolean result;

        if (isModificationAllowed(s, isMaintenanceModeOn)) {
            if (isAdmin(s)) {
                result = true;
            } else if (isStudent(s) && s.getUserId() == studentUserId) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        System.out.println("[DEBUG] canModifyStudentData -> result=" + result);
        return result;
    }

    public static boolean canModifyInstructorSection(Session s, int instructorUserId, boolean isMaintenanceModeOn) {
        System.out.println("[DEBUG] canModifyInstructorSection -> session=" + s + ", instructorUserId=" + instructorUserId + ", maintenance=" + isMaintenanceModeOn);

        boolean result;

        if (isModificationAllowed(s, isMaintenanceModeOn)) {
            if (isAdmin(s)) {
                result = true;
            } else if (isInstructor(s) && s.getUserId() == instructorUserId) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        System.out.println("[DEBUG] canModifyInstructorSection -> result=" + result);
        return result;
    }
}
