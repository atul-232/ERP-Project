package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAO {

    public void insert(Course course) {
        System.out.println("[DEBUG] insert called -> course=" + course);
        String sql = "INSERT INTO courses(code, title, credits, department) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, course.getCourseCode());
            pst.setString(2, course.getTitle());
            pst.setInt(3, course.getCredits());
            pst.setString(4, course.getDepartment());

            System.out.println("[DEBUG] Executing INSERT -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] insert SUCCESS -> title=" + course.getTitle());

        } catch (SQLException e) {
            System.out.println("[DEBUG] insert FAILED -> " + e.getMessage());
            throw new DataAccessException("Error inserting course: " + course.getTitle(), e);
        }
    }

    public Optional<Course> findById(int courseId) {
        System.out.println("[DEBUG] findById called -> courseId=" + courseId);
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, courseId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Course course = mapRowToCourse(rs);
                System.out.println("[DEBUG] findById SUCCESS -> courseCode=" + course.getCourseCode());
                return Optional.of(course);
            } else {
                System.out.println("[DEBUG] findById -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findById FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding course with id: " + courseId, e);
        }

        return Optional.empty();
    }

    public List<Course> findAll() {
        System.out.println("[DEBUG] findAll called");
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY code";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);

            while (rs.next()) {
                courses.add(mapRowToCourse(rs));
            }

            System.out.println("[DEBUG] findAll SUCCESS -> count=" + courses.size());
            return courses;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error retrieving all courses.", e);
        }
    }

    public void update(Course course) {
        System.out.println("[DEBUG] update called -> courseId=" + course.getCourseId());
        String sql = "UPDATE courses SET code=?, title=?, credits=?, department=? WHERE course_id=?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, course.getCourseCode());
            pst.setString(2, course.getTitle());
            pst.setInt(3, course.getCredits());
            pst.setString(4, course.getDepartment());
            pst.setInt(5, course.getCourseId());

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            int rows = pst.executeUpdate();
            System.out.println("[DEBUG] update SUCCESS -> affectedRows=" + rows);

        } catch (SQLException e) {
            System.out.println("[DEBUG] update FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating course ID: " + course.getCourseId(), e);
        }
    }

    public void delete(int courseId) {
        System.out.println("[DEBUG] delete called -> courseId=" + courseId);
        String sql = "DELETE FROM courses WHERE course_id=?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, courseId);
            System.out.println("[DEBUG] Executing DELETE -> " + sql);
            int rows = pst.executeUpdate();
            System.out.println("[DEBUG] delete SUCCESS -> affectedRows=" + rows);

        } catch (SQLException e) {
            System.out.println("[DEBUG] delete FAILED -> " + e.getMessage());
            throw new DataAccessException("Error deleting course ID: " + courseId, e);
        }
    }

    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("code"),
                rs.getString("title"),
                rs.getInt("credits"),
                rs.getString("department")
        );
        System.out.println("[DEBUG] mapRowToCourse -> courseCode=" + course.getCourseCode());
        return course;
    }
}
