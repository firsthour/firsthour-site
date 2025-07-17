<#include "header.ftl">

<#include "menu.ftl">

<#list published_posts?filter(post -> post.status == "published") as post>
	<a href="/${post.uri?remove_ending("/index.html")}">
		<h2 class="postTitle">${post.title}</h2>
	</a>
	<h3 class="siteType">${post.siteType}</h3>
	<p>by ${post.author} — ${post.date?string("MMMM dd, yyyy")}</p>
	<p>${post.teaser}</p>
	<a href="/${post.uri?remove_ending("/index.html")}">Read more</a>
	<hr />
</#list>

<#if config.index_paginate>
	<span><#if (previousFileName??)><a href="/${previousFileName}">Newer</a></#if></span>
	<span>${currentPageNumber} of ${numberOfPages}</span>
	<#if (nextFileName??)><a href="/${nextFileName}">Older</a></#if>
</#if>

<#include "footer.ftl">
