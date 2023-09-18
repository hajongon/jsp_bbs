<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 우리가 만든 클래스 임포트 --%> 
<%@ page import="user.UserDAO" %>
<%-- 자바스크립트 사용 위해 --%>
<%@ page import="java.io.PrintWriter" %>
<%-- 건너오는 데이터 인코딩 --%>
<% request.setCharacterEncoding("UTF-8"); %>
<%-- 자바 빈 객체를 생성하고 이 빈 객체에 userID와 userPassword 값 설정 --%>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID" /> 
<jsp:setProperty name="user" property="userPassword" /> 
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
		UserDAO userDAO = new UserDAO();
		int result = userDAO.login(user.getUserID(), user.getUserPassword());
		if (result == 1) {
			// 세션 id 부여
			session.setAttribute("userID", user.getUserID());
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		}
		else if (result == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호를 확인해주세요.');");
			// 이전 페이지로 이동
			script.println("history.back()");
			script.println("</script>");
		}
		else if (result == -1) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('존재하지 않는 아이디입니다.');");
			script.println("history.back()");
			script.println("</script>");
		}
		else if (result == -2) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('데이터베이스 오류가 발생했습니다.');");
			script.println("history.back()");
			script.println("</script>");
		}
	%>
</body>
</html>