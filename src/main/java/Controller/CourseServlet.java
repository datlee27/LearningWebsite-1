package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "Course", urlPatterns = {"/course"})
public class CourseServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CourseServlet.class.getName());
    private final CourseDAO dao = new CourseDAO();

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
           int newCourseId = dao.saveCourse(name, description, teacherId);
            logger.info("Course added successfully by teacher ID: " + teacherId);
            // Redirect kèm courseId sang addLectures.jsp
            response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp?courseId=" + newCourseId);

        } catch (SQLException e) {
            logger.severe("Database error while adding course: " + e.getMessage());
            request.setAttribute("error", "Failed to add course: " + e.getMessage());
            request.getRequestDispatcher("/view/addCourses.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CourseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/addCourses.jsp");
    }
}