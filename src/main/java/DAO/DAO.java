package DAO;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt; // Thêm dependency

public class DAO {
    private DBcontext dbContext = new DBcontext();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void save(User user) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            String sql = "INSERT INTO learning_management.Users (username, password, email, role, google_id) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Đã để trống cho Google
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getGoogleId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
    }

    public User authenticate(String identifier, String password, String googleIdToken) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            if (googleIdToken != null) {
                String googleId = verifyGoogleToken(googleIdToken); // Thay placeholder
                String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE google_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, googleId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return mapToUser(rs);
                }
            } else {
                String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE (username = ? OR email = ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, identifier);
                pstmt.setString(2, identifier);
                rs = pstmt.executeQuery();
                if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                    return mapToUser(rs);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
        return null;
    }

    public User findByGoogleId(String googleId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE google_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, googleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
        return null;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setGoogleId(rs.getString("google_id"));
        return user;
    }

    public String verifyGoogleToken(String idTokenString) {
        // Placeholder; cần tích hợp thực tế với Google API
        return "dummyGoogleId"; // Thay bằng logic từ LoginServlet
    }
}