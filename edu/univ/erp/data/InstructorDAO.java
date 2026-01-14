package edu.univ.erp.data;

import edu.univ.erp.domain.Instructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorDAO {

    public void insert(Instructor instructor) {
        System.out.println("[DEBUG] insert called -> instructor=" + instructor);
        String sql = "INSERT INTO instructors(user_id, department, full_name) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, instructor.getUserId());
            pst.setString(2, instructor.getDepartment());
            pst.setString(3, instructor.getFullName());

            System.out.println("[DEBUG] Executing INSERT -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] insert SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] insert FAILED -> " + e.getMessage());
            throw new DataAccessException("Error inserting instructor: " + instructor.getFullName(), e);
        }
    }

    public Optional<Instructor> findByUserId(int userId) {
        System.out.println("[DEBUG] findByUserId called -> userId=" + userId);
        String sql = "SELECT * FROM instructors WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Instructor instructor = mapRowToInstructor(rs);
                System.out.println("[DEBUG] findByUserId SUCCESS -> instructor=" + instructor.getFullName());
                return Optional.of(instructor);
            } else {
                System.out.println("[DEBUG] findByUserId -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByUserId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding instructor with user ID: " + userId, e);
        }

        return Optional.empty();
    }

    public List<Instructor> findAll() {
        System.out.println("[DEBUG] findAll called");
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructors ORDER BY full_name";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            while (rs.next()) {
                instructors.add(mapRowToInstructor(rs));
            }
            System.out.println("[DEBUG] findAll SUCCESS -> count=" + instructors.size());
            return instructors;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error retrieving all instructors.", e);
        }
    }

    public void update(Instructor instructor) {
        System.out.println("[DEBUG] update called -> instructor=" + instructor);
        String sql = "UPDATE instructors SET full_name=?, department=? WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, instructor.getFullName());
            pst.setString(2, instructor.getDepartment());
            pst.setInt(3, instructor.getUserId());

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] update SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] update FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating instructor with user ID: " + instructor.getUserId(), e);
        }
    }

    private Instructor mapRowToInstructor(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor(
                rs.getInt("instructor_id"),
                rs.getInt("user_id"),
                rs.getString("department"),
                rs.getString("full_name")
        );
        System.out.println("[DEBUG] mapRowToInstructor -> instructorId=" + instructor.getInstructorId());
        return instructor;
    }
}
