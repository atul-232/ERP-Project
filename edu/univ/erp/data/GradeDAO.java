package edu.univ.erp.data;

import edu.univ.erp.domain.Grade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public void save(Grade grade) {
        System.out.println("[DEBUG] save called -> grade=" + grade);
        String checkSql = "SELECT grade_id FROM grades WHERE enrollment_id = ? AND component = ?";
        String insertSql = "INSERT INTO grades(enrollment_id, component, score) VALUES (?, ?, ?)";
        String updateSql = "UPDATE grades SET score = ? WHERE grade_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db")) {

            try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
                checkPst.setInt(1, grade.getEnrollmentId());
                checkPst.setString(2, grade.getComponent());

                System.out.println("[DEBUG] Executing SELECT (check existing) -> " + checkSql);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    int gradeId = rs.getInt("grade_id");
                    System.out.println("[DEBUG] grade exists -> UPDATE gradeId=" + gradeId);

                    try (PreparedStatement updatePst = conn.prepareStatement(updateSql)) {
                        updatePst.setDouble(1, grade.getScore());
                        updatePst.setInt(2, gradeId);

                        System.out.println("[DEBUG] Executing UPDATE -> " + updateSql);
                        updatePst.executeUpdate();
                        System.out.println("[DEBUG] update SUCCESS");
                    }

                } else {
                    System.out.println("[DEBUG] grade does NOT exist -> INSERT");

                    try (PreparedStatement insertPst = conn.prepareStatement(insertSql)) {
                        insertPst.setInt(1, grade.getEnrollmentId());
                        insertPst.setString(2, grade.getComponent());
                        insertPst.setDouble(3, grade.getScore());

                        System.out.println("[DEBUG] Executing INSERT -> " + insertSql);
                        insertPst.executeUpdate();
                        System.out.println("[DEBUG] insert SUCCESS");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] save FAILED -> " + e.getMessage());
            throw new DataAccessException("Error saving grade for enrollment ID: " + grade.getEnrollmentId(), e);
        }
    }

    public List<Grade> findByEnrollmentId(int enrollmentId) {
        System.out.println("[DEBUG] findByEnrollmentId called -> enrollmentId=" + enrollmentId);
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE enrollment_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, enrollmentId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                grades.add(mapRowToGrade(rs));
            }

            System.out.println("[DEBUG] findByEnrollmentId SUCCESS -> count=" + grades.size());
            return grades;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByEnrollmentId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding grades for enrollment ID: " + enrollmentId, e);
        }
    }

    private Grade mapRowToGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade(
                rs.getInt("grade_id"),
                rs.getInt("enrollment_id"),
                rs.getString("component"),
                rs.getDouble("score")
        );
        System.out.println("[DEBUG] mapRowToGrade -> gradeId=" + grade.getGradeId());
        return grade;
    }
}
