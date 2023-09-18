package user;
// 회원정보를 담을 수 있는 User 테이블 구축
// JavaBeans로 유저클래스 만듦
public class User {
	// DB의 컬럼명들을 작성한다고 생각하면 됨 
	private String userID;
	private String userPassword;
	private String userName;
	private String userGender;
	private String userEmail;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
}
