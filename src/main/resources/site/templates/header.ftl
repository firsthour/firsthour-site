<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<#assign title =
			(content.title)???then(
				content.title + " | First Hour",
				"First Hour - reviewing just the first hour of video games")
		>
		
		<#assign image =
			(content.image)???then(
				config.site_host + content.image,
				"")
		>
		
		<#assign description =
			(content.description)???then(
				content.description,
				"")
		>
		
		<title>${title}</title>
		<meta name="title" content="${title}">
		<meta property="og:title" content="${title}">
		<meta name="twitter:title" content="${title}">
		
		<meta name="description" content="${description}">
		<meta property="og:description" content="${description}">
		<meta name="twitter:description" content="${description}">
		
		<meta name="image" content="${image}">
		<meta property="og:image" content="${image}">
		<meta name="twitter:image" content="${image}">
		
		<meta property="og:type" content="article">
		<meta property="article:published_time" content="${content.date???then(content.date?string("yyyy-MM-dd"), '')}">
		
		<meta property="og:url" content="${config.site_host}/${content.noExtensionUri???then(content.noExtensionUri, '')}">
		
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/bootstrap.min.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/asciidoctor.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/base.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/prettify.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/styles.css" rel="stylesheet">
		
		<link rel="shortcut icon" href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>favicon.ico">
	</head>
	
	<body>
		<div id="wrap">