package edu.univ.erp.data;

import edu.univ.erp.domain.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO {

    public void insert(Student student) {
        System.out.println("[DEBUG] insert called -> student=" + student);
        String sql = "INSERT INTO students(user_id, full_name, roll_no, program, year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, student.getUserId());
            pst.setString(2, student.getFullName());
            pst.setString(3, student.getRollNo());
            pst.setString(4, student.getProgram());
            pst.setInt(5, student.getYear());

            System.out.println("[DEBUG] Executing INSERT -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] insert SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] insert FAILED -> " + e.getMessage());
            throw new DataAccessException("Error inserting student", e);
        }
    }

    public Optional<Student> findByUserId(int userId) {
        System.out.println("[DEBUG] findByUserId called -> userId=" + userId);
        String sql = "SELECT * FROM students WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Student student = mapRowToStudent(rs);
                System.out.println("[DEBUG] findByUserId SUCCESS -> student=" + student.getFullName());
                return Optional.of(student);
            } else {
                System.out.println("[DEBUG] findByUserId -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByUserId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding student with user ID: " + userId, e);
        }
        return Optional.empty();
    }

    public Optional<Student> findById(int studentId) {
        System.out.println("[DEBUG] findById called -> studentId=" + studentId);
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Student student = mapRowToStudent(rs);
                System.out.println("[DEBUG] findById SUCCESS -> student=" + student.getFullName());
                return Optional.of(student);
            } else {
                System.out.println("[DEBUG] findById -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findById FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding student with ID: " + studentId, e);
        }
        return Optional.empty();
    }

    public List<Student> findAll() {
        System.out.println("[DEBUG] findAll called");
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
            System.out.println("[DEBUG] findAll SUCCESS -> count=" + students.size());
            return students;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error retrieving all students.", e);
        }
    }

    public void update(Student student) {
        System.out.println("[DEBUG] update called -> student=" + student);
        String sql = "UPDATE students SET full_name=?, roll_no=?, program=?, year=? WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, student.getFullName());
            pst.setString(2, student.getRollNo());
            pst.setString(3, student.getProgram());
            pst.setInt(4, student.getYear());
            pst.setInt(5, student.getUserId());

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] update SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] update FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating student with user ID: " + student.getUserId(), e);
        }
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student(
                rs.getInt("student_id"),
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("roll_no"),
                rs.getString("program"),
                rs.getInt("year")
        );
        System.out.println("[DEBUG] mapRowToStudent -> studentId=" + student.getStudentId());
        return student;
    }
}
