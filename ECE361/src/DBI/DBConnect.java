package DBI;

import java.security.MessageDigest;
import java.sql.*;

import org.apache.commons.codec.digest.DigestUtils;



public class DBConnect {
	
	private static String DBURL = "jdbc:mysql://199.98.20.120/SetGame";
	private static String DBUSERNAME = "root";
	private static String DBPASSWORD = "password";
	
	private Connection dbcon;
	private static Statement st;
	
	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbcon = DriverManager.getConnection(DBURL, DBUSERNAME, DBPASSWORD);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to connect to the database.");
		}
	}
	
	public Boolean validateUser(String username, String password) throws SQLException {
		String queryString =
			    "SELECT U.username " +
			    "FROM Users U " +
			    "WHERE username = ? AND password = ?";

		PreparedStatement prepareStatement = dbcon.prepareStatement(queryString);
		prepareStatement.setString(1, username);
		prepareStatement.setString(2, DigestUtils.shaHex(password));
		ResultSet rs = prepareStatement.executeQuery();
		return rs.next();
	}
	
	public Boolean createUser(String username, String password) throws SQLException {
		String queryString = "INSERT into Users (username, password) VALUES (?, ?)";
		PreparedStatement ps = dbcon.prepareStatement(queryString);
		ps.setString(1, username);
		ps.setString(2, DigestUtils.shaHex(password));
		int manipulated = ps.executeUpdate();
		return manipulated==1;
	}
	
	
	public static void main(String[] args) {
		DBConnect  a = new DBConnect();
		try {
			a.validateUser("Andrew", "andrew");
			System.out.println("yay");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}