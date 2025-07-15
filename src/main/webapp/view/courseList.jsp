<%@ page import="java.util.List" %>
<%@ page import="your.package.Course" %>
<%
    String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Courses</title>
    <style>
        .course-card {
            padding: 20px;
            margin: 15px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
            background: #f9f9f9;
        }
        .progress-bar-bg {
            width: 100%;
            background: #eee;
            border-radius: 5px;
            margin: 10px 0;
        }
        .progress-bar-fill {
            height: 20px;
            background: #4caf50;
            border-radius: 5px;
            text-align: center;
            color: #fff;
        }
        .teacher-panel {
            display: flex;
            gap: 30px;
            margin-top: 40px;
        }
        .panel-card {
            background: #e3e3ff;
            padding: 30px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,.08);
            flex: 1;
        }
        .panel-card a {
            display: block;
            margin-top: 10px;
            font-weight: bold;
            color: #333;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <h1>My Courses</h1>

    <% if ("student".equals(role)) { 
        List<Course> courses = (List<Course>) session.getAttribute("enrolledCourses");
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) { %>
                <div class="course-card">
                    <h1><%= course.getName() %></h1>
                    <h2>Due: <%= course.getDueDate() %></h2>
                    <div class="progress-bar-bg">
                        <div class="progress-bar-fill" style="width:<%= course.getProgress() %>%;">
                            <%= course.getProgress() %>%
                        </div>
                    </div>
                </div>
        <%  }
        } else { %>
            <p>You are not enrolled in any courses.</p>
        <% }
    } else if ("teacher".equals(role)) { %>
        <div class="teacher-panel">
            <div class="panel-card">
                <span>Courses</span>
                <a href="manageCourses.jsp">Go to Courses Management</a>
            </div>
            <div class="panel-card">
                <span>Lectures</span>
                <a href="manageLectures.jsp">Go to Lectures Management</a>
            </div>
            <div class="panel-card">
                <span>Assignments</span>
                <a href="editAssignments.jsp">Go to Assignments Edit</a>
            </div>
        </div>
    <% } else { %>
        <p>Unknown user role.</p>
    <% } %>
</body>
</html>
