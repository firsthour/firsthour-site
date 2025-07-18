<#include "header.ftl">

<#include "menu.ftl">

<h1>${content.title}</h1>

<#list published_posts?filter(post -> post.status == "published" && content.siteType?contains(post.siteType)) as post>
	<div class="listPost">
		<a href="/${post.uri?remove_ending("/index.html")}">
			<h2 class="postTitle">${post.title}</h2>
		</a>
		<#if content.siteType?contains(",")>
			<h3 class="siteType">${post.siteType}</h3>
		</#if>
		<p>by ${post.author} — ${post.date?string("MMMM dd, yyyy")}</p>
		<p>${post.teaser}</p>
		<a href="/${post.uri?remove_ending("/index.html")}">Read more</a>
	</div>
	<#sep>
		<hr />
	</#sep>
</#list>

<#include "footer.ftl">
