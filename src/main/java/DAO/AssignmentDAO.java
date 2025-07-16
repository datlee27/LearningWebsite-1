package dao;

import model.Assignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.taglibs.standard.tag.common.core.CatchTag;
import java.util.logging.Logger;

public class AssignmentDAO {

    private final DBConnection DBConnection = new DBConnection();
    private static final Logger logger = Logger.getLogger(AssignmentDAO.class.getName());

    public int saveAssignment(int courseId, Integer idLecture, String title, String description, String dueDate) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO learning_management.Assignments (course_id, id_lecture, title, description, due_date, status) VALUES (?, ?, ?, ?, ?, 'active');";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, courseId);
            if (idLecture != null && idLecture > 0) {
                pstmt.setInt(2, idLecture);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setTimestamp(5, Timestamp.valueOf(dueDate));
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating assignment failed, no rows affected.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Creating assignment failed, no ID obtained.");
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

    public void updateAssignment(int assignmentId, int courseId, Integer idLecture, String title, String description, String dueDate) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE learning_management.Assignments SET course_id=?, id_lecture=?, title=?, description=?, due_date=? WHERE idAss=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            if (idLecture != null && idLecture > 0) {
                pstmt.setInt(2, idLecture);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setTimestamp(5, Timestamp.valueOf(dueDate));
            pstmt.setInt(6, assignmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to update assignment: " + e.getMessage());
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            } 
        }
    }

    public List<Assignment> getAssignmentsByLecture(int lectureId) throws SQLException, Exception {
        List<Assignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM learning_management.Assignments WHERE id_lecture = ? AND status = 'active';";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lectureId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setIdAss(rs.getInt("idAss"));
                assignment.setIdCourse(rs.getInt("course_id"));
                assignment.setIdLecture(rs.getObject("id_lecture", Integer.class));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null);
                assignment.setStatus(rs.getString("status"));
                assignments.add(assignment);
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
        return assignments;
    }

    public Assignment getAssignmentById(int assignmentId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Assignment assignment = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM learning_management.Assignments WHERE idAss = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, assignmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                assignment = new Assignment();
                assignment.setIdAss(rs.getInt("idAss"));
                assignment.setIdCourse(rs.getInt("course_id"));
                assignment.setIdLecture(rs.getObject("id_lecture", Integer.class));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null);
                assignment.setStatus(rs.getString("status"));
                return assignment;
            } else {
                throw new SQLException("No assignment found with ID: " + assignmentId);
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
    }
    

    public void deleteAssignment(int assignmentId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM learning_management.Assignments WHERE idAss=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, assignmentId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }
}
