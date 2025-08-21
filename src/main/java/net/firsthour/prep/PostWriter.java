package net.firsthour.prep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import net.firsthour.model.Post;

public class PostWriter {
	
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private Post post;
	
	public PostWriter(Post post) {
		this.post = post;
	}
	
	public void write() throws IOException {
		Path html = createFile();
		writeText(html);
	}
	
	private Path createFile() throws IOException {
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
	
	private void writeText(Path html) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getJbakeHeader());
		
		sb.append(post.getBody());
		
		Files.writeString(html, sb.toString());
	}
	
	private String getJbakeHeader() {
		return
			"""
			title=TITLE
			date=DATE
			type=post
			status=published
			teaser=TEASER
			author=AUTHOR
			siteType=SITETYPE
			image=IMAGE
			description=DESCRIPTION
			~~~~~~
			"""
			.replace("TITLE", post.getTitle())
			.replace("DATE", post.getDate().format(DTF))
			.replace("TEASER", post.getTeaser())
			.replace("AUTHOR", post.getAuthors().stream().collect(Collectors.joining(",")))
			.replace("SITETYPE", post.getSiteType())
			.replace("IMAGE", findFirstImagePath())
			.replace("DESCRIPTION", buildDescription());
	}
	
	//use the teaser with HTML stripped out
	private String buildDescription() {
		String description =
			post.getTeaser()
				.replaceAll("<[^>]*>", " ")
				.replace("\"", "'")
				.replace("<", "")
				.replace(">", "")
				.trim();
		return description;
	}
	
	//find the first image source in the body
	private String findFirstImagePath() {
		String image = "";
		if(post.getBody().toLowerCase().contains("<img")) {
			image = post.getBody().replaceAll("(?si).*?<img.*?src=\"([^\"]+)\".*", "$1");
			if(StringUtils.isNotBlank(image) && image.contains("-thumb.")) {
				image = image.replace("-thumb.", ".");
			}
		}
		return image;
	}
}
