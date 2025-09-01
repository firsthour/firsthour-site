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
		body = cleanBody(
			body,
			post.getUrl().startsWith("beyond-the-first-hour-review")
				|| "first-hour-review/nintendo-ds-m-rated-blitz".equals(post.getUrl())
				|| "first-hour-review/scribblenauts".equals(post.getUrl())
				|| "first-hour-review/mlb-2k10".equals(post.getUrl()),
			"recap/day-one".equals(post.getUrl())
				|| "recap/day-one".equals(post.getUrl())
				|| "recap/day-two".equals(post.getUrl())
				|| "recap/day-three".equals(post.getUrl())
				|| "recap/day-four".equals(post.getUrl())
				|| "recap/day-five".equals(post.getUrl()));
		post.setBody(body);
		
		return post;
	}

	private static String cleanBody(String body, boolean fixDanglingDiv, boolean addNewlines) {
		body = body
			.replace(
				"\r\n\r\n<span class=\"minute-counter\">",
				"<br><br><span class=\"minute-counter\">")
			.replace(
				"<a class=\"mta\">MtA<span>Minutes to Action</span>",
				"Minutes to Action")
			.replace(
				"<a class=\"mta\">MtA</a>",
				"Minutes to Action")
			.replace(
				"<th class=\"centerinfobox\" colspan=\"2\"><a class=\"amazonlink\".*?</th>",
				"")
			.replaceAll(
				"<th class=\\\"centerinfobox\\\" colspan=\\\"2\\\"><a class=\\\"amazonlink\\\" target=\\\"_blank\\\" href=\\\"http:\\/\\/www\\.amazon\\.com\\/dp\\/[A-Z0-9]*\\?tag=thfiho0a-20\\\">Buy from Amazon<\\/a><\\/th>",
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
				"<tr class=\"even\">\r\n<td colspan=\"2\" style=\"padding: 3px 1px 3px 1px\">",
				"<tr class=\"even\">\r\n<td colspan=\"2\" style=\"padding: 3px 1px 3px 1px; text-align: center\">")
			.replace(
				"<!--break-->",
				"")
			.replace(
				"</span></a></td></tr><br /><tr class=\"odd\"><br /><br /></tr><br /></table>",
				"</span></td></tr></table>")
			.replace(
				"<br /><br /><h2>Minute by Minute</h2><br /><div id=\"show\" style=\"display: none\"><a href=\"javascript:HideContent('show'); ShowContent('minute-by-minute')\" style=\"font-size: x-small\">[show]</a></div><br /><div id=\"minute-by-minute\"><div id=\"hide\" style=\"display: none\"><a href=\"javascript:HideContent('minute-by-minute'); ShowContent('show')\" style=\"font-size: x-small\">[hide]</a></div><br />",
				"<h2>Minute by Minute</h2><div id=\"minute-by-minute\">")
			.replace(
				"<div id=\"show\" style=\"display: none\"><a href=\"javascript:HideContent('show'); ShowContent('minute-by-minute')\" style=\"font-size: x-small\">[show]</a></div>",
				"")
			.replace(
				"<div id=\"hide\" style=\"display: none\"><a href=\"javascript:HideContent('minute-by-minute'); ShowContent('show')\" style=\"font-size: x-small\">[hide]</a></div>",
				"")
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
				"<a href=\"/list/")
			.replace(
				"\" /><span><b>Gameplay</b>:&nbsp;",
				"\" /><br><span><b>Gameplay</b>:&nbsp;")
			.replace(
				"<span class=\"score-type\">Gameplay</span>",
				"<br><br><span class=\"score-type\">Gameplay</span>")
			.replace(
				"<span class=\"score-type\">Fun Factor</span>",
				"<br><br><span class=\"score-type\">Fun Factor</span>")
			.replace(
				"<span class=\"score-type\">Graphics</span>",
				"<br><br><span class=\"score-type\">Graphics</span>")
			.replace(
				"<span class=\"score-type\">Graphics and Sound</span>",
				"<br><br><span class=\"score-type\">Graphics and Sound</span>")
			.replace(
				"<span class=\"score-type\">Story</span>",
				"<br><br><span class=\"score-type\">Story</span>")
			.replace(
				"<span class=\"score-type\">Overall First Hour</span>",
				"<br><br><span class=\"score-type\">Overall First Hour</span>")
			.replace(
				"<span class=\"score-type\">Overall</span>",
				"<br><br><span class=\"score-type\">Overall</span>")
			.replaceAll(
				"<br><span><b>Gameplay<\\/b>:&nbsp;(\\d\\d*|\\d\\.\\d)<br \\/><b>Fun&nbsp;Factor<\\/b>:&nbsp;(\\d\\d*|\\d\\.\\d)<br \\/><b>Gfx\\/Sound<\\/b>:&nbsp;(\\d\\d*|\\d\\.\\d)<br \\/><b>Story<\\/b>:&nbsp;(\\d\\d*|\\d\\.\\d)<br \\/><\\/span>",
				"")
			.replaceAll(
				"<span class=\"score-type\">(?!Minutes to Action)([^<]+)</span>:\\s*(\\d+)",
				"<span class=\"score-type\">$1</span>: $2<br>")
			.replace(
				"{{/first-hour-review/god-of-war-2\" >God of War 2",
				"<a href=\"/first-hour-review/god-of-war-2\">God of War 2</a>")
			.replace(
				"<script type=\"text/javascript\">document.getElementById('hide').style.display = ''</script>",
				"")
			.replace(
				"<h2 style=\"line-height: 50%\">Scores</h2>\r\n<br><br>",
				"<h2 style=\"line-height: 50%\">Scores</h2>\r\n<br>")
			.replaceAll(
				"([\\.\\!\\?\\\"])\\r\\n\\r\\n([A-Z]|\\<a)",
				"$1<br><br>$2")
			.replace(
				"Retribution</b>.",
				"Retribution</a>.")
			.replaceAll(
				"\\<span class\\=\\\"question\\\"\\>((?!Minutes to Action).*)[\\:\\?]\\<\\/span\\>",
				"<br><br><span class=\"question\">$1:</span>")
			.replaceAll(
				"<span class=\"answer\">(\\d\\.\\d|\\d\\d|\\d)",
				"<span class=\"answer\">$1<br>")
			.replace(
				"<h2 style=\"line-height: 50%\">Scores</h2>\r\n<br><br>",
				"<h2 style=\"line-height: 50%\">Scores</h2>\r\n<br>")
			.replaceAll(
				"title=\"&lt;a href='.*' target='_blank'&gt;(Save Full-Sized Image|Save Cover)&lt;\\/a&gt;\"",
				"target='_blank'")
			.replace(
				"<div style=\"text-align: center; margin: auto\"><object type=\"application/x-shockwave-flash\" style=\"width:425px; height:344px;\" data=\"http://www.youtube.com/v/HrnsOx9Akcc\"><param name=\"movie\" value=\"http://www.youtube.com/v/HrnsOx9Akcc\" /></object>",
				"")
			.replaceAll(
				"<span class=\\\"question\\\">Minutes to Action:<\\/span> <span class=\\\"answer\\\">(\\d\\d*)<br>(\\r\\n)?<\\/span>\\r\\n(\\r\\n)?<br><br>",
				"<span class=\"question\">Minutes to Action:</span> <span class=\"answer\">$1</span>\r\n\r\n<br><br>")
			.replace(
				"<th class=\"leftinfobox\" style=\"width: 50%\">Minutes to Action</a></th>",
				"<th class=\"leftinfobox\" style=\"width: 50%\">Minutes to Action</th>")
			.replaceAll(
				"<tr class=\\\"(odd|even)\\\">\\r\\n<th class=\\\"centerinfobox\\\" colspan=\\\"2\\\"><a class=\\\"amazonlink\\\" .*>Buy from Amazon<\\/a><\\/th>\\s*\\r\\n<\\/tr>",
				"")
			.replace(
				"<span class=\"question\">Minutes to Action:</span> <span class=\"answer\">19<br>",
				"<span class=\"question\">Minutes to Action:</span> <span class=\"answer\">19</span>")
			.replace(
				"<td class=\"rightinfobox\">8&nbsp;&nbsp;<a href=\"#scores\" class=\"infoclock\"><img src=\"/images/clocks/infobox-score-8.png\" style=\"vertical-align: bottom\" alt=\"Clock score of 8\" /><span><b>Graphics</b>:&nbsp;8<br /><b>Sound</b>:&nbsp;8<br /><b>Controls</b>:&nbsp;7<br /><b>Gameplay</b>:&nbsp;8<br /></span></a></td>",
				"<td class=\"rightinfobox\">8&nbsp;&nbsp;<a href=\"#scores\" class=\"infoclock\"><img src=\"/images/clocks/infobox-score-8.png\" style=\"vertical-align: bottom\" alt=\"Clock score of 8\" /></a></td>")
			.replace(
				"and argue about the Nintendo 64's library of games.<br><br>",
				"and argue about the Nintendo 64's library of games.<br><br><a href='/podcast/first-hour-podcast-episode-001.mp3'>Download</a><br><br>")
			.replace(
				"<a href=\"http://www.paul-eastwood.com/podcast/first-hour-podcast-episode-002.mp3\">Download the podcast here</a>.  We hope to provide iTunes and general RSS support very soon.",
				"<a href='/podcast/first-hour-podcast-episode-002.mp3'>Download</a>")
			.replace(
				"Mass Effect 2, talk Sonic games, and more!<br><br>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.<br><br>",
				"Mass Effect 2, talk Sonic games, and more!<br><br><a href='/podcast/first-hour-podcast-episode-003.mp3'>Download</a><br><br>")
			.replace(
				"fresh episode five.</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"fresh episode five.</p>")
			.replace(
				"Final Fantasy XIII.  And that's not all!\r\n</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Final Fantasy XIII. And that's not all!</p><a href='/podcast/first-hour-podcast-episode-005.mp3'>Download</a><br><br>")
			.replace(
				"episode for any reason.</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"episode for any reason.</p><a href='/podcast/first-hour-podcast-episode-006.mp3'>Download</a><br><br>")
			.replace(
				"Don't miss it!</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Don't miss it!</p><a href='/podcast/first-hour-podcast-episode-007.mp3'>Download</a><br><br>")
			.replace(
				"podcast that you can not miss.</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"podcast that you can not miss.</p><a href='/podcast/first-hour-podcast-episode-008.mp3'>Download</a><br><br>")
			.replace(
				"Other M, and more! Enjoy the show!</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Other M, and more! Enjoy the show!</p><a href='/podcast/first-hour-podcast-episode-009.mp3'>Download</a><br><br>")
			.replace(
				"Mafia II, and more! Enjoy the show!</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Mafia II, and more! Enjoy the show!</p><a href='/podcast/first-hour-podcast-episode-010.mp3'>Download</a><br><br>")
			.replace(
				"</div>\r\n<div>\r\n<div>\r\n<table class=\"infobox\">",
				"<div>\r\n<div>\r\n<table class=\"infobox\">")
			.replace(
				"</div>\r\n<div> 		\r\n<table class=\"infobox\">",
				"<div>\r\n<div>\r\n<table class=\"infobox\">")
			.replace(
				"<h2>Getting the game</h2>\r\n</div>",
				"<h2>Getting the game</h2>\r\n")
			.replace(
				"just download it for the music alone.</p>",
				"just download it for the music alone.</p><br>")
			.replace(
				"</div>\r\n<div style=\"text-align: left;\">\r\n<div style=\"text-align: left;\"> 		\r\n<table class=\"infobox\">",
				"<div style=\"text-align: left;\">\r\n<div style=\"text-align: left;\"> 		\r\n<table class=\"infobox\">")
			.replace(
				"Ace Attorney Investigations: Miled",
				"Ace Attorney Investigations: Miles")
			.replace(
				"also be developer Quantic Dream",
				"also by developer Quantic Dream")
			.replace(
				"the story is pretty godo",
				"the story is pretty good")
			.replace(
				"screen to the exit on the right.</div>",
				"screen to the exit on the right.")
			.replace(
				"</div>\r\n<table class=\"infobox\">\r\n<tbody>",
				"<table class=\"infobox\">\r\n<tbody>")
			.replace(
				"<a href=\"/screenshots/mass-effect/mass-effect-cover.jpg\"><img src=\"/screenshots/mass-effect/mass-effect-cover-thumb.jpg\" alt=\"Mass Effect Cover\" title=\"Mass Effect Cover\" class=\"cover_image_no_infobox\" width=\"240\" height=\"337\"></a>Playing just the first hour of a game can be so boring!",
				"<a href=\"/screenshots/mass-effect/mass-effect-cover.jpg\" style='float: right'><img src=\"/screenshots/mass-effect/mass-effect-cover-thumb.jpg\" alt=\"Mass Effect Cover\" title=\"Mass Effect Cover\" class=\"cover_image_no_infobox\" width=\"240\" height=\"337\"></a>Playing just the first hour of a game can be so boring!")
			.replace(
				"Star Trek: The Next Generate - Final Unity",
				"Star Trek: The Next Generation - Final Unity")
			.replace(
				"<p><strong><a href=\"/screenshots/cave-story/cave-story-cover.jpg\">",
				"<p><strong><a href=\"/screenshots/cave-story/cave-story-cover.jpg\" style='float: right'>")
			.replace(
				"<tr class=\"odd\">\r\n<th class=\"centerinfobox\" colspan=\"2\"><a class=\"amazonlink\" href=\"http://www.amazon.com/dp/B009D0LBRO?tag=thfiho0a-20\" target=\"_blank\">Buy from Amazon (for Android)</a></th> 			\r\n</tr>",
				"")
			.replace(
				"<a href=\"/screenshots/legend-of-zelda-link-to-the-past/legend-of-zelda-link-to-the-past-cover.jpg\">",
				"<a href=\"/screenshots/legend-of-zelda-link-to-the-past/legend-of-zelda-link-to-the-past-cover.jpg\" style='float: right'>")
			.replace(
				"<img src=\"https://mail.google.com/mail/u/0/images/cleardot.gif\" class=\"ajT\">\r\n<div class=\"yj6qo ajU\">\r\n</div>",
				"")
			.replace(
				"<TR class=odd>\r\n<TH class=centerinfobox colSpan=2><A class=amazonlink href=\"http://www.amazon.com/dp/B001TOMR6Q?tag=thfiho0a-20\" target=_blank>Buy from Amazon</A></TH></TR>",
				"")
			.replace(
				"<TR class=even>\r\n<TH class=centerinfobox colSpan=2><A class=amazonlink href=\"http://www.amazon.com/dp/B002EE7OKE?tag=thfiho0a-20\" target=_blank>Buy from Amazon</A></TH></TR>",
				"")
			.replace(
				"<p>The First Hour is a unique video game review site",
				"<p><b>Updated</b>: See the new <a href='/blog/first-hour-returns'>About page</a>.</p><p>The First Hour is a unique video game review site")
		;
		
		if(fixDanglingDiv) {
			body = body.replace(
				"</div><a name=\"scores\"></a>",
				"<a name=\"scores\"></a>");
		}
		
		if(addNewlines) {
			body = body.replace("\r\n", "\r\n<br>");
		}
		
		//putting these last
		body = body.replace("  ", " ");
		body = body.replace(".&nbsp; ", ". ");
		body = body.replace("?&nbsp; ", "? ");
		body = body.replace("!&nbsp; ", "! ");
		body = body.replace(".&nbsp;\r\n", ".\r\n");
		body = body.replace("?&nbsp;\r\n", "?\r\n");
		body = body.replace("!&nbsp;\r\n", "!\r\n");
		
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
				"<a href=\"/list/")
			.replace(
				"and argue about the Nintendo 64's library of games.<br><br>",
				"and argue about the Nintendo 64's library of games.<br><br><a href='/podcast/first-hour-podcast-episode-001.mp3'>Download</a><br><br>")
			.replace(
				"<a href=\"http://www.paul-eastwood.com/podcast/first-hour-podcast-episode-002.mp3\">Download the podcast here</a>.  We hope to provide iTunes and general RSS support very soon.",
				"<a href='/podcast/first-hour-podcast-episode-002.mp3'>Download</a>")
			.replace(
				"Mass Effect 2, talk Sonic games, and more!<br><br>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.<br><br>",
				"Mass Effect 2, talk Sonic games, and more!<br><br><a href='/podcast/first-hour-podcast-episode-003.mp3'>Download</a><br><br>")
			.replace(
				"fresh episode five.</p>\r\n<p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"fresh episode five.</p>")
			.replace(
				"Final Fantasy XIII.  And that's not all!</p>",
				"Final Fantasy XIII. And that's not all!</p><a href='/podcast/first-hour-podcast-episode-005.mp3'>Download</a>")
			.replace(
				"episode for any reason.</p><p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"episode for any reason.</p><a href='/podcast/first-hour-podcast-episode-006.mp3'>Download</a><br><br>")
			.replace(
				"Don't miss it!</p><p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Don't miss it!</p><a href='/podcast/first-hour-podcast-episode-007.mp3'>Download</a><br><br>")
			.replace(
				"podcast that you can not miss.</p><p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"podcast that you can not miss.</p><a href='/podcast/first-hour-podcast-episode-008.mp3'>Download</a><br><br>")
			.replace(
				"Other M, and more! Enjoy the show!</p><p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Other M, and more! Enjoy the show!</p><a href='/podcast/first-hour-podcast-episode-009.mp3'>Download</a><br><br>")
			.replace(
				"Mafia II, and more! Enjoy the show!</p><p>Subscribe on <a href=\"http://itunes.apple.com/WebObjects/MZStore.woa/wa/viewPodcast?id=355612728\">iTunes</a> or via <a href=\"/podcast/rss.xml\">RSS</a>.",
				"Mafia II, and more! Enjoy the show!</p><a href='/podcast/first-hour-podcast-episode-010.mp3'>Download</a><br><br>")
			.replace(
				"<div><br>This game was developed by Rad",
				"<br>This game was developed by Rad")
			.replace(
				"thefirst hour of Red Dead Revolver on the Xbox.</div><div style=\"text-align: left;\">",
				"the first hour of Red Dead Revolver on the Xbox.</div>")
			.replace(
				"also be developer Quantic Dream",
				"also by developer Quantic Dream")
			.replace(
				"/sites/default/files/images/game-of-the-year-thumb.png",
				"/images/game-of-the-year-thumb.png")
		;
		
		teaser = teaser.replace("  ", " ");
		teaser = teaser.replace(".&nbsp; ", ". ");
		teaser = teaser.replace("?&nbsp; ", "? ");
		teaser = teaser.replace("!&nbsp; ", "! ");
		
		return teaser;
	}
}
