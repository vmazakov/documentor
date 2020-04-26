package bg.documentor.util.impl;

import bg.documentor.util.WordDocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j @Component public class FileWatcher {

	private final WordDocumentProcessor wordDocumentProcessor;
	@Value("${fileWatcher.processDir}") public File processDir;
	@Value("${fileWatcher.watchFolder}") private String watchFolder;

	public FileWatcher(WordDocumentProcessor wordDocumentProcessor) {
		this.wordDocumentProcessor = wordDocumentProcessor;
	}

	@Scheduled(fixedDelay = 10000) public void watcher() {
		log.info("Starting file watcher for import on: {}", watchFolder);
		scanFolder();
	}

	//
	void scanFolder() {
		File p = new File(watchFolder);
		File[] listFiles = p.listFiles();
		if (listFiles.length == 0) {
			FileWatcher.log.info("No file found at watched folder.");
		} else {

			for (File f : listFiles) {
				if (!getFileExtension(f).equals("docx")) {
					FileWatcher.log
							.info("The file found is not compatible. A MSWord version 2007 and above file is expected.");
				} else {

					FileWatcher.log.info("File '{}' found after startup. Creating new session", f.getName());

					Path sourcePath = f.toPath();
					Path targetPath = processDir.toPath().resolve(f.getName());
					try {
						Files.move(sourcePath, targetPath, REPLACE_EXISTING);
						FileWatcher.log.info("File '{}' moved to process folder.", f.getName());
						//				wordDocumentProcessor.parseWordDocGamma(targetPath.toFile());
						wordDocumentProcessor.parseWordDocTeta(targetPath.toFile());
					} catch (IOException e) {
						FileWatcher.log.info("File '{}' move to process folder failed.", f.getName());
						throw new IllegalArgumentException("Moving files failed " + e);
					}
				}
			}
		}
	}

	private String getFileExtension(File f) {
		return FilenameUtils.getExtension(f.getName().trim());
	}
}



