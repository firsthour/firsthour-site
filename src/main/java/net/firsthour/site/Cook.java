package net.firsthour.site;

import java.io.File;

import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.app.configuration.JBakeConfigurationFactory;
import org.jbake.launcher.BakeWatcher;
import org.jbake.launcher.Baker;
import org.jbake.launcher.JettyServer;

public class Cook {

	public static void main(String[] args) {
		try {
			File source = new File("src/main/resources/site");
			File destination = new File("src/main/resources/site/output");
			JBakeConfiguration config =
				new JBakeConfigurationFactory().createDefaultJbakeConfiguration(
					source,
					destination,
					true);
			
			Baker baker = new Baker();
			baker.bake(config);
			
			BakeWatcher watcher = new BakeWatcher();
			watcher.start(config);
			
			try(JettyServer server = new JettyServer()) {
				server.run(destination.getPath(), config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
