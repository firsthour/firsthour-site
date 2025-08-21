package net.firsthour.site;

import org.apache.commons.lang3.StringUtils;

import net.firsthour.prep.Prep;

public class Main {
	
	//"prep" to just prep
	//"cook" to just cook
	//"both" to prep then cook
	public static void main(String[] args) throws Exception {
		if(args.length == 0 || StringUtils.isBlank(args[0])) {
			return;
		}
		
		String arg = args[0];
		
		//extracts posts from database and writes them as intermediate html files
		//files include a JBake ready header
		//also merges in files from the manual directory
		//all files end up in content directory
		if("prep".equalsIgnoreCase(arg) || "both".equalsIgnoreCase(arg)) {
			new Prep().prep();
		}
		
		//runs JBake to convert all intermediate html files to web ready html
		if("cook".equalsIgnoreCase(arg) || "both".equalsIgnoreCase(arg)) {
			new Cook().cook();
		}
	}
}
