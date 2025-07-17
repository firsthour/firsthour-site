<#include "header.ftl">

<#include "menu.ftl">

<#if (content.title)??>
	<div class="page-header">
		<h1 class="postTitle">${content.title}</h1>
		<h3 class="siteType">${content.siteType}</h3>
	</div>
<#else></#if>

<p>${content.date?string("MMMM dd, yyyy")} by ${content.author}</p>

<p>${content.body}</p>

<div class="readMore">
	<h3>Read More</h3>
	<#if content.nextContent??>
		<div class="readMoreNextContent">
			Next:
			<a href="/${content.nextContent.uri?remove_ending("/index.html")}">
				${content.nextContent.title}
			</a>
		</div>
	</#if>
	
	<#if content.previousContent??>
		<div class="readMorePreviousContent">
			Previous:
			<a href="/${content.previousContent.uri?remove_ending("/index.html")}">
				${content.previousContent.title}
			</a>
		</div>
	</#if>
</div>

<#include "footer.ftl">