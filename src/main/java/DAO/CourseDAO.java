package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Assignment;
import model.Course;
import model.Lecture;

public class CourseDAO {
    private final DBConnection DBConnection = new DBConnection();
    private static final Logger logger = Logger.getLogger(CourseDAO.class.getName());

     public int saveCourse(String name, String description, int teacherId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO learning_management.Courses (name, description, teacher_id) VALUES (?, ?, ?);";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, teacherId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating course failed, no rows affected.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Creating course failed, no ID obtained.");
                }
            }
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public List<Course> getCourses() throws SQLException, Exception {
    List<Course> courses = new ArrayList<>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DBConnection.getConnection();
        String sql = "SELECT id, name, description, teacher_id FROM learning_management.Courses;";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Course course = new Course(
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("teacher_id")
            );
            course.setIdCourse(rs.getInt("id"));  // ✅ sửa đúng tên cột
            courses.add(course);
        }

        for (Course course : courses) {
            // Get Lectures
            sql = "SELECT * FROM learning_management.Lectures WHERE course_id = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, course.getIdCourse());
            rs = pstmt.executeQuery();
            List<Lecture> lectures = new ArrayList<>();
            while (rs.next()) {
                Lecture lecture = new Lecture(course.getIdCourse(), rs.getString("title"), rs.getString("video_url"), rs.getString("status"));
                lecture.setIdLecture(rs.getInt("idLecture"));
                lectures.add(lecture);
            }
            course.setLectures(lectures);

            // Get Assignments
            sql = "SELECT * FROM learning_management.Assignments WHERE course_id = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, course.getIdCourse());
            rs = pstmt.executeQuery();
            List<Assignment> assignments = new ArrayList<>();
            while (rs.next()) {
                Assignment assignment = new Assignment(
                    course.getIdCourse(),
                    rs.getInt("id_lecture") > 0 ? rs.getInt("id_lecture") : null,
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null,
                    rs.getString("status")
                );
                assignment.setIdAss(rs.getInt("idAss"));
                assignments.add(assignment);
            }
            course.setAssignments(assignments);
        }
        return courses;
    } finally {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) DBConnection.closeConnection(conn);
    }
}

    // Add more course-related functions if needed
}