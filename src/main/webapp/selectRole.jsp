<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Select Role - LearnHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="auth-wrapper">
        <div class="auth-panel branding-panel">
            <h1>One Platform, Two Roles</h1>
            <p>Whether you're here to learn or to teach, we've got you covered.</p>
        </div>
        <div class="auth-panel form-panel">
            <h2>How will you be using LearnHub?</h2>
            <p class="form-intro">This helps us tailor your experience.</p>
            
            <div class="role-selection-panel">
                <a href="${pageContext.request.contextPath}/updateRole?role=STUDENT" class="role-card-link">
                    <div class="role-card">
                        <div class="icon"><i class="fas fa-user-graduate"></i></div>
                        <h3>Student</h3>
                        <p>Browse courses and submit assignments.</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/updateRole?role=TEACHER" class="role-card-link">
                    <div class="role-card">
                        <div class="icon"><i class="fas fa-chalkboard-teacher"></i></div>
                        <h3>Teacher</h3>
                        <p>Create courses and grade submissions.</p>
                    </div>
                </a>
            </div>

            <div class="form-footer">
                <p><a href="${pageContext.request.contextPath}/logout">Logout</a></p>
            </div>
        </div>
    </div>
</body>
</html>
