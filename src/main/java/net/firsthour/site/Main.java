package net.firsthour.site;

import net.firsthour.prep.Prep;

public class Main {
	
	public static void main(String[] args) throws Exception {
		new Prep().prep();
		
		new Cook().cook();
	}
}
