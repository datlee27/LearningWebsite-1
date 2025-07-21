-- Active: 1752671726187@@127.0.0.1@3306@learning_management
-- MySQL Learning Management Schema

CREATE DATABASE IF NOT EXISTS learning_management;
USE learning_management;

CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('male', 'female', 'other')),
    phone VARCHAR(20),
    school VARCHAR(100),
    role VARCHAR(10) CHECK (role IN ('student', 'teacher', 'admin')) NOT NULL
);

CREATE TABLE Courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    teacher_id INT,
    thumbnail VARCHAR(255),
    FOREIGN KEY (teacher_id) REFERENCES Users(id)
);

CREATE TABLE Lectures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    title VARCHAR(100) NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE Assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    lecture_id INT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATETIME,
    status VARCHAR(10) CHECK (status IN ('not yet', 'in progress', 'ended')) DEFAULT 'not yet',
    FOREIGN KEY (course_id) REFERENCES Courses(id),
    FOREIGN KEY (lecture_id) REFERENCES Lectures(id)
);

CREATE TABLE Submissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT,
    student_id INT,
    file_url VARCHAR(255),
    submission_date DATETIME,
    grade DECIMAL(4,2),
    FOREIGN KEY (assignment_id) REFERENCES Assignments(id),
    FOREIGN KEY (student_id) REFERENCES Users(id)
);

CREATE TABLE Enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) CHECK (status IN ('enrolled', 'completed', 'dropped')) DEFAULT 'enrolled',
    FOREIGN KEY (student_id) REFERENCES Users(id),
    FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE UserActivity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    login_time DATETIME NOT NULL,
    logout_time DATETIME,
    duration_minutes INT
);

-- Sample data
INSERT INTO Courses (name, description, teacher_id, thumbnail) VALUES
('Introduction to Programming', 'Learn the basics of coding with this beginner-friendly course.', 1, 'thumbnail1.png'),
('Web Development Basics', 'Build your first website with HTML, CSS, and JavaScript.', 1, 'thumbnail2.png'),
('Data Science Fundamentals', 'Explore data analysis and visualization techniques.', 1, 'thumbnail3.png');

-- Example lectures
INSERT INTO Lectures (course_id, title, video_url) VALUES
(1, 'Getting Started with Programming', 'https://video.example.com/programming_intro.mp4'),
(1, 'Variables and Data Types', 'https://video.example.com/variables.mp4'),
(1, 'Control Structures', 'https://video.example.com/control_structures.mp4'),
(2, 'HTML Basics', 'https://video.example.com/html_basics.mp4'),
(2, 'CSS Fundamentals', 'https://video.example.com/css_fundamentals.mp4'),
(2, 'JavaScript Introduction', 'https://video.example.com/js_intro.mp4'),
(3, 'Introduction to Data Science', 'https://video.example.com/ds_intro.mp4'),
(3, 'Data Visualization', 'https://video.example.com/data_viz.mp4'),
(3, 'Basic Statistics', 'https://video.example.com/statistics.mp4');

-- Example enrollments
INSERT INTO Enrollments (student_id, course_id, status) VALUES
(1, 1, 'enrolled'),
(1, 2, 'enrolled'),
(1, 3, 'enrolled');

-- Example submissions
INSERT INTO Submissions (assignment_id, student_id, file_url, submission_date, grade) VALUES
(1, 2, 'file1.pdf', NOW(), 9.0),
(2, 2, 'file2.pdf', NOW(), 8.5);

-- MySQL Procedures and Triggers
DELIMITER //

CREATE PROCEDURE GetEnrollmentCount(IN courseId INT, OUT cnt INT)
BEGIN
    SELECT COUNT(*) INTO cnt FROM Enrollments WHERE course_id = courseId;
END //

CREATE TRIGGER PreventDuplicateEnrollment BEFORE INSERT ON Enrollments
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM Enrollments WHERE student_id = NEW.student_id AND course_id = NEW.course_id
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Student is already enrolled in this course.';
    END IF;
END //

CREATE PROCEDURE CompleteEnrollment(IN enrollmentId INT)
BEGIN
    UPDATE Enrollments
    SET status = 'completed'
    WHERE id = enrollmentId;
END //

CREATE TRIGGER UpdateDurationOnLogout AFTER UPDATE ON UserActivity
FOR EACH ROW
BEGIN
    IF NEW.logout_time IS NOT NULL THEN
        UPDATE UserActivity
        SET duration_minutes = TIMESTAMPDIFF(MINUTE, NEW.login_time, NEW.logout_time)
        WHERE id = NEW.id;
    END IF;
END //

CREATE PROCEDURE GetStudentGrades(IN courseId INT)
BEGIN
    SELECT u.id AS student_id, u.username, a.title, s.grade
    FROM Users u
    JOIN Enrollments e ON u.id = e.student_id
    JOIN Assignments a ON a.course_id = e.course_id
    LEFT JOIN Submissions s ON s.assignment_id = a.id AND s.student_id = u.id
    WHERE e.course_id = courseId;
END //

CREATE PROCEDURE UpdateAssignmentDueDate(IN AssignmentId INT, IN NewDueDate DATETIME)
BEGIN
    UPDATE Assignments
    SET due_date = NewDueDate
    WHERE id = AssignmentId;
END //

CREATE TRIGGER trg_PreventLateSubmission BEFORE INSERT ON Submissions
FOR EACH ROW
BEGIN
    DECLARE due DATETIME;
    SELECT due_date INTO due FROM Assignments WHERE id = NEW.assignment_id;
    IF NEW.submission_date > due THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot submit after assignment due date.';
    END IF;
END //

DELIMITER ;