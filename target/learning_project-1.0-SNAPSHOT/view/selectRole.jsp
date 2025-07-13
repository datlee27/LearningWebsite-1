<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Select Role - Learning Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/selectRole.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="card-container">  
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            <div class="role-card">
                <h3>Student</h3>
                <p>Explore courses, access learning materials, and track your progress as a student.</p>
                <form action="${pageContext.request.contextPath}/updateRoleServlet" method="post">
                    <input type="hidden" name="role" value="student">
                    <button type="submit" class="btn">Select Student</button>
                </form>
            </div>
            <div class="role-card">
                <h3>Teacher</h3>
                <p>Create, manage, and share courses to inspire and educate learners.</p>
                <form action="${pageContext.request.contextPath}/updateRoleServlet" method="post">
                    <input type="hidden" name="role" value="teacher">
                    <button type="submit" class="btn">Select Teacher</button>
                </form>
            </div>
        </div>
        <p class="text-center mt-4">Already have a role? <a href="${pageContext.request.contextPath}/loginServlet">Sign In Again</a></p>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
