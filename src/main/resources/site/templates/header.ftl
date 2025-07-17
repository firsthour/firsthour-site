<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8"/>
		<title><#if (content.title)??>${content.title} | First Hour<#else>First Hour - reviewing just the first hour of video games</#if></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/bootstrap.min.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/asciidoctor.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/base.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/prettify.css" rel="stylesheet">
		<link href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>css/styles.css" rel="stylesheet">
		
		<link rel="shortcut icon" href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>favicon.ico">
	</head>
	
	<body>
		<div id="wrap">