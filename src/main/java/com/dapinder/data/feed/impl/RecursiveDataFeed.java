/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.parser.ParserFactory;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;



/**
 * @author Dapinder Singh
 *
 * @param <T>
 */
class RecursiveDataFeed<T> extends RecursiveTask<Map<String, T>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RecursiveDataFeed.class);

	private static final long serialVersionUID = 125084009657302999L;
	private static final int FILE_COUNT_THRESHOLD = 2;
	private FeedSupportedFileTypes fileType;
	private File[] dirFiles;

	private Class<T> clazz;

	private Map<String, T> finalMap = new HashMap<String, T>();

	/**
	 * @param sourceDirPath
	 * @param clazz
	 * @param files
	 */
	public RecursiveDataFeed(final Class<T> clazz, final File[] files,
			final FeedSupportedFileTypes fileType) {
		this.dirFiles = files;
		this.clazz = clazz;
		this.fileType = fileType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Map<String, T> compute() {
		if (dirFiles.length == 0) {
			return Collections.emptyMap();
		} else if (dirFiles.length <= FILE_COUNT_THRESHOLD) {
			return parseFiles(dirFiles, this.clazz);
		} else {
			// Split the array of XML files into two equal parts
			final int center = dirFiles.length / 2;
			final File[] part1 = (File[]) splitArray(dirFiles, 0, center);
			final File[] part2 = (File[]) splitArray(dirFiles, center, dirFiles.length);

			final RecursiveDataFeed<T> partX1 = new RecursiveDataFeed<T>(this.clazz, part1,
					this.fileType);
			partX1.fork();
			final RecursiveDataFeed<T> partX2 = new RecursiveDataFeed<T>(this.clazz, part2,
					this.fileType);
			final Map<String, T> netResultMap = new HashMap<String, T>();

			netResultMap.putAll(partX2.compute());
			netResultMap.putAll(partX1.join());
			return netResultMap;
		}
	}

	/**
	 * @param array
	 * @param start
	 * @param end
	 * @return
	 */
	private File[] splitArray(final File[] array, final int start, final int end) {
		final int length = end - start;
		final File[] part = new File[length];
		for (int i = start; i < end; i++) {
			part[i - start] = array[i];
		}
		return part;
	}

	/**
	 * @param filesToParse
	 * @param clazz
	 * @return
	 */
	private Map<String, T> parseFiles(final File[] filesToParse, final Class<T> clazz) {
		// Parse and copy the given set of XML files
		for (final File file : filesToParse) {
			T records = null;
			try {
				records = ParserFactory.getInstance().getParser(clazz, file, fileType);
			} catch (DataFeedException e) {
				// TODO log and what ahead
			}
			LOGGER.info(file.getName() + ",{" + Thread.currentThread().getName() + "}");
			finalMap.put(file.getName(), records);
		}
		return finalMap;
	}

}
