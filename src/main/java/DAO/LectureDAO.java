package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Lecture;

public class LectureDAO {
    private final DBConnection DBConnection = new DBConnection();
    private static final Logger logger = Logger.getLogger(LectureDAO.class.getName());

    public int saveLecture(int courseId, String title, String content, String videoUrl) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO learning_management.Lectures (course_id, title, content, video_url, status) VALUES (?, ?, ?, ?, 'active');";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, courseId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, videoUrl != null ? videoUrl : "");
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating lecture failed, no rows affected.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Creating lecture failed, no ID obtained.");
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

    public List<Lecture> getLecturesByCourse(int courseId) throws SQLException, Exception {
        List<Lecture> lectures = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM learning_management.Lectures WHERE course_id = ? AND status = 'active';";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Lecture lecture = new Lecture();
                lecture.setIdLecture(rs.getInt("id"));
                lecture.setIdCourse(rs.getInt("course_id"));
                lecture.setTitle(rs.getString("title"));
                lecture.setVideoUrl(rs.getString("video_url"));
                lectures.add(lecture);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
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
        return lectures;
    }
}