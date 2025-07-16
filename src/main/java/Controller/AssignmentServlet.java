package controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import dao.AssignmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "Assignment", urlPatterns = {"/assignment"})
public class AssignmentServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AssignmentServlet.class.getName());
    private final AssignmentDAO dao = new AssignmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/view/signIn.jsp");
            return;
        }

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String idLectureParam = request.getParameter("lectureId");
        Integer idLecture = (idLectureParam != null && !idLectureParam.isEmpty()) ? Integer.parseInt(idLectureParam) : null;
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDate = request.getParameter("dueDate");
        String assignmentIdParam = request.getParameter("assignmentId");

        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || dueDate == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            if (assignmentIdParam != null && !assignmentIdParam.isEmpty()) {
                int assignmentId = Integer.parseInt(assignmentIdParam);
                dao.updateAssignment(assignmentId, courseId, idLecture, title, description, dueDate);
            } else {
                dao.saveAssignment(courseId, idLecture, title, description, dueDate);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/addAssignment.jsp");
    }
}