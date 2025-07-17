package controller;

import java.io.IOException;
import java.util.List;

import dao.AssignmentDAO;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses"})
public class CourseServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        int userId = (int) request.getSession().getAttribute("id");
        if (role == null || !"teacher".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }        
        List<Course> courses = courseDAO.getCoursesByTeacher(userId);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/WEB-INF/courses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "create"; // Default action
        }

        try {
            switch (action) {
                case "update":
                    updateCourse(request);
                    break;
                case "delete":
                    deleteCourse(request);
                    break;
                default:
                    createCourse(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error processing course action: " + action, e);
        }
        response.sendRedirect(request.getContextPath() + "/courses");
    }

    private void createCourse(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        User user = (User) request.getSession().getAttribute("user");
        int teacherId = user.getId();

        String thumbnail = request.getParameter("thumbnail");
        Course course = new Course(name, description, teacherId, thumbnail);
        courseDAO.saveCourse(course);
    }

    private void updateCourse(HttpServletRequest request) {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        Course course = courseDAO.findCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + courseId));
        
        course.setName(name);
        course.setDescription(description);
        courseDAO.updateCourse(course);
    }

    private void deleteCourse(HttpServletRequest request) {
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        // Delete related enrollments
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        enrollmentDAO.deleteByCourseId(courseId);

        // Delete related assignments (if you have AssignmentDAO)
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        assignmentDAO.deleteByCourseId(courseId);

        // Delete the course itself
        courseDAO.deleteCourse(courseId);
    }
}
