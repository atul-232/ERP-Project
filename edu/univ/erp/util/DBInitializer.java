package edu.univ.erp.util;

import edu.univ.erp.data.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initializeAuthDB() {
        System.out.println("[DBInitializer] Starting initialization of auth_db...");
        String sql = "CREATE TABLE IF NOT EXISTS `users_auth` (" +
                "`user_id` INT NOT NULL AUTO_INCREMENT, " +
                "`username` VARCHAR(50) NOT NULL, " +
                "`role` ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL, " +
                "`password_hash` VARCHAR(255) NOT NULL, " +
                "`status` VARCHAR(20) DEFAULT 'ACTIVE', " +
                "`last_login` DATETIME, " +
                "PRIMARY KEY (`user_id`), " +
                "UNIQUE KEY `uk_username` (`username`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        try (Connection conn = DBConnection.getConnection("auth_db");
             Statement stmt = conn.createStatement()) {

            if (DBConnection.getDbNameOverride() == null) {
                System.out.println("[DBInitializer] Creating database auth_db (if missing)...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS auth_db;");
                stmt.executeUpdate("USE auth_db;");
            }
            
            stmt.executeUpdate(sql);
            System.out.println(" auth_db schema initialized successfully.");

            // Auto-seed auth table if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `users_auth`;")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DBInitializer] Seeding default users into users_auth...");
                    stmt.executeUpdate("INSERT INTO `users_auth` (`user_id`, `username`, `role`, `password_hash`) VALUES " +
                            "(1, 'admin1', 'ADMIN', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'), " +
                            "(101, 'stu1', 'STUDENT', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'), " +
                            "(102, 'stu2', 'STUDENT', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'), " +
                            "(201, 'inst1', 'INSTRUCTOR', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm');");
                }
            }

        } catch (SQLException e) {
            System.err.println(" Error initializing auth_db: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initializeErpDB() {
        System.out.println("[DBInitializer] Starting initialization of erp_db...");

        try (Connection conn = DBConnection.getConnection("erp_db");
             Statement stmt = conn.createStatement()) {

            if (DBConnection.getDbNameOverride() == null) {
                System.out.println("[DBInitializer] Creating database erp_db (if missing)...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS erp_db;");
                stmt.executeUpdate("USE erp_db;");
            }

            System.out.println("[DBInitializer] Creating core tables...");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `courses` (" +
                    "`course_id` INT NOT NULL AUTO_INCREMENT, " +
                    "`code` VARCHAR(20) NOT NULL, " +
                    "`title` VARCHAR(255) NOT NULL, " +
                    "`credits` INT NOT NULL, " +
                    "`department` VARCHAR(100), " +
                    "PRIMARY KEY (`course_id`), " +
                    "UNIQUE KEY `uk_course_code` (`code`)) ENGINE=InnoDB;");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `instructors` (`instructor_id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL, `full_name` VARCHAR(255) NOT NULL, `department` VARCHAR(100) NOT NULL, PRIMARY KEY (`instructor_id`), UNIQUE KEY `uk_user_id` (`user_id`)) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `students` (`student_id` INT NOT NULL AUTO_INCREMENT, `user_id` INT NOT NULL, `full_name` VARCHAR(255) NOT NULL, `roll_no` VARCHAR(50) NOT NULL, `program` VARCHAR(100) NOT NULL, `year` INT NOT NULL, PRIMARY KEY (`student_id`), UNIQUE KEY `uk_user_id` (`user_id`), UNIQUE KEY `uk_roll_no` (`roll_no`)) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `sections` (`section_id` INT NOT NULL AUTO_INCREMENT, `course_id` INT NOT NULL, `instructor_id` INT, `day_time` VARCHAR(100) NOT NULL, `room` VARCHAR(50) NOT NULL, `capacity` INT NOT NULL, `semester` VARCHAR(20) NOT NULL, `year` INT NOT NULL, PRIMARY KEY (`section_id`)) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `enrollments` (`enrollment_id` INT NOT NULL AUTO_INCREMENT, `student_id` INT NOT NULL, `section_id` INT NOT NULL, `status` ENUM('ENROLLED', 'DROPPED') NOT NULL, `final_grade` VARCHAR(5), PRIMARY KEY (`enrollment_id`), UNIQUE KEY `uk_student_section` (`student_id`, `section_id`)) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `grades` (`grade_id` INT NOT NULL AUTO_INCREMENT, `enrollment_id` INT NOT NULL, `component` VARCHAR(100) NOT NULL, `score` DECIMAL(5,2) NOT NULL, PRIMARY KEY (`grade_id`), UNIQUE KEY `uk_enrollment_component` (`enrollment_id`, `component`)) ENGINE=InnoDB;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `settings` (`key` VARCHAR(50) NOT NULL, `value` TEXT, PRIMARY KEY (`key`)) ENGINE=InnoDB;");

            System.out.println("[DBInitializer] Adding foreign key constraints...");
            try { stmt.executeUpdate("ALTER TABLE `sections` ADD CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE;"); System.out.println("  ✔ FK: sections → courses"); } catch (SQLException ignored) {}
            try { stmt.executeUpdate("ALTER TABLE `sections` ADD CONSTRAINT `sections_ibfk_2` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`instructor_id`) ON DELETE SET NULL;"); System.out.println("  ✔ FK: sections → instructors"); } catch (SQLException ignored) {}
            try { stmt.executeUpdate("ALTER TABLE `enrollments` ADD CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE;"); System.out.println("  ✔ FK: enrollments → students"); } catch (SQLException ignored) {}
            try { stmt.executeUpdate("ALTER TABLE `enrollments` ADD CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`) ON DELETE CASCADE;"); System.out.println("  ✔ FK: enrollments → sections"); } catch (SQLException ignored) {}
            try { stmt.executeUpdate("ALTER TABLE `grades` ADD CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`enrollment_id`) ON DELETE CASCADE;"); System.out.println("  ✔ FK: grades → enrollments"); } catch (SQLException ignored) {}

            System.out.println("erp_db schema initialized successfully.");

            // Auto-seed settings table if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `settings`;")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DBInitializer] Seeding settings table...");
                    stmt.executeUpdate("INSERT INTO `settings` (`key`, `value`) VALUES ('maintenance_on', 'false');");
                }
            }

            // Auto-seed instructors table if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `instructors`;")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DBInitializer] Seeding instructors table...");
                    stmt.executeUpdate("INSERT INTO `instructors` (`user_id`, `full_name`, `department`) VALUES " +
                            "(201, 'Dr. Alan Turing', 'Computer Science');");
                }
            }

            // Auto-seed students table if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `students`;")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("[DBInitializer] Seeding students table...");
                    stmt.executeUpdate("INSERT INTO `students` (`user_id`, `full_name`, `roll_no`, `program`, `year`) VALUES " +
                            "(101, 'Ada Lovelace', '2025-CS-01', 'Computer Science', 1), " +
                            "(102, 'Grace Hopper', '2025-CS-02', 'Computer Science', 1);");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error initializing erp_db: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
