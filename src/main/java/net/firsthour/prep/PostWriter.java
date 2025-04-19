package net.firsthour.prep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.firsthour.model.Post;

public class PostWriter {
	
	private Post post;
	
	public PostWriter(Post post) {
		this.post = post;
	}
	
	public void write() throws IOException {
		Path html = createFile();
		System.out.println(html);
		System.out.println();
	}

	private Path createFile() throws IOException {
		System.out.println(post.getUrl());
		
		Path content = Paths.get("src/main/resources/site/content");
		
		String[] urlParts = post.getUrl().split("/");
		
		Path dir;
		if(urlParts.length > 1) {
			dir = Paths.get(content.toAbsolutePath() + "/" + urlParts[0]);
			if(!Files.exists(dir)) {
				Files.createDirectory(dir);
			}
		} else {
			//a few one off pages don't have a subdirectory (about, contact, scores)
			dir = content;
		}
		
		Path html = Paths.get(dir.toAbsolutePath() + "/" + urlParts[urlParts.length - 1] + ".html");
		return Files.createFile(html);
	}
}
