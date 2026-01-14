package edu.univ.erp.data;

import edu.univ.erp.domain.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAO {

    public void insert(Enrollment enrollment) {
        System.out.println("[DEBUG] insert called -> " + enrollment);
        String sql = "INSERT INTO enrollments(student_id, section_id, status) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, enrollment.getStudentId());
            pst.setInt(2, enrollment.getSectionId());
            pst.setString(3, enrollment.getStatus());
            pst.setString(4, enrollment.getStatus());

            System.out.println("[DEBUG] Executing INSERT/UPSERT -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] insert SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] insert FAILED -> " + e.getMessage());
            throw new DataAccessException("Error inserting enrollment: " + e.getMessage(), e);
        }
    }

    public List<Enrollment> findAll() {
        System.out.println("[DEBUG] findAll called");
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            while (rs.next()) {
                enrollments.add(mapRowToEnrollment(rs));
            }
            System.out.println("[DEBUG] findAll SUCCESS -> count=" + enrollments.size());
            return enrollments;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding all enrollments", e);
        }
    }

    public void updateFinalGrade(int enrollmentId, String finalGrade) {
        System.out.println("[DEBUG] updateFinalGrade called -> enrollmentId=" + enrollmentId + ", finalGrade=" + finalGrade);
        String sql = "UPDATE enrollments SET final_grade = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, finalGrade);
            pst.setInt(2, enrollmentId);

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] updateFinalGrade SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] updateFinalGrade FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating final grade for enrollment ID: " + enrollmentId, e);
        }
    }

    public List<Enrollment> findByStudentId(int studentId) {
        System.out.println("[DEBUG] findByStudentId called -> studentId=" + studentId);
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapRowToEnrollment(rs));
                }
            }

            System.out.println("[DEBUG] findByStudentId SUCCESS -> count=" + enrollments.size());
            return enrollments;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByStudentId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding enrollments for student ID: " + studentId, e);
        }
    }

    public List<Enrollment> findBySectionId(int sectionId) {
        System.out.println("[DEBUG] findBySectionId called -> sectionId=" + sectionId);
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE section_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, sectionId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapRowToEnrollment(rs));
                }
            }

            System.out.println("[DEBUG] findBySectionId SUCCESS -> count=" + enrollments.size());
            return enrollments;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findBySectionId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding enrollments for section ID: " + sectionId, e);
        }
    }

    public void updateStatus(int enrollmentId, String newStatus) {
        System.out.println("[DEBUG] updateStatus called -> enrollmentId=" + enrollmentId + ", newStatus=" + newStatus);
        String sql = "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, newStatus);
            pst.setInt(2, enrollmentId);

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] updateStatus SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] updateStatus FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating status for enrollment ID: " + enrollmentId, e);
        }
    }

    private Enrollment mapRowToEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("section_id"),
                rs.getString("status"),
                rs.getString("final_grade")
        );
        System.out.println("[DEBUG] mapRowToEnrollment -> enrollmentId=" + e.getEnrollmentId());
        return e;
    }

    public Optional<Enrollment> findByStudentAndSectionId(int studentId, int sectionId) {
        System.out.println("[DEBUG] findByStudentAndSectionId called -> studentId=" + studentId + ", sectionId=" + sectionId);
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND section_id = ? AND status = 'ENROLLED'";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            pst.setInt(2, sectionId);

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Enrollment e = mapRowToEnrollment(rs);
                System.out.println("[DEBUG] findByStudentAndSectionId SUCCESS -> enrollmentId=" + e.getEnrollmentId());
                return Optional.of(e);
            } else {
                System.out.println("[DEBUG] findByStudentAndSectionId -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByStudentAndSectionId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding enrollment for student " + studentId + " in section " + sectionId, e);
        }

        return Optional.empty();
    }
}
