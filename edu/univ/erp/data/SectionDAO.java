package edu.univ.erp.data;

import edu.univ.erp.domain.Section;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SectionDAO {

    public void insert(Section section) {
        System.out.println("[DEBUG] insert called -> section=" + section);
        String sql = "INSERT INTO sections(course_id, instructor_id, day_time, room, capacity, semester, year) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, section.getCourseId());
            pst.setInt(2, section.getInstructorId());
            pst.setString(3, section.getDayTime());
            pst.setString(4, section.getRoom());
            pst.setInt(5, section.getCapacity());
            pst.setString(6, section.getSemester());
            pst.setInt(7, section.getYear());

            System.out.println("[DEBUG] Executing INSERT -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] insert SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] insert FAILED -> " + e.getMessage());
            throw new DataAccessException("Error inserting section", e);
        }
    }

    public Optional<Section> findById(int sectionId) {
        System.out.println("[DEBUG] findById called -> sectionId=" + sectionId);
        String sql = "SELECT * FROM sections WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, sectionId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Section section = mapRowToSection(rs);
                System.out.println("[DEBUG] findById SUCCESS -> section found");
                return Optional.of(section);
            } else {
                System.out.println("[DEBUG] findById -> NO RESULT");
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] findById FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding section with id: " + sectionId, e);
        }
        return Optional.empty();
    }

    public List<Section> findByInstructorId(int instructorId) {
        System.out.println("[DEBUG] findByInstructorId called -> instructorId=" + instructorId);
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT * FROM sections WHERE instructor_id = ?";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, instructorId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                sections.add(mapRowToSection(rs));
            }

            System.out.println("[DEBUG] findByInstructorId SUCCESS -> count=" + sections.size());
            return sections;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findByInstructorId FAILED -> " + e.getMessage());
            throw new DataAccessException("Error finding sections for instructor ID: " + instructorId, e);
        }
    }

    public List<Section> findAll() {
        System.out.println("[DEBUG] findAll called");
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT * FROM sections";

        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            while (rs.next()) {
                sections.add(mapRowToSection(rs));
            }

            System.out.println("[DEBUG] findAll SUCCESS -> count=" + sections.size());
            return sections;

        } catch (SQLException e) {
            System.out.println("[DEBUG] findAll FAILED -> " + e.getMessage());
            throw new DataAccessException("Error retrieving all sections.", e);
        }
    }

    public int getEnrolledCount(int sectionId) {
        System.out.println("[DEBUG] getEnrolledCount called -> sectionId=" + sectionId);
        String sql = "SELECT COUNT(*) FROM enrollments WHERE section_id = ? AND status = 'ENROLLED'";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, sectionId);
            System.out.println("[DEBUG] Executing SELECT -> " + sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("[DEBUG] getEnrolledCount SUCCESS -> count=" + count);
                return count;
            }

        } catch (SQLException e) {
            System.out.println("[DEBUG] getEnrolledCount FAILED -> " + e.getMessage());
            throw new DataAccessException("Error getting enrolled count for section ID: " + sectionId, e);
        }
        return 0;
    }

    public void update(Section section) {
        System.out.println("[DEBUG] update called -> section=" + section);
        String sql = "UPDATE sections SET course_id=?, instructor_id=?, day_time=?, room=?, capacity=?, semester=?, year=? WHERE section_id = ?";
        try (Connection conn = DBConnection.getConnection("erp_db");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, section.getCourseId());
            pst.setInt(2, section.getInstructorId());
            pst.setString(3, section.getDayTime());
            pst.setString(4, section.getRoom());
            pst.setInt(5, section.getCapacity());
            pst.setString(6, section.getSemester());
            pst.setInt(7, section.getYear());
            pst.setInt(8, section.getSectionId());

            System.out.println("[DEBUG] Executing UPDATE -> " + sql);
            pst.executeUpdate();
            System.out.println("[DEBUG] update SUCCESS");

        } catch (SQLException e) {
            System.out.println("[DEBUG] update FAILED -> " + e.getMessage());
            throw new DataAccessException("Error updating section", e);
        }
    }

    private Section mapRowToSection(ResultSet rs) throws SQLException {
        Section section = new Section(
                rs.getInt("section_id"),
                rs.getInt("course_id"),
                rs.getInt("instructor_id"),
                rs.getString("day_time"),
                rs.getString("room"),
                rs.getInt("capacity"),
                rs.getString("semester"),
                rs.getInt("year")
        );
        System.out.println("[DEBUG] mapRowToSection -> id=" + section.getSectionId());
        return section;
    }
}
