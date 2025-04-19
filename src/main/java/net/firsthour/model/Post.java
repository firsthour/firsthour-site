package net.firsthour.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.Data;

@Data
public class Post {
	
	private int nodeId;
	private String type;
	private String title;
	private LocalDateTime date;
	private String author;
	private int authorId;
	private String url;
	private String teaser;
	private String body;
	
	public static Post from(ResultSet result) throws SQLException {
		Post post = new Post();
		
		post.setNodeId(result.getInt("nid"));
		post.setType(result.getString("type").replace("_", "-"));
		post.setTitle(result.getString("title"));
		post.setDate(Instant.ofEpochSecond(result.getInt("date")).atZone(ZoneId.systemDefault()).toLocalDateTime());
		post.setAuthor(result.getString("author"));
		post.setAuthorId(result.getInt("uid"));
		post.setUrl(result.getString("url"));
		post.setTeaser(result.getString("teaser"));
		post.setBody(result.getString("body"));
		
		return post;
	}
}
