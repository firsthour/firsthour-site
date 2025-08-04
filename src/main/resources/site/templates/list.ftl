<#include "header.ftl">

<#include "menu.ftl">

<h1>${content.title}</h1>

<#list published_posts?filter(post ->
		post.status == "published"
		&&
			(content.siteType?contains(post.siteType)
			|| content.author?contains(post.author)))
			as post>
	
	<div class="listPost">
		<a href="/${post.uri?remove_ending("/index.html")}">
			<h2 class="postTitle">${post.title}</h2>
		</a>
		<#if !content.site?has_content || content.siteType?contains(",")>
			<h3 class="siteType">${post.siteType}</h3>
		</#if>
		<p>${post.date?string("MMMM d, yyyy")} by
			<#list post.author?split(",") as author>
				<a href="/writer/${author?lower_case?replace(" ", "-")?replace(".", "")}">${author}</a><#sep>, </#sep>
			</#list>
		</p>
		<p>${post.teaser}</p>
		<a href="/${post.uri?remove_ending("/index.html")}">Read more</a>
	</div>
	<#sep>
		<hr />
	</#sep>
</#list>

<#include "footer.ftl">
