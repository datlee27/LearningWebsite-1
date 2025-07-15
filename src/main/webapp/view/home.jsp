<%@ page session="true" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Learning Website - Home</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .container { max-width: 900px; margin: 0 auto; padding: 40px 20px; }
        .intro { padding: 30px 0; text-align: center; }
        .buttons { margin: 20px 0; text-align: center; }
        .btn { display: inline-block; margin: 0 10px; padding: 10px 25px; background: #007bff; color: #fff; border: none; border-radius: 5px; text-decoration: none; font-size: 1.1em; cursor: pointer; }
        .popular-courses { margin: 40px 0 0 0; }
        .course-list { display: flex; flex-wrap: wrap; gap: 20px; justify-content: center; }
        .course-item { background: #f4f4f4; border-radius: 8px; padding: 18px; min-width: 220px; box-shadow: 0 2px 6px rgba(0,0,0,0.04); }
        h2 { margin-bottom: 22px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="intro">
            <h1>Welcome to Learning Website</h1>
            <p>
                Unlock your knowledge with our curated online courses.<br>
                Start your learning journey today!
            </p>
            <div class="buttons">
                <%-- Show buttons only if the user is NOT logged in --%>
                <%
                    Object user = session.getAttribute("user");
                    if (user == null) {
                %>
                    <a href="signup.jsp" class="btn">Sign Up</a>
                    <a href="login.jsp" class="btn">Login</a>
                <%
                    }
                %>
            </div>
        </div>
        <div class="popular-courses">
            <h2>Popular Courses</h2>
            <div class="course-list">
                <div class="course-item">
                    <strong>Java Programming Basics</strong><br>
                    Kickstart your coding journey with Java!
                </div>
                <div class="course-item">
                    <strong>Web Development Essentials</strong><br>
                    Learn HTML, CSS, and JavaScript for modern sites.
                </div>
                <div class="course-item">
                    <strong>Data Structures & Algorithms</strong><br>
                    Master the foundations for technical interviews.
                </div>
                <div class="course-item">
                    <strong>Database Management</strong><br>
                    Understand SQL and database design principles.
                </div>
            </div>
        </div>
    </div>
</body>
</html>
