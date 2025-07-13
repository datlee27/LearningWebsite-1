<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.DAO" %>
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        DAO dao = new DAO();
        try {
            dao.logLogout(username); // Ghi nhận thời gian đăng xuất
        } catch (Exception e) {
            out.println("<p>Error logging out: " + e.getMessage() + "</p>");
        }
    }
    session.invalidate(); // Hủy session
    response.sendRedirect(request.getContextPath() + "/view/homePage.jsp"); // Chuyển hướng về trang chủ
%>