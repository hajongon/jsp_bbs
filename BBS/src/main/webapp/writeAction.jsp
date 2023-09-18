<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 우리가 만든 클래스 임포트 --%> 
<%@ page import="bbs.BbsDAO" %>
<%-- 자바스크립트 사용 위해 --%>
<%@ page import="java.io.PrintWriter" %>
<%-- 건너오는 데이터 인코딩 --%>
<% request.setCharacterEncoding("UTF-8"); %>
<%-- 자바 빈 객체를 생성하고 이 빈 객체에 bbs 정보 설정 --%>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbs" property="bbsTitle" /> 
<jsp:setProperty name="bbs" property="bbsContent" /> 
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
		} else {
			if (bbs.getBbsTitle() == null || bbs.getBbsContent() == null) {
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('입력되지 않은 사항이 있습니다.');");
					script.println("history.back()");
					script.println("</script>");
				} else {
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.write(bbs.getBbsTitle(), userID, bbs.getBbsContent());
					if (result == -1) {
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('글쓰기에 실패했습니다.');");
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
		}

		
	%>
</body>
</html>