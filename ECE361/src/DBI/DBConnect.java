package DBI;

import java.security.MessageDigest;
import java.sql.*;

import org.apache.commons.codec.digest.DigestUtils;

public class DBConnect {
	
	private static String DBURL = "jdbc:mysql://199.98.20.120/SetGame";
	private static String DBUSERNAME = "root";
	private static String DBPASSWORD = "password";
	
	private static Connection dbcon;
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
	
	public static Boolean validateUser(String username, String password) throws SQLException {
		String queryString =
			    "SELECT U.username " +
			    "FROM Users U " +
			    "WHERE username = ? AND password = ?";

		PreparedStatement prepareStatement = dbcon.prepareStatement(queryString);
		prepareStatement.setString(1, username);
		prepareStatement.setString(2, DigestUtils.sha1Hex(password));
		ResultSet rs = prepareStatement.executeQuery();
		return rs.next();
	}
	
	public static Boolean createUser(String username, String password) throws SQLException {
		String queryString = "INSERT into Users (username, password) VALS (?, ?)";
		PreparedStatement ps = dbcon.prepareStatement(queryString);
		ps.setString(1, username);
		ps.setString(2, DigestUtils.sha1Hex(password));
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	
	public static void main(String[] args) {
		new DBConnect();
		try {
			validateUser("Andrew", "andrew");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}