package net.firsthour.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.app.configuration.JBakeConfigurationFactory;
import org.jbake.launcher.BakeWatcher;
import org.jbake.launcher.Baker;
import org.jbake.launcher.JettyServer;

public class Cook {

	public static void main(String[] args) throws Exception {
		new Cook().cook();
	}
	
	public void cook() throws Exception {
		File source = new File("src/main/resources/site");
		File destination = new File("src/main/resources/site/output");
		JBakeConfiguration config =
			new JBakeConfigurationFactory().createDefaultJbakeConfiguration(
				source,
				destination,
				true);
		
		Baker baker = new Baker();
		baker.bake(config);
		
		cleanup();
		
		BakeWatcher watcher = new BakeWatcher();
		watcher.start(config);
		
		try(JettyServer server = new JettyServer()) {
			server.run(destination.getPath(), config);
		}
	}
	
	private void cleanup() throws IOException {
		Path output = Paths.get("src/main/resources/site/output").toAbsolutePath();
		if(Files.exists(output)) {
			try(Stream<Path> walkStream = Files.walk(output)) {
				walkStream.filter(p -> p.toFile().isFile() && ".gitignore".equalsIgnoreCase(p.toFile().getName())).forEach(f -> {
					try {
						Files.delete(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}
}
