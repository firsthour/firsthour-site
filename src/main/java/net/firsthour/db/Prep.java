package net.firsthour.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Prep {
	
	public static void main(String[] args) {
		try {
			Connection conn = DB.getConnection();
			Statement statement = conn.createStatement();
			ResultSet results = statement.executeQuery("select * from url_alias");
			
			while(results.next()) {
				System.out.println(results.getInt("pid") + "; " + results.getString("src") + "; " + results.getString("dst"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
