CREATE DATABASE learning_management;

USE learning_management;

CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender ENUM('male', 'female', 'other'),
    phone VARCHAR(20),
    school VARCHAR(100),
    role ENUM('student', 'teacher', 'admin') NOT NULL
);

CREATE TABLE Courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES Users(id)
);

CREATE TABLE Lectures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    title VARCHAR(100) NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    FOREIGN KEY (course_id) REFERENCES Courses(id)
);

CREATE TABLE Assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    lecture_id INT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATETIME,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
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
    status ENUM('enrolled', 'completed', 'dropped') DEFAULT 'enrolled',
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
INSERT INTO Users (username, password, email, first_name, last_name, date_of_birth, gender, phone, school, role) VALUES
('admin_qnnguyen', 'A8d!nU2i4@', 'quangnm@viettel.com.vn', 'Nguyễn Mạnh', 'Quang','1980-01-01', 'male', '0909000000', 'Viettel', 'admin'),
('mt_haminh', 'M!nHk2024#', 'mhminh@hust.edu.vn', 'Lê Minh',  'Hà','2003-03-15', 'female', '0901000001', 'Đại học Bách Khoa Hà Nội', 'student'),
('ap_phamanh', 'A7pN!eu@', 'apmanh@neu.edu.vn', 'Phạm', 'Hoàng Anh', '2002-07-22', 'male', '0901000002', 'Đại học Kinh tế Quốc dân', 'student'),
('hl_lehoa', 'H0aL#ftu$', 'hlhoa@ftu.edu.vn', 'Lê', 'Thanh Hoa', '2001-11-05', 'female', '0901000003', 'Đại học Ngoại thương', 'student'),
('tv_vutuan', 'TuanV!uet*', 'tvvtuan@uet.edu.vn', 'Vũ', 'Mạnh Tuấn', '2004-01-30', 'male', '0901000004', 'Đại học Công nghệ', 'student'),
('lh_hoanglan', 'L@nHmu2024!', 'lhlan@hmu.edu.vn', 'Hoàng', 'Thị Lan', '2003-09-18', 'female', '0901000005', 'Đại học Y Hà Nội', 'student'),
('hn_nguyenhieu', 'H!euN@hust$', 'hnhieu@hust.edu.vn', 'Nguyễn', 'Mạnh Hiếu', '1985-04-10', 'male', '0902000001', 'Đại học Bách Khoa Hà Nội', 'teacher'),
('mt_tranmai', 'M@iT!neu#', 'mtmai@neu.edu.vn', 'Trần', 'Hoàng Mai', '1983-08-25', 'female', '0902000002', 'Đại học Kinh tế Quốc dân', 'teacher'),
('sp_phamson', 'S0nP@ftu$', 'spson@ftu.edu.vn', 'Phạm', 'Ngọc Sơn', '1987-12-12', 'male', '0902000003', 'Đại học Ngoại thương', 'teacher'),
('tl_lethao', 'Th@oL!uet*', 'tlthao@uet.edu.vn', 'Lê', 'Thị Thảo', '1984-06-30', 'female', '0902000004', 'Đại học Công nghệ', 'teacher')

-- Sample Courses
INSERT INTO Courses (name, description, teacher_id) VALUES
('Lập trình Python', 'Khóa học lập trình Python cơ bản', 7),
('SQL Cơ bản', 'Khóa học về cơ sở dữ liệu và SQL', 9),
('Trí tuệ nhân tạo', 'Giới thiệu về AI và Machine Learning', 8);

-- Sample Lectures
INSERT INTO Lectures (course_id, title, video_url, status) VALUES
(1, 'Biến và kiểu dữ liệu', 'https://video.hocmai.vn/python_variables.mp4', 'approved'),
(2, 'Giới thiệu về SQL', 'https://video.hocmai.vn/sql_intro.mp4', 'approved'),
(2, 'Truy vấn dữ liệu', 'https://video.hocmai.vn/sql_query.mp4', 'approved'),
(3, 'Giới thiệu về AI', 'https://video.hocmai.vn/ai_intro.mp4', 'approved'),
(3, 'Machine Learning cơ bản', 'https://video.hocmai.vn/ml_basic.mp4', 'approved');

-- Sample Assignments
INSERT INTO Assignments (course_id, lecture_id, title, description, due_date, status) VALUES
(1, 1, 'Bài tập Python', 'Viết chương trình Python tính tổng các số từ 1 đến 100', '2024-06-24 23:59:59', 'approved'),
(2, 2, 'Bài tập SQL', 'Viết truy vấn SQL lấy danh sách học sinh', '2024-06-25 23:59:59', 'approved'),
(3, 3, 'Bài tập AI', 'Trình bày ứng dụng AI trong thực tế', '2024-06-26 23:59:59', 'approved');

-- Sample Enrollments
INSERT INTO Enrollments (student_id, course_id, enrollment_date, status) VALUES
(2, 1, '2024-06-01 08:00:00', 'enrolled'),
(3, 1, '2024-06-01 08:05:00', 'enrolled'),
(4, 2, '2024-06-02 09:00:00', 'enrolled'),
(5, 3, '2024-06-03 10:00:00', 'enrolled'),
(6, 2, '2024-06-04 11:00:00', 'enrolled'),
(2, 3, '2024-06-05 12:00:00', 'enrolled'),
(3, 1, '2024-06-06 13:00:00', 'enrolled'),
(4, 2, '2024-06-07 14:00:00', 'enrolled');

-- Sample Submissions
INSERT INTO Submissions (assignment_id, student_id, file_url, submission_date, grade) VALUES
(1, 2, 'https://files.hocmai.vn/python_assignment.pdf', '2024-06-14 16:00:00', 9.5),
(2, 3, 'https://files.hocmai.vn/sql_assignment.pdf', '2024-06-15 17:00:00', 8.0),
(3, 4, 'https://files.hocmai.vn/ai_assignment.pdf', '2024-06-16 18:00:00', 9.0);

GO
-- MySQL does not support table-valued parameters or T-SQL style triggers/procedures.
-- Below are MySQL-compatible versions.

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

