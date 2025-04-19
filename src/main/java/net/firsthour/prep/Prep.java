package net.firsthour.prep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
			ResultSet results = statement.executeQuery(
			"""
				select
					n.nid as 'nid',
					n.type as 'type',
					n.title as 'title',
					n.created as 'date',
					u.name as 'author',
					u.uid as 'uid',
					url.dst as 'url',
					teaser as 'teaser',
					body as 'body'
				from node n
				left outer join users u on n.uid = u.uid
				left outer join node_revisions nr on n.nid = nr.nid
				left outer join url_alias url on url.src = CONCAT('node/', n.nid)
				order by n.created asc
			""");
			
			while(results.next()) {
				Post post = Post.from(results);
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
	}
}
