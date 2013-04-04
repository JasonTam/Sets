package DBI;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;

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
	
	
	public int updateScore(ArrayList<Integer> list, String username) throws SQLException {
		String updateString=
			    "UPDATE Users " +
			    "SET " +
			        "games_played = games_played + ?, " +
			        "games_won = games_won + ?, " +
			        "games_lost = games_lost + ?, " +
			        "games_tied = games_tied + ?, " +
			        "games_quit = games_quit + ?, " +
			        "sets_submitted = sets_submitted + ?, " +
			        "sets_correct = sets_correct + ? " +
			    "WHERE username = ?";

		PreparedStatement prepareStatement = dbcon.prepareStatement(updateString);
		for(int i = 0; i < list.size() ; i++)
		{
		    prepareStatement.setInt(i+1, list.get(i));
		}
		prepareStatement.setString(list.size() + 1, username);
		
		System.out.println(prepareStatement.toString());
		return prepareStatement.executeUpdate();
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