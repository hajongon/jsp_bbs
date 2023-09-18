package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	// 데이터베이스 접근할 수 있게 해주는 객체
	private Connection conn;
	private PreparedStatement pstmt;
	// 정보를 담을 수 있는 객체
	private ResultSet rs;
	
	// 생성자
	// mysql에 접속을 하는 부분
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword From USER WHERE userID = ?";
		try {
			// SQL 인젝션과 같은 해킹 기법 방어 수단 PreparedStatement
			// 문장을 미리 준비해놓고 "?" 자리에 userID를 넣는다.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			// ResultSet에 쿼리를 실행한 결과를 넣는다.
			rs = pstmt.executeQuery();
			// 결과가 존재하면
			if (rs.next()) {
				// set 했던 값 (1번째 물음표에 입력된 userId로 SELECT한 pw)이 
				// input에 입력했던 userPassword와 같으면
				if(rs.getString(1).equals(userPassword)) {
					return 1; // 로그인 성공
				}
				else 
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;	// 데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}
