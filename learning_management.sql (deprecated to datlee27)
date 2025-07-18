-- Active: 1752587118467@@127.0.0.1@1433@learning_management
CREATE DATABASE learning_management;

USE learning_management;

CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('male', 'female', 'other')),
    phone VARCHAR(20),
    school VARCHAR(100),
    role VARCHAR(10) CHECK (role IN ('student', 'teacher', 'admin')) NOT NULL
);

CREATE TABLE Courses (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES Users(id)
);

CREATE TABLE Lectures (
    id INT IDENTITY(1,1) PRIMARY KEY,
    course_id INT,
    title NVARCHAR(100) NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE Assignments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    course_id INT,
    lecture_id INT,
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    due_date DATETIME,
    status VARCHAR(10) CHECK (status IN ('not yet', 'in progress', 'ended')) DEFAULT 'not yet',
    FOREIGN KEY (course_id) REFERENCES Courses(id),
    FOREIGN KEY (lecture_id) REFERENCES Lectures(id)
);

CREATE TABLE Submissions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    assignment_id INT,
    student_id INT,
    file_url VARCHAR(255),
    submission_date DATETIME,
    grade DECIMAL(4,2),
    FOREIGN KEY (assignment_id) REFERENCES Assignments(id),
    FOREIGN KEY (student_id) REFERENCES Users(id)
);

CREATE TABLE Enrollments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATETIME DEFAULT GETDATE(),
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

-- Sample data: 5 students, 4 teachers, 1 admin (Vietnamese names and schools)

CREATE FUNCTION GetEnrollmentCount(@courseId INT)
RETURNS INT
AS
BEGIN
    DECLARE @cnt INT;
    SELECT @cnt = COUNT(*) FROM Enrollments WHERE course_id = @courseId;
    RETURN @cnt;
END
GO

CREATE TRIGGER PreventDuplicateEnrollment
ON Enrollments
INSTEAD OF INSERT
AS
BEGIN
    IF EXISTS (
        SELECT 1 FROM inserted i
        JOIN Enrollments e ON i.student_id = e.student_id AND i.course_id = e.course_id
    )
    BEGIN
        RAISERROR('Student is already enrolled in this course.', 16, 1);
        RETURN;
    END

    INSERT INTO Enrollments (student_id, course_id, enrollment_date, status)
    SELECT student_id, course_id, enrollment_date, status
    FROM inserted;
END
GO

CREATE PROCEDURE CompleteEnrollment
    @enrollmentId INT
AS
BEGIN
    UPDATE Enrollments
    SET status = 'completed'
    WHERE id = @enrollmentId;
END
GO

CREATE TRIGGER UpdateDurationOnLogout
ON UserActivity
AFTER UPDATE
AS
BEGIN
    UPDATE ua
    SET duration_minutes = DATEDIFF(MINUTE, ua.login_time, ua.logout_time)
    FROM UserActivity ua
    INNER JOIN inserted i ON ua.id = i.id
    WHERE i.logout_time IS NOT NULL AND ua.logout_time IS NOT NULL;
END
GO

CREATE PROCEDURE GetStudentGrades
    @courseId INT
AS
BEGIN
    SELECT u.id AS student_id, u.username, a.title, s.grade
    FROM Users u
    JOIN Enrollments e ON u.id = e.student_id
    JOIN Assignments a ON a.course_id = e.course_id
    LEFT JOIN Submissions s ON s.assignment_id = a.id AND s.student_id = u.id
    WHERE e.course_id = @courseId;
END
GO

CREATE PROCEDURE UpdateAssignmentDueDate
    @AssignmentId INT,
    @NewDueDate DATETIME
AS
BEGIN
    UPDATE Assignments
    SET due_date = @NewDueDate
    WHERE id = @AssignmentId;
END
GO

CREATE TRIGGER trg_PreventLateSubmission
ON Submissions
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO Submissions (assignment_id, student_id, file_url, submission_date, grade)
    SELECT i.assignment_id, i.student_id, i.file_url, i.submission_date, i.grade
    FROM inserted i
    JOIN Assignments a ON i.assignment_id = a.id
    WHERE i.submission_date <= a.due_date;

    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN Assignments a ON i.assignment_id = a.id
        WHERE i.submission_date > a.due_date
    )
    BEGIN
        RAISERROR('Cannot submit after assignment due date.', 16, 1);
    END
END
GO

-- Courses
INSERT INTO Courses (name, description, teacher_id) VALUES
('Introduction to Programming', 'Learn the basics of coding with this beginner-friendly course.', 21),
('Web Development Basics', 'Build your first website with HTML, CSS, and JavaScript.', 21),
('Data Science Fundamentals', 'Explore data analysis and visualization techniques.', 21);

-- Example thumbnails (add a 'thumbnail' column to Courses if needed)
UPDATE Courses SET thumbnail = 'thumbnail1.png' WHERE name = 'Introduction to Programming';
UPDATE Courses SET thumbnail = 'thumbnail2.png' WHERE name = 'Web Development Basics';
UPDATE Courses SET thumbnail = 'thumbnail3.png' WHERE name = 'Data Science Fundamentals';

-- Lessons for each course
INSERT INTO Lectures (course_id, title, video_url) VALUES
(7, 'Getting Started with Programming', 'https://video.example.com/programming_intro.mp4'),
(7, 'Variables and Data Types', 'https://video.example.com/variables.mp4'),
(7, 'Control Structures', 'https://video.example.com/control_structures.mp4'),
-- ... add up to 12 lessons for course 1

(8, 'HTML Basics', 'https://video.example.com/html_basics.mp4'),
(8, 'CSS Fundamentals', 'https://video.example.com/css_fundamentals.mp4'),
(8, 'JavaScript Introduction', 'https://video.example.com/js_intro.mp4'),
-- ... add up to 15 lessons for course 2

(9, 'Introduction to Data Science', 'https://video.example.com/ds_intro.mp4'),
(9, 'Data Visualization', 'https://video.example.com/data_viz.mp4'),
(9, 'Basic Statistics', 'https://video.example.com/statistics.mp4');
-- ... add up to 10 lessons for course 3

-- Example: Enrollments and Progress (for progress bar)
-- Assume student_id = 2 is logged in
INSERT INTO Enrollments (student_id, course_id, enrollment_date, status) VALUES
(2, 1, GETDATE(), 'enrolled'),
(2, 2, GETDATE(), 'enrolled'),
(2, 3, GETDATE(), 'enrolled');

-- Example: Submissions for completed lessons (for progress calculation)
-- For course 1 (12 lessons, 6 completed = 50%)
INSERT INTO Submissions (assignment_id, student_id, file_url, submission_date, grade) VALUES
(1, 2, 'file1.pdf', GETDATE(), 9.0),
(2, 2, 'file2.pdf', GETDATE(), 8.5);
-- ... add 6 submissions for course 1

-- For course 2 (15 lessons, 3 completed = 20%)
-- ... add 3 submissions for course 2

-- For course 3 (10 lessons, 7 completed = 70%)
-- ... add 7 submissions for course 3
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


