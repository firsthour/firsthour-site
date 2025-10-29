package net.firsthour.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.firsthour.model.Type;

public class CreateManualPost {
	
	public static void main(String[] args) throws IOException {
		//update below
		Type type = Type.DEVLOG;
		String title = "My First Game Jam - Step and Deliver";
		String screenshotDir = "step-and-deliver";
		String headerImage = "step-and-deliver-header.png"; //bluesky likes 1.91 ratio (eg. 1000x523)
		//update above
		
		create(type, title, screenshotDir, headerImage);
	}
	
	//include <BREAK> at teaser end
	//images use <IMG name-of-image.png>
	private static String text =
"""

""";
	
	private static final String PARA = "</p>\n<p>";
	
	private static void create(
			Type type,
			String title,
			String screenshotDir,
			String headerImage) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		String[] parts = text.split("\\<BREAK\\>");
		
		if(parts.length == 0 || parts.length == 1) {
			throw new IllegalArgumentException("missing teaser");
		}
		
		String teaser = parts[0].trim();
		String body = parts[1].trim();
		
		sb.append("title=")
			.append(title)
			.append("\n")
			
			.append("date=")
			.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.append("\n")
			
			.append("type=post\n")
			
			.append("status=published\n")
			
			.append("teaser=")
			.append(
				buildHtmlTeaser(
					type,
					title,
					screenshotDir,
					headerImage,
					deleteLinks(teaser),
					true))
			.append("\n")
			
			.append("author=Greg Noe\n")
			
			.append("siteType=")
			.append(type.siteType)
			.append("\n")
			
			.append("image=/screenshots/")
			.append(screenshotDir)
			.append("/")
			.append(headerImage)
			.append("\n")
			
			.append("description=")
			.append(stripTeaser(deleteLinks(teaser)))
			.append("\n")
			
			.append("~~~~~~")
			.append("\n")
			
			.append(
				buildHtmlTeaser(
					type,
					title,
					screenshotDir,
					headerImage,
					teaser,
					false))
			.append("\n")
			
			.append(buildBody(screenshotDir, "<p>" + body + "</p>"));
		
		createFile(
			sb.toString(),
			type,
			title,
			screenshotDir);
	}
	
	private static String buildHtmlTeaser(
			Type type,
			String title,
			String screenshotDir,
			String headerImage,
			String teaser,
			boolean includeLink) {
		
		StringBuilder sb = new StringBuilder();
		
		teaser = reduceLineBreaks(teaser);
		teaser = teaser.replace("\n", "</p><p>");
		
		String url = convertTitleToSlug(title);
		
		sb.append("<div class=\"manualTeaserImage\">");
		
		if(includeLink) {
			sb.append("<a href=\"/")
				.append(type.dir)
				.append("/")
				.append(url)
				.append("\">");
		}
		
		sb.append("<img src=\"/screenshots/")
			.append(screenshotDir)
			.append("/")
			.append(headerImage)
			.append("\" alt=\"")
			.append(convertImageToString(headerImage))
			.append("\" title=\"")
			.append(convertImageToString(headerImage))
			.append("\">");
		
		if(includeLink) {
			sb.append("</a>");
		}
		
		sb.append("</div>")
			.append("<p>")
			.append(teaser)
			.append("</p>");
		
		return sb.toString();
	}
	
	private static String buildBody(String screenshotDir, String body) {
		body = reduceLineBreaks(body);
		body = body.replace("\n", PARA);
		body = breakUpParagraphTags(body);
		
		String result = "";
		for(String line : body.split("\\n")) {
			if(line.startsWith("<p><IMG ")) {
				String image = line.replace("<p><IMG ", "").replace("></p>", "");
				
				String actualScreenshotDir =
					image.contains("/")
						? image.split("\\/")[0]
						: screenshotDir;
				
				image =
					image.contains("/")
						? image.split("\\/")[1]
						: image;
				
				StringBuilder sb = new StringBuilder();
				sb.append("<img src=\"/screenshots/")
					.append(actualScreenshotDir)
					.append("/")
					.append(image)
					.append("\" alt=\"")
					.append(convertImageToString(image))
					.append("\" title=\"")
					.append(convertImageToString(image))
					.append("\" />");
				
				line = "<div class=\"manualImage\">" + sb.toString() + "</div>";
			}
			
			result += line + "\n";
		}
		
		return result.trim();
	}
	
	private static String stripTeaser(String teaser) {
		teaser = teaser.replace("\n", " ").replace("\r", "").trim();
		
		boolean foundDoubleSpace = false;
		do {
			int length = teaser.length();
			teaser = teaser.trim().replace("  ", " ");
			foundDoubleSpace = length != teaser.length();
		} while(foundDoubleSpace);
		
		return teaser;
	}
	
	private static String convertImageToString(String image) {
		return image.trim().replace("-", " ").substring(0, image.indexOf("."));
	}
	
	private static String deleteLinks(String teaser) {
		return teaser.replaceAll("\\<a href=.*?\\>", "").replaceAll("\\<\\/a\\>", "");
	}
	
	private static String breakUpParagraphTags(String body) {
		return body
			.replace(PARA + "<ul>" + PARA, "</p>\n<ul>\n")
			.replace(PARA + "<ol>" + PARA, "</p>\n<ol>\n")
			.replace("</li>" + PARA, "</li>\n")
			.replace("</ul>" + PARA, "</ul>\n<p>")
			.replace("</ol>" + PARA, "</ol>\n<p>")
			.replace(PARA + "<h1>", "</p>\n<h2>")
			.replace("<p><h1>", "<h1>")
			.replace(PARA + "<h2>", "</p>\n<h2>")
			.replace("<p><h2>", "<h2>")
			.replace(PARA + "<h3>", "</p>\n<h2>")
			.replace("<p><h3>", "<h3>")
			.replace(PARA + "<h4>", "</p>\n<h2>")
			.replace("<p><h4>", "<h4>")
			.replace(PARA + "<h5>", "</p>\n<h2>")
			.replace("<p><h5>", "<h5>")
			.replace("</h1>" + PARA, "</h1>\n<p>")
			.replace("</h2>" + PARA, "</h2>\n<p>")
			.replace("</h3>" + PARA, "</h3>\n<p>")
			.replace("</h4>" + PARA, "</h4>\n<p>")
			.replace("</h5>" + PARA, "</h5>\n<p>");
	}
	
	private static String reduceLineBreaks(String text) {
		boolean foundDoubleLineBreak = false;
		do {
			int length = text.length();
			text = text.trim().replace("\n\n", "\n");
			text = text.replace("\r\n\r\n", "\n");
			foundDoubleLineBreak = length != text.length();
		} while(foundDoubleLineBreak);
		return text;
	}
	
	private static String convertTitleToSlug(String title) {
		return title.toLowerCase().replace(" ", "-").replace("---", "-");
	}
	
	private static void createFile(String output, Type type, String title, String screenshotDir) throws IOException {
		Path dir = Paths.get("src/main/resources/site/manual").toAbsolutePath().resolve(type.dir);
		if(!dir.toFile().exists()) {
			dir.toFile().mkdirs();
		}
		
		String slug = convertTitleToSlug(title);
		
		//create slug in dir if it doesn't exist
		//okay to reuse file
		Path file = dir.resolve(slug + ".html");
		
		Files.writeString(file, output);
		System.out.println("Created: " + file.toAbsolutePath());
		
		//create screenshot dir if it doesn't exist
		Path screenshotPath = Paths.get("src/main/resources/site/assets/screenshots").toAbsolutePath().resolve(screenshotDir);
		if(!screenshotPath.toFile().exists()) {
			screenshotPath.toFile().mkdirs();
			System.out.println("Created screenshot directory: " + screenshotPath.toAbsolutePath());
		}
	}
}
