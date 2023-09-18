<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 우리가 만든 클래스 임포트 --%> 
<%@ page import="user.UserDAO" %>
<%-- 자바스크립트 사용 위해 --%>
<%@ page import="java.io.PrintWriter" %>
<%-- 건너오는 데이터 인코딩 --%>
<% request.setCharacterEncoding("UTF-8"); %>
<%-- 자바 빈 객체를 생성하고 이 빈 객체에 user 정보 설정 --%>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID" /> 
<jsp:setProperty name="user" property="userPassword" /> 
<jsp:setProperty name="user" property="userName" /> 
<jsp:setProperty name="user" property="userGender" /> 
<jsp:setProperty name="user" property="userEmail" /> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹사이트</title>
</head>
<body>
	<%
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if (userID != null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인되어 있습니다.');");
			// 이전 페이지로 이동
			script.println("location.href = 'main.jsp'");
			script.println("</script>"); 
		}
		if (user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null 
			|| user.getUserGender() == null || user.getUserEmail() == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력되지 않은 사항이 있습니다.');");
			script.println("history.back()");
			script.println("</script>");
		} else {
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			// id는 PRIMARY KEY이므로 아이디 중복이면 데이터베이스 오류
			if (result == -1) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.');");
				script.println("history.back()");
				script.println("</script>");
			}
			else {
				session.setAttribute("userID", user.getUserID());
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'main.jsp'");
				script.println("</script>");
			}
		}
	%>
</body>
</html>