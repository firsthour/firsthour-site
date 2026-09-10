<#include "header.ftl">

<#include "menu.ftl">

<h1>${content.title}</h1>

<#if content.title == "Game of the Year">
	<#assign goty_order = [
		"goty/2025-game-of-the-year",
		"goty/2016-game-of-the-year-stardew-valley-10th-anniversary",
		"goty/my-favorite-games-of-2015",
		"goty/2014-game-of-the-year",
		"goty/2013-game-of-the-year",
		"goty/2012-game-of-the-year-awards-day-four",
		"goty/2012-game-of-the-year-awards-day-three",
		"goty/2012-game-of-the-year-awards-day-two",
		"goty/2012-game-of-the-year-awards-day-one",
		"goty/2011-game-of-the-year-awards",
		"goty/2010-game-of-the-year-awards",
		"goty/2009-game-of-the-year-awards",
		"goty/2008-game-of-the-year-awards",
		"goty/2007-game-of-the-year-awards"
	]>

	<#list goty_order as slug>
		<#-- find the published post whose uri (without trailing /index.html) matches the slug -->
		<#assign matched = (published_posts?filter(p -> p.status == "published" && (p.uri?remove_ending("/index.html") == slug)))?first>
		<#if matched??>
			<div class="listPost">
				<a href="/${matched.uri?remove_ending("/index.html")}/">
					<h2 class="postTitle">${matched.title}</h2>
				</a>
				<#if !content.site?has_content || content.siteType?contains(",")>
					<h3 class="siteType">${matched.siteType}</h3>
				</#if>
				<p>${matched.date?string("MMMM d, yyyy")} by
					<#list matched.author?split(",") as author>
						<a href="/writer/${author?lower_case?replace(" ", "-")?replace(".", "")}/">${author}</a><#sep>, </#sep>
					</#list>
				</p>
				<p>${matched.teaser}</p>
				<a href="/${matched.uri?remove_ending("/index.html")}/">Read more</a>
			</div>
			<#sep>
				<hr />
			</#sep>
		</#if>
	</#list>
<#else>
	<#list published_posts?filter(post ->
		post.status == "published"
		&&
			(content.siteType?contains(post.siteType)
			|| content.author?contains(post.author)))
			as post>
		
		<div class="listPost">
			<a href="/${post.uri?remove_ending("/index.html")}/">
				<h2 class="postTitle">${post.title}</h2>
			</a>
			<#if !content.site?has_content || content.siteType?contains(",")>
				<h3 class="siteType">${post.siteType}</h3>
			</#if>
			<p>${post.date?string("MMMM d, yyyy")} by
				<#list post.author?split(",") as author>
					<a href="/writer/${author?lower_case?replace(" ", "-")?replace(".", "")}/">${author}</a><#sep>, </#sep>
				</#list>
			</p>
			<p>${post.teaser}</p>
			<a href="/${post.uri?remove_ending("/index.html")}/">Read more</a>
		</div>
		<#sep>
			<hr />
		</#sep>
	</#list>
</#if>

<#include "footer.ftl">
