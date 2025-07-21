<#include "header.ftl">

<#include "menu.ftl">

<#list published_posts?filter(post -> post.status == "published") as post>
	<div class="listPost">
		<a href="/${post.uri?remove_ending("/index.html")}">
			<h2 class="postTitle">${post.title}</h2>
		</a>
		<h3 class="siteType">${post.siteType}</h3>
		<p>${post.date?string("MMMM dd, yyyy")} by
			<#list post.author?split(",") as author>
				<a href="/writer/${author?lower_case?replace(" ", "-")?replace(".", "")}">${author}</a><#sep>, </#sep>
			</#list>
		</p>
		<p>${post.teaser}</p>
		<a href="/${post.uri?remove_ending("/index.html")}">Read more</a>
	</div>
	<hr />
</#list>

<#if config.index_paginate>
	<span><#if (previousFileName??)><a href="/${previousFileName}">Newer</a></#if></span>
	<span>${currentPageNumber} of ${numberOfPages}</span>
	<#if (nextFileName??)><a href="/${nextFileName}">Older</a></#if>
</#if>

<#include "footer.ftl">
