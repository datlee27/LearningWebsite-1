<%@ page import="java.util.List" %>
<%
    // Assume courses and lectures are provided by your servlet/controller
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    List<Lecture> lectures = (List<Lecture>) request.getAttribute("lectures");
    String selectedCourseId = request.getParameter("courseId");
%>
<html>
<head>
    <title>Manage Lectures</title>
    <!-- Add Bootstrap CSS and JS for modals if desired -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"/>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</head>
<body>
    <!-- Courses Dropdown -->
    <form method="get" action="addLectures.jsp">
        <label for="courseDropdown">Select Course:</label>
        <select name="courseId" id="courseDropdown" onchange="this.form.submit()">
            <option value="">-- Select --</option>
            <% for(Course c : courses) { %>
                <option value="<%= c.getId() %>" <%= c.getId().equals(selectedCourseId) ? "selected" : "" %>>
                    <%= c.getName() %>
                </option>
            <% } %>
        </select>
    </form>
    
    <!-- Lectures Table -->
    <% if(selectedCourseId != null && !selectedCourseId.isEmpty()) { %>
        <h3>Lectures for Course: <%= selectedCourseId %></h3>
        <button data-toggle="modal" data-target="#addLectureModal">Add Lecture</button>
        <table class="table">
            <tr>
                <th>Lecture ID</th>
                <th>Lecture Name</th>
                <th>Actions</th>
            </tr>
            <% for(Lecture l : lectures) { %>
                <tr>
                    <td><%= l.getId() %></td>
                    <td><%= l.getName() %></td>
                    <td>
                        <!-- Edit -->
                        <button data-toggle="modal" data-target="#editLectureModal<%= l.getId() %>">Edit</button>
                        <!-- Delete -->
                        <form method="post" action="deleteLecture" style="display:inline;">
                            <input type="hidden" name="lectureId" value="<%= l.getId() %>"/>
                            <input type="hidden" name="courseId" value="<%= selectedCourseId %>"/>
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
                <!-- Edit Modal for this lecture -->
                <div class="modal fade" id="editLectureModal<%= l.getId() %>" tabindex="-1">
                  <div class="modal-dialog">
                    <form method="post" action="editLecture">
                      <input type="hidden" name="lectureId" value="<%= l.getId() %>"/>
                      <input type="hidden" name="courseId" value="<%= selectedCourseId %>"/>
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5>Edit Lecture</h5>
                          <button type="button" data-dismiss="modal">&times

