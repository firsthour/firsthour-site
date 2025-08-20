package net.firsthour.prep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.firsthour.model.Post;

public class Prep {
	
	public void prep() throws Exception {
		List<Post> posts = getPosts();
		
		clearContentDir();
		
		for(Post post : posts) {
			new PostWriter(post).write();
		}
	}

	private List<Post> getPosts() throws Exception {
		List<Post> posts = new ArrayList<>();
		
		try(Statement statement = createStatement()) {
			//find nodes with multiple authors and build a map
			ResultSet results = statement.executeQuery(
			"""
				SELECT
					n.nid AS 'nid',
					GROUP_CONCAT(
						DISTINCT u.name
						ORDER BY u.name
						SEPARATOR ',') AS 'authors'
				FROM content_field_authors cfa
				INNER JOIN node n ON cfa.nid = n.nid
				INNER JOIN users u ON cfa.field_authors_uid = u.uid
				GROUP BY n.nid
			""");
			
			Map<Integer, List<String>> authors = new HashMap<>();
			while(results.next()) {
				int nid = results.getInt("nid");
				List<String> authorNames =
					Arrays.asList(results.getString("authors").split(","));
				authors.put(nid, authorNames);
			}
			
			results = statement.executeQuery(
			"""
				SELECT
					n.nid AS 'nid',
					nt.name as 'type',
					n.title AS 'title',
					n.created AS 'date',
					u.name AS 'author',
					u.uid AS 'uid',
					url.dst AS 'url',
					teaser AS 'teaser',
					body AS 'body'
				FROM node n
				LEFT OUTER JOIN users u ON n.uid = u.uid
				LEFT OUTER JOIN node_revisions nr ON n.nid = nr.nid
				LEFT OUTER JOIN url_alias url ON url.src = CONCAT('node/', n.nid)
				left outer join node_type nt on n.type = nt.type
				WHERE n.status = 1
				AND nt.name <> 'Poll'
				ORDER BY n.created asc
			""");
			
			while(results.next()) {
				Post post = Post.from(results, authors);
				posts.add(post);
			}
		}
		
		return posts;
	}
	
	private Statement createStatement() throws Exception {
		return DB.getConnection().createStatement();
	}
	
	private void clearContentDir() throws IOException {
		Path content = Paths.get("src/main/resources/site/content").toAbsolutePath();
		
		if(Files.exists(content)) {
			try(var s1 = Files.newDirectoryStream(content)) {
				for(Path sub : s1) {
					if(Files.isDirectory(sub)) {
						try(var s2 = Files.newDirectoryStream(sub)) {
							for(Path entry : s2) {
								Files.delete(entry);
							}
						}
					}
					Files.delete(sub);
				}
			}
		}
		
		Files.createDirectories(content);
		
		//copy contents of the directory manual into content
		Path manual = Paths.get("src/main/resources/site/manual").toAbsolutePath();
		if(Files.exists(manual)) {
			try(var s1 = Files.newDirectoryStream(manual)) {
				for(Path sub : s1) {
					if(Files.isDirectory(sub)) {
						Files.createDirectories(content.resolve(sub.getFileName()));
						try(var s2 = Files.newDirectoryStream(sub)) {
							for(Path sub2 : s2) {
								Files.copy(
									sub2,
									content
										.resolve(sub.getFileName())
										.resolve(sub2.getFileName()));
							}
						}
					} else {
						Files.copy(sub, content.resolve(sub.getFileName()));
					}
				}
			}
		}
	}
}
