SET SQL_SAFE_UPDATES = 0;

USE auth_db;

DELETE FROM users_auth;

INSERT INTO `users_auth` (`user_id`, `username`, `role`, `password_hash`) VALUES
(1, 'admin1', 'ADMIN', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'),
(101, 'stu1', 'STUDENT', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'),
(102, 'stu2', 'STUDENT', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm'),
(201, 'inst1', 'INSTRUCTOR', '$2b$10$p0dJzlh9.riRgUMu61R7neVmBp7PutNrnjFGNsNtvSLWVIPtAXDKm');

USE erp_db;

DELETE FROM students;
DELETE FROM instructors;

INSERT INTO `instructors` (`user_id`, `full_name`, `department`) VALUES
(201, 'Dr. Alan Turing', 'Computer Science');

INSERT INTO `students` (`user_id`, `full_name`, `roll_no`, `program`, `year`) VALUES
(101, 'Ada Lovelace', '2025-CS-01', 'Computer Science', 1),
(102, 'Grace Hopper', '2025-CS-02', 'Computer Science', 1);

SET SQL_SAFE_UPDATES = 1;
