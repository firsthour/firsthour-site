package net.firsthour.prep;

import java.sql.Connection;
import java.sql.DriverManager;

import net.firsthour.util.Props;

public class DB {
	
	private DB() {}
	
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		String username = Props.get("username");
		String password = Props.get("password");
		String url = "jdbc:mysql://localhost:3306/firsthour?user=" + username + "&password=" + password;
		return DriverManager.getConnection(url);
	}
}
