package net.firsthour.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Post {
	
	private int nodeId;
	private String siteType;
	private String title;
	private LocalDateTime date;
	private List<String> authors;
	private String url;
	private String teaser;
	private String body;
	
	public static Post from(ResultSet result, Map<Integer, List<String>> authors) throws SQLException {
		Post post = new Post();
		
		int nid = result.getInt("nid");
		
		post.setNodeId(nid);
		post.setSiteType(result.getString("type"));
		post.setTitle(result.getString("title"));
		post.setDate(Instant.ofEpochSecond(result.getInt("date")).atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		post.setAuthors(
			authors.containsKey(nid)
				? authors.get(nid)
				: List.of(result.getString("author")));
		
		post.setUrl(result.getString("url"));
		
		String teaser = result.getString("teaser");
		teaser = cleanTeaser(teaser);
		post.setTeaser(teaser);
		
		String body = result.getString("body");
		body = cleanBody(body);
		post.setBody(body);
		
		return post;
	}

	private static String cleanBody(String body) {
		body = body
			.replace(
				"<a class=\"mta\">MtA<span>Minutes to Action</span>",
				"Minutes to Action")
			.replace(
				"<a class=\"mta\">MtA</a>",
				"Minutes to Action")
			.replace(
				"<th class=\"centerinfobox\" colspan=\"2\"><a class=\"amazonlink\".*?</th>",
				"")
			.replace(
				"<img src=\"/sites/default/files/images/clocks/infobox-score-.*?style=\"vertical-align: bottom;\">",
				"")
			.replace(
				"&nbsp;&nbsp;<a\\ href=\"\\#scores\"\\ class=\"infoclock\"><img\\ src=\"/sites/default/files/images/clocks/infobox-score-.*?\\.png\"\\ style=\"vertical-align:\\ bottom\"\\ alt=\"Clock\\ score\\ of\\ .*?\"\\ />",
				"<br />")
			.replace(
				"<span style=\"font-family: verdana,geneva;\">",
				"<span>")
			.replace(
				"<a href=\"([^\"]+)\" rel=\"[^\"]*\" title=\"[^\"]*\">",
				"<a href=\"$1\" target='_blank' style='display: inline-block'>")
			.replace(
				"<tr class=\"even\"><td colspan=\"2\" style=\"padding: 3px 1px 3px 1px\">",
				"<tr class=\"even\"><td colspan=\"2\" style=\"padding: 3px 1px 3px 1px; text-align: center\">")
			.replace(
				"<!--break--><br />",
				"")
			.replace(
				"</span></a></td></tr><br /><tr class=\"odd\"><br /><br /></tr><br /></table>",
				"</span></td></tr></table>")
			.replace(
				"<br /><br /><h2>Minute by Minute</h2><br /><div id=\"show\" style=\"display: none\"><a href=\"javascript:HideContent('show'); ShowContent('minute-by-minute')\" style=\"font-size: x-small\">[show]</a></div><br /><div id=\"minute-by-minute\"><div id=\"hide\" style=\"display: none\"><a href=\"javascript:HideContent('minute-by-minute'); ShowContent('show')\" style=\"font-size: x-small\">[hide]</a></div><br />",
				"<h2>Minute by Minute</h2><div id=\"minute-by-minute\">")
			.replace(
				"(minutes are in bold)",
				"(minutes are in bold)<br />")
			.replace(
				"<div style=\"text-align: center\"><a href",
				"<div style=\"text-align: center\"><a class='screenshotLink' href")
			.replace(
				"=\"http://firsthour.net/",
				"=\"/")
			.replace(
				"I think that strikes a good balance for reviewing this game.</div>",
				"I think that strikes a good balance for reviewing this game.")
			.replace(
				"=\"/sites/default/files/images/",
				"=\"/images/")
			.replace(
				"<a href=\"../../screenshots/",
				"<a href=\"/screenshots/")
			.replace(
				"<a href=\"first-hour-review/",
				"<a href=\"/first-hour-review/")
			.replace(
				"<a href=\"editorial/",
				"<a href=\"/editorial/")
			.replace(
				"<a href=\"full-review/",
				"<a href=\"/full-review/")
			.replace(
				"<a href=\"list/",
				"<a href=\"/list/");
		
		return body;
	}
	
	private static String cleanTeaser(String teaser) {
		teaser = teaser.replace(
			"><img class=\"summaryimage\" ",
			" style='display: inline-block'><img class=\"summaryimage\" ");
		
		teaser = teaser.replace("</a>", "</a><br />");
		
		List<String> suffixes = Arrays.asList("<div>", "<em>", "<p>", "<strong>", "<span>");
		boolean found;
		do {
		    found = false;
		    for (String suffix : suffixes) {
		        if (teaser.endsWith(suffix)) {
		            teaser = teaser.substring(0, teaser.length() - suffix.length());
		            found = true;
		            break; // restart loop from beginning since string has changed
		        }
		    }
		} while (found);
		
		teaser = teaser
			.replace(
				"<p><span style=\"color: rgb(0, 0, 255);\"><strong>",
				"<p><span style=\"color: rgb(0, 0, 255);\">")
			.replace(
				"in the game which took me about 25-30 hours.<br><strong>",
				"in the game which took me about 25-30 hours.<br>")
			.replace(
				"\r\n\r\n",
				"<br><br>")
			.replace(
				"\n\n",
				"<br><br>")
			.replace(
				"\r\n",
				"")
			.replace(
				"\n",
				"")
			.replace(
				"</a><br />",
				"</a>")
			.replace(
				"<span style=\"font-family: verdana,geneva;\">",
				"<span>")
			.replace(
				"alt=\".net/images/\" title=\".net/images/\"",
				"alt=\"Microphone\" title=\"Microphone\"")
			.replace(
				"I think that strikes a good balance for reviewing this game.<br>",
				"I think that strikes a good balance for reviewing this game.<br></div>")
			.replace(
				"=\"http://firsthour.net/",
				"=\"/")
			.replace(
				"<a href=\"blog/",
				"<a href=\"/blog/")
			.replace(
				"<a href=\"full-review/",
				"<a href=\"/full-review/")
			.replace(
				"<a href=\"goty/",
				"<a href=\"/goty/")
			.replace(
				"<a href=\"list/",
				"<a href=\"/list/");
		
		return teaser;
	}
}
