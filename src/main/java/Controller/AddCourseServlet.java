package Controller;

import DAO.DAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AddCourseServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddCourseServlet.class.getName());
    private final DAO dao = new DAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/view/signIn.jsp");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int teacherId = Integer.parseInt(request.getParameter("teacher_id"));

        if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Course name and description are required.");
            request.getRequestDispatcher("/view/addCourses.jsp").forward(request, response);
            return;
        }

        try {
            int courseId = dao.saveCourse(name, description, teacherId); // Get the generated course ID
            logger.info("Course added successfully by teacher ID: " + teacherId + ", Course ID: " + courseId);
            response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp?courseId=" + courseId);
        } catch (SQLException e) {
            logger.severe("Database error while adding course: " + e.getMessage());
            request.setAttribute("error", "Failed to add course: " + e.getMessage());
            request.getRequestDispatcher("/view/addCourses.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Unexpected error while adding course: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/addCourses.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/addCourses.jsp");
    }
}