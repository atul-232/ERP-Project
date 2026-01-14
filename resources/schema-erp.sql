
CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `enrollments`;
DROP TABLE IF EXISTS `sections`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `instructors`;
DROP TABLE IF EXISTS `courses`;
DROP TABLE IF EXISTS `settings`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `courses` (
  `course_id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(20) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `credits` INT NOT NULL,
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `uk_course_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `instructors` (
  `instructor_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `department` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`instructor_id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `students` (
  `student_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `roll_no` VARCHAR(50) NOT NULL,
  `program` VARCHAR(100) NOT NULL,
  `year` INT NOT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_roll_no` (`roll_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sections` (
  `section_id` INT NOT NULL AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `instructor_id` INT,
  `day_time` VARCHAR(100) NOT NULL,
  `room` VARCHAR(50) NOT NULL,
  `capacity` INT NOT NULL,
  `semester` VARCHAR(20) NOT NULL,
  `year` INT NOT NULL,
  PRIMARY KEY (`section_id`),
  KEY `fk_course_id` (`course_id`),
  KEY `fk_instructor_id` (`instructor_id`),
  CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `sections_ibfk_2` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`instructor_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `enrollments` (
  `enrollment_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` INT NOT NULL,
  `section_id` INT NOT NULL,
  `status` ENUM('ENROLLED', 'DROPPED') NOT NULL,
  `final_grade` VARCHAR(5),
  PRIMARY KEY (`enrollment_id`),
  UNIQUE KEY `uk_student_section` (`student_id`, `section_id`),
  KEY `fk_student_id` (`student_id`),
  KEY `fk_section_id` (`section_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE,
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `grades` (
  `grade_id` INT NOT NULL AUTO_INCREMENT,
  `enrollment_id` INT NOT NULL,
  `component` VARCHAR(100) NOT NULL,
  `score` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `uk_enrollment_component` (`enrollment_id`, `component`),
  CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`enrollment_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `settings` (
  `key` VARCHAR(50) NOT NULL,
  `value` TEXT,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `settings` (`key`, `value`) VALUES ('maintenance_on', 'false');