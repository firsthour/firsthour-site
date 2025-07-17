		<div class="navbar navbar-default navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>">
						<img src="/images/first-hour-logo.png" />
						<span class="menuTitle">First Hour</span>
					</a>
				</div>
				<div class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li><a href="https://bsky.app/profile/firsthour.net">Bluesky</a></li>
						<li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>archive.html">Archive</a></li>
						<li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>${config.feed_file}">RSS Feed</a></li>
						<li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>about">About</a></li>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="container" style="display: flex">
			<div class="leftSideBar">
				<h4 style="font-weight: bold">Reviews</h4>
				<ul class="sideBarList">
					<li>
						<a href="/first-hour-review">The First Hour</a>
					</li>
					<li>
						<a href="/full-review">Full Reviews</a>
					</li>
					<li>
						<a href="/more-reviews">More Reviews</a>
					</li>
				</ul>
			</div>
			<div class="textBody">