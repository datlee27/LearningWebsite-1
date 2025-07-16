package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ActivityDAO {
    private final DBConnection DBConnection = new DBConnection();
    private static final Logger logger = Logger.getLogger(ActivityDAO.class.getName());

    public void logLogin(String username) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO user_activity (username, login_time) VALUES (?, NOW());";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } finally {
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

    public void logLogout(String username) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE user_activity SET logout_time = NOW(), duration_minutes = TIMESTAMPDIFF(MINUTE, login_time, NOW()) WHERE username = ? AND logout_time IS NULL;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } finally {
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

    public Map<String, Integer> getUserActivity(String username) throws SQLException, Exception {
        Map<String, Integer> onlineTimes = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT DATE(login_time) as activity_date, COALESCE(SUM(duration_minutes), 0) as total_minutes FROM user_activity WHERE username = ? AND login_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) GROUP BY DATE(login_time);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("activity_date");
                int minutes = rs.getInt("total_minutes");
                onlineTimes.put(date, minutes);
            }
            return onlineTimes;
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
    }

    public int getTotalActivityForDate(String username, String date) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COALESCE(SUM(duration_minutes), 0) as total_minutes FROM user_activity WHERE username = ? AND DATE(login_time) = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, date);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_minutes");
            }
            return 0;
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
    }
}