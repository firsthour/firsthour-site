package net.firsthour.site;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.app.configuration.JBakeConfigurationFactory;

import net.firsthour.prep.Prep;

public class Main {
	
	//"prep" to just prep
	//"cook" to just cook
	//"both" to prep then cook
	public static void main(String[] args) throws Exception {
		if(args.length == 0 || StringUtils.isBlank(args[0])) {
			return;
		}
		
		Set<String> steps = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		steps.addAll(Arrays.asList(args));
		
		boolean prep = steps.contains("prep");
		boolean cook = steps.contains("cook");
		boolean serve = steps.contains("serve");
		
		//extracts posts from database and writes them as intermediate html files
		//files include a JBake ready header
		//also merges in files from the manual directory
		//all files end up in content directory
		if(prep) {
			new Prep().prep();
		}
		
		if(cook || serve) {
			JBakeConfiguration config =
				new JBakeConfigurationFactory().createDefaultJbakeConfiguration(
					new File("src/main/resources/site"),
					new File("src/main/resources/site/output"),
					true);
			
			//runs JBake to convert all intermediate html files to web ready html
			if(cook) {
				new Cook().cook(config);
			}
			
			//start the webserver
			if(serve) {
				new Serve().serve(config);
			}
		}
	}
}
