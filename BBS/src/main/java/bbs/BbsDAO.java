package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	// 데이터베이스 접근할 수 있게 해주는 객체
	private Connection conn;
	// 정보를 담을 수 있는 객체
	private ResultSet rs;
	
	// 생성자
	// mysql에 접속을 하는 부분
	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3307/BBS?useSSL=false&characterEncoding=UTF-8";
			String dbID = "root";
			String dbPassword = "root";
			// mysql에 접속할 수 있도록 해주는 매개체 역할을 하는 라이브러리
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public String getDate() {
		// 현재 시각 가져오기
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			return ""; // 데이터베이스 오류
		}
	}
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
			return -1; // 데이터베이스 오류
		}
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS (bbsID, bbsTitle, userID, bbsDate, bbsContent, bbsAvailable) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); // bbsAvailable 삭제되지 않은 상태니까 1 입력
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1; // 데이터베이스 오류
		}
		
	}
	
	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			// 예를 들어 게시글이 현재 99개라고 하자.
			// getNext() 는 100.
			// 3page를 get한다.
			// pageNumber == 3
			// (3 - 1) * 10 == 20
			// 100 - 20 == 80
			
			// *** 80보다 id가 작은 것 최대 10개를 get
			
			// *** 왜 getNext() 에서 빼냐고?
			// 가장 id가 높은것부터 내림차순으로 보이는 거니까!
			// 1페이지에는 99 ~ 90
			// 2페이지에는 89 ~ 80
			// 3페이지에는 79 ~ 70 이 나와야지
		
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			// 하나씩 다음으로 넘기면서
			while (rs.next()) {
				// bbs 새로 만들고
				Bbs bbs = new Bbs();
				// SELECT로 뽑아온 데이터 하나씩 set
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				// list에 추가
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// nextPage는 페이징 처리를 위해 존재하는 함수
	// '다음'이라는 버튼이 존재해야 하는지 체크
	// 입력된 페이지가 존재하는지의 여부를 판단
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			// 결과가 하나라도 존재한다면
			if (rs.next()) {
				// '다음' 페이지로 넘어갈 수 있다.
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			// 결과가 하나라도 존재한다면
			if (rs.next()) {
				// '다음' 페이지로 넘어갈 수 있다.
				Bbs bbs = new Bbs();
				// SELECT로 뽑아온 데이터 하나씩 set
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			// SQL 문 실행 결과로, 데이터베이스에서 영향을 받은 행의 수를 나타내는 정수 값을 반환
			return pstmt.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace();
			return -1; // 데이터베이스 오류
		}
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			// SQL 문 실행 결과로, 데이터베이스에서 영향을 받은 행의 수를 나타내는 정수 값을 반환
			return pstmt.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace();
			return -1; // 데이터베이스 오류
		}
	}
}
