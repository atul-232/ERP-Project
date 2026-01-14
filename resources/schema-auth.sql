
CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

DROP TABLE IF EXISTS `users_auth`;

CREATE TABLE `users_auth` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `role` ENUM('STUDENT', 'INSTRUCTOR', 'ADMIN') NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `status` VARCHAR(20) DEFAULT 'ACTIVE',
  `last_login` DATETIME,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;