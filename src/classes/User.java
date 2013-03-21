package classes;

// this class is modeling the user object
public class User {
	
	// public constructor for the user object
	public User(String username, String password,String userType){
		this.username = username;
		this.password = password;
		this.userType = userType;
	}
	
	// private fields
	private String username;
	private String password;
	private String userType;
	
	// public getters for the fields
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUserType() {
		return userType;
	}
	
	// public static fields which represents the type of the user
	public static String BUYER_TYPE = "Buyer";
	public static String SELLER_TYPE = "Seller";
}
