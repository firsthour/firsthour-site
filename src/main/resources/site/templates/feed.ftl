<?xml version="1.0"?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
	<channel>
		<title>First Hour</title>
		<link>${config.site_host}</link>
		<atom:link href="${config.site_host}/${config.feed_file}" rel="self" type="application/rss+xml" />
		<description>First Hour RSS Feed</description>
		<language>en-us</language>
		<pubDate>${published_date?string("EEE, d MMM yyyy HH:mm:ss Z")}</pubDate>
		<lastBuildDate>${published_date?string("EEE, d MMM yyyy HH:mm:ss Z")}</lastBuildDate>
		
		<#list published_posts?sequence[0..9] as post>
			<item>
				<title><#escape x as x?xml>${post.title}</#escape></title>
				<link>${config.site_host}/${post.noExtensionUri}</link>
				<pubDate>${post.date?string("EEE, d MMM yyyy HH:mm:ss Z")}</pubDate>
				<guid isPermaLink="false">${post.noExtensionUri}</guid>
				<description>
					<#escape x as x?xml>
						${post.body}
					</#escape>
				</description>
			</item>
		</#list>
	</channel>
</rss>
