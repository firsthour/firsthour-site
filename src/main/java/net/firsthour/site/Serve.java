package net.firsthour.site;

import java.io.File;
import java.io.IOException;

import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.launcher.BakeWatcher;
import org.jbake.launcher.JettyServer;

public class Serve {
	
	public void serve(JBakeConfiguration config) throws IOException {
		BakeWatcher watcher = new BakeWatcher();
		watcher.start(config);
		
		try(JettyServer server = new JettyServer()) {
			server.run(new File("src/main/resources/site/output").getPath(), config);
		}
	}
}
