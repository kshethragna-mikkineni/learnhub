-- Optional: Hibernate (ddl-auto=update) creates these tables automatically.
-- Run this file instead if you prefer to manage the schema yourself.
CREATE DATABASE IF NOT EXISTS learnhub CHARACTER SET utf8mb4;
USE learnhub;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL DEFAULT 'STUDENT',
  created_at DATETIME(6)
);
CREATE TABLE courses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(2000),
  category VARCHAR(64),
  level VARCHAR(32),
  instructor VARCHAR(255),
  duration_hours INT NOT NULL DEFAULT 0
);
CREATE TABLE enrollments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  progress INT NOT NULL DEFAULT 0,
  last_score INT NULL,
  enrolled_at DATETIME(6),
  UNIQUE KEY uq_user_course (user_id, course_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);
CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT NOT NULL,
  text VARCHAR(500) NOT NULL,
  correct_index INT NOT NULL,
  FOREIGN KEY (course_id) REFERENCES courses(id)
);
CREATE TABLE question_options (
  question_id BIGINT NOT NULL,
  option_order INT NOT NULL,
  option_text VARCHAR(255),
  PRIMARY KEY (question_id, option_order),
  FOREIGN KEY (question_id) REFERENCES questions(id)
);
