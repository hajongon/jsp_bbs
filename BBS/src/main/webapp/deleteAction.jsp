<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 우리가 만든 클래스 임포트 --%> 
<%@ page import="bbs.Bbs" %>
<%@ page import="bbs.BbsDAO" %>
<%-- 자바스크립트 사용 위해 --%>
<%@ page import="java.io.PrintWriter" %>
<%-- 건너오는 데이터 인코딩 --%>
<% request.setCharacterEncoding("UTF-8"); %>
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
		if (userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인이 필요합니다.');");
			// 이전 페이지로 이동
			script.println("location.href = 'login.jsp'");
			script.println("</script>"); 
		}
		
		// 유효한 글인지 체크
		int bbsID = 0 ;
		if (request.getParameter("bbsID") != null) {
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		if (bbsID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 접근입니다.');");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>"); 
		}
		Bbs bbs = new BbsDAO().getBbs(bbsID);
		if (!userID.equals(bbs.getUserID())) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.');");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>"); 
		} else {
			// input type text name="bbsTitle"이 존재하고 submit을 하면
			// request.getParameter("bbsTitle")로 뽑아낼 수 있음

			BbsDAO bbsDAO = new BbsDAO();
			int result = bbsDAO.delete(bbsID);
			if (result == -1) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('글 삭제에 실패했습니다.');");
				script.println("history.back()");
				script.println("</script>");
			}
			else {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'bbs.jsp'");
				script.println("</script>");
			}	
		}

		
	%>
</body>
</html>