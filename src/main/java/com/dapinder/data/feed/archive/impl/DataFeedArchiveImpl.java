/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.archive.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.archive.DataFeedArchive;
import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;
import com.dapinder.data.feed.util.ParseUtility;

@Component("dataFeedArchiveImpl")
public class DataFeedArchiveImpl implements DataFeedArchive {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataFeedArchiveImpl.class);

	private static final SimpleDateFormat DIR_DATE_FORMAT = new SimpleDateFormat("MM_dd_YYYY_HH_mm");

	@Override
	public void archiveFiles(File sourceDir, FeedSupportedFileTypes fileType) {
		LOGGER.info("Process started for archiving processed files: ");
		final File[] processedFiles = ParseUtility.listFiles(sourceDir, fileType);
		if (null != processedFiles && processedFiles.length > 0) {
			// Create folder (if not exists) named "processed" in source dir.
			final String processedDirName = sourceDir.getName() + "\\processed";
			final File processedDir = new File(processedDirName);
			if (!processedDir.exists()) {
				processedDir.mkdir();
			}

			/*
			 * Create folder (if not exists) inside "processed" folder with name
			 * "current date time" and move all processed files there for each
			 * run.
			 */
			final Path targetDir = FileSystems.getDefault()
					.getPath(processedDirName + "\\" + DIR_DATE_FORMAT.format(new Date()));

			if (!(targetDir.toFile().exists())) {
				targetDir.toFile().mkdir();
			}

			for (final File processedXmlFile : processedFiles) {
				try {
					Files.move(processedXmlFile.toPath(), targetDir.resolve(processedXmlFile.toPath().getFileName()),
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					StringBuilder error = new StringBuilder("IOException while moving files to processed folder: ")
							.append(targetDir).append(". Encountered exception while processing file: "
									+ processedXmlFile.getAbsolutePath());
					LOGGER.error(error.toString());
					throw new DataFeedRuntimeException(e);
				}
			}
		}
		LOGGER.info("Successfully processed all files. ");
	}

}
