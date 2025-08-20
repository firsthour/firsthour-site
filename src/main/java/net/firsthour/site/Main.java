package net.firsthour.site;

import net.firsthour.prep.Prep;

public class Main {
	
	public static void main(String[] args) throws Exception {
		//extracts posts from database and writes them as intermediate html files
		//files include a JBake ready header
		//also merges in files from the manual directory
		//all files end up in content directory
		new Prep().prep();
		
		//runs JBake to convert all intermediate html files to web ready html
		new Cook().cook();
	}
}
