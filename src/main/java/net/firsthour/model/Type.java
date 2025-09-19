package net.firsthour.model;

public enum Type {

	BLOG("blog", "Blog Post"),
	BOOK_REVIEW("book-review", "Book Review"),
	DLC("dlc", "Downloadable Content"),
	FIRST_HOUR_REVIEW("first-hour-review", "First Hour Review"),
	FULL_REVIEW("full-review", "Full Review"),
	GOTY("goty", "Game of the Year Awards"),
	MOVIE_REVIEW("movie-review", "Movie Review"),
	NOSTALGIA("nostalgia", "Gaming Nostalgia"),
	SEAL_OF_QUALITY("seal-of-quality", "Seal of Quality");
	
	public final String dir;
	public final String siteType;
	
	private Type(String dir, String siteType) {
		this.dir = dir;
		this.siteType = siteType;
	}
}
