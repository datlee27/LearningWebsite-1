<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${lecture.title}" /> - Learning Platform</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>

    <%@ include file="/WEB-INF/jsp/header.jspf" %>
    
    <div class="page-header">
        <div class="container">
            <p>Lecture</p>
            <h1><c:out value="${lecture.title}" /></h1>
        </div>
    </div>

    <main class="main-content">
        <div class="container">
            <div class="card">
                <c:if test="${sessionScope.user.role == 'TEACHER'}">
                    <div style="float: right;">
                        <a href="#" class="btn btn-secondary btn-sm">Edit Lecture</a>
                    </div>
                </c:if>
                
                <h2>Lecture Content</h2>
                <hr style="margin: 15px 0;">
                
                <%-- This is where your lecture content (text, video embed, etc.) would go --%>
                <div class="lecture-content">
                    <p>${lecture.content}</p>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="/WEB-INF/jsp/footer.jspf" %>

</body>
</html>
