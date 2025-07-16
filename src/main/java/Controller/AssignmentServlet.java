package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dao.AssignmentDAO;
import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Assignment;
import model.Course;

@WebServlet(name = "AssignmentServlet", urlPatterns = {"/assignments"})
public class AssignmentServlet extends HttpServlet {
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        List<Assignment> assignments = assignmentDAO.getAssignmentsByCourse(courseId);
        request.setAttribute("assignments", assignments);
        request.setAttribute("courseId", courseId);
        request.getRequestDispatcher("/view/assignments.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "create";
        
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        try {
            switch (action) {
                case "update":
                    updateAssignment(request);
                    break;
                case "delete":
                    deleteAssignment(request);
                    break;
                default:
                    createAssignment(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error processing assignment action: " + action, e);
        }
        response.sendRedirect(request.getContextPath() + "/assignments?courseId=" + courseId);
    }

    private void createAssignment(HttpServletRequest request) throws ServletException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDateTime dueDate = LocalDateTime.parse(request.getParameter("dueDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Course course = courseDAO.findCourseById(courseId)
                .orElseThrow(() -> new ServletException("Course not found"));
        
        Assignment assignment = new Assignment(course, null, title, description, dueDate, "active");
        assignmentDAO.saveAssignment(assignment);
    }

    private void updateAssignment(HttpServletRequest request) throws ServletException {
        int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDateTime dueDate = LocalDateTime.parse(request.getParameter("dueDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId)
                .orElseThrow(() -> new ServletException("Assignment not found"));
        
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignmentDAO.updateAssignment(assignment);
    }

    private void deleteAssignment(HttpServletRequest request) {
        int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
        assignmentDAO.deleteAssignment(assignmentId);
    }
}
