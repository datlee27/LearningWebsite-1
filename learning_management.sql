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
INSERT INTO Lectures (course_id, title, video_url) VALUES
(1, 'Biến và kiểu dữ liệu', 'https://video.hocmai.vn/python_variables.mp4'),
(2, 'Giới thiệu về SQL', 'https://video.hocmai.vn/sql_intro.mp4'),
(2, 'Truy vấn dữ liệu', 'https://video.hocmai.vn/sql_query.mp4'),
(3, 'Giới thiệu về AI', 'https://video.hocmai.vn/ai_intro.mp4'),
(3, 'Machine Learning cơ bản', 'https://video.hocmai.vn/ml_basic.mp4');

-- Sample Assignments
INSERT INTO Assignments (course_id, lecture_id, title, description, due_date, status) VALUES
(1, 1, 'Bài tập Python', 'Viết chương trình Python tính tổng các số từ 1 đến 100', '2024-06-24 23:59:59', 'in progress'),
(2, 2, 'Bài tập SQL', 'Viết truy vấn SQL lấy danh sách học sinh', '2024-06-25 23:59:59', 'in progress'),
(3, 3, 'Bài tập AI', 'Trình bày ứng dụng AI trong thực tế', '2024-06-26 23:59:59', 'in progress');

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


