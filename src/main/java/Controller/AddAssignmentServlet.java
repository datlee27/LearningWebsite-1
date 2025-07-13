package Controller;

import DAO.DAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class AddAssignmentServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddAssignmentServlet.class.getName());
    private final DAO dao = new DAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/view/signIn.jsp");
            return;
        }

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDate = request.getParameter("dueDate"); // Format: yyyy-MM-ddTHH:mm

        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() || dueDate == null) {
            request.setAttribute("error", "Assignment title, description, and due date are required.");
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
            return;
        }

        try {
            // Convert dueDate string to proper format if needed
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            sdf.setLenient(false);
            dao.saveAssignment(courseId, title, description, dueDate);
            logger.info("Assignment added successfully for course ID: " + courseId);
            response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp?courseId=" + courseId + "&success=true");
        } catch (SQLException e) {
            logger.severe("Database error while adding assignment: " + e.getMessage());
            request.setAttribute("error", "Failed to add assignment: " + e.getMessage());
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
        } catch (Exception e) {
            logger.severe("Unexpected error while adding assignment: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp");
    }
}