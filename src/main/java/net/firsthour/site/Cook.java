package net.firsthour.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.jbake.app.configuration.JBakeConfiguration;
import org.jbake.app.configuration.JBakeConfigurationFactory;
import org.jbake.launcher.BakeWatcher;
import org.jbake.launcher.Baker;
import org.jbake.launcher.JettyServer;

public class Cook {

	public void cook() throws Exception {
		mergeManual();
		
		File source = new File("src/main/resources/site");
		File destination = new File("src/main/resources/site/output");
		JBakeConfiguration config =
			new JBakeConfigurationFactory().createDefaultJbakeConfiguration(
				source,
				destination,
				true);
		
		Baker baker = new Baker();
		baker.bake(config);
		
		mergeHtml();
		
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
	
	//copy contents of the manual directory into content
	private void mergeManual() throws IOException {
		Path manual = Paths.get("src/main/resources/site/manual").toAbsolutePath();
		Path content = Paths.get("src/main/resources/site/content").toAbsolutePath();
		
		merge(manual, content);
	}
	
	//copy contents of the html directory into output
	private void mergeHtml() throws IOException {
		Path html = Paths.get("src/main/resources/site/html").toAbsolutePath();
		Path output = Paths.get("src/main/resources/site/output").toAbsolutePath();
		
		merge(html, output);
	}
	
	private void merge(Path source, Path destination) throws IOException {
		if(Files.exists(source)) {
			try(var s1 = Files.newDirectoryStream(source)) {
				for(Path sub : s1) {
					if(Files.isDirectory(sub)) {
						Files.createDirectories(destination.resolve(sub.getFileName()));
						try(var s2 = Files.newDirectoryStream(sub)) {
							for(Path sub2 : s2) {
								Files.copy(
									sub2,
									destination
										.resolve(sub.getFileName())
										.resolve(sub2.getFileName()),
									StandardCopyOption.REPLACE_EXISTING);
							}
						}
					} else {
						Files.copy(
							sub,
							destination.resolve(sub.getFileName()),
							StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
	}
}
