/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.impl;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.archive.DataFeedArchive;
import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;
import com.dapinder.data.feed.util.ParseUtility;
import com.dapinder.data.feed.validate.DataFeedValidate;


/**
 * The Class DataFeedImpl.
 *
 * @author Dapinder Singh
 */
@Component
public class DataFeedImpl implements DataFeed {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataFeedImpl.class);

	@Autowired
	@Qualifier("dataFeedArchiveImpl")
	private DataFeedArchive dataFeedArchive;

	@Autowired
	@Qualifier("dataFeedValidateOval")
	private DataFeedValidate dataFeedValidate;


	/*
	 * (non-Javadoc)
	 *
	 * @see com.crbs.data.feed.impl.DataFeed#process(java.lang.Class,
	 * java.io.File, com.crbs.data.feed.util.FeedSupportedFileTypes, boolean,
	 * boolean)
	 */
	@Override
	public <T> Map<String, T> process(final Class<T> clazz, final File sourceDir, final FeedSupportedFileTypes fileType)
			throws DataFeedException {
		LOGGER.info("Process started ");
		/* validate */
		validateIfDir(sourceDir);
		final File[] dirFiles = ParseUtility.listFiles(sourceDir, fileType);
		return process(clazz, dirFiles, fileType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.crbs.data.feed.impl.DataFeed#process(java.lang.Class,
	 * java.io.File[], com.crbs.data.feed.util.FeedSupportedFileTypes, boolean,
	 * boolean)
	 */
	@Override
	public <T> Map<String, T> process(Class<T> clazz, File[] files, FeedSupportedFileTypes fileType)
			throws DataFeedException {
		final ForkJoinPool pool = new ForkJoinPool();
		Map<String, T> fedData = null;
		try {
			final RecursiveDataFeed<T> recursiveTask = new RecursiveDataFeed<T>(clazz, files, fileType);
			fedData = pool.invoke(recursiveTask);
		} finally {
			pool.shutdown();
		}
		return fedData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.crbs.data.feed.impl.DataFeed#validate(java.util.Map,
	 * java.lang.Class, com.crbs.data.feed.util.FeedSupportedFileTypes)
	 */
	@Override
	public <T> Notification validate(Map<String, T> fedData, Class<T> clazz, FeedSupportedFileTypes fileType) {
		return dataFeedValidate.validate(fedData, clazz, fileType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.crbs.data.feed.impl.DataFeed#archiveFiles(java.io.File,
	 * com.crbs.data.feed.util.FeedSupportedFileTypes)
	 */
	@Override
	public void archiveFiles(final File sourceDir, final FeedSupportedFileTypes fileType) {
		dataFeedArchive.archiveFiles(sourceDir, fileType);

	}

	/**
	 * The method is validating and throwing exception if the given File path is
	 * not a directory.
	 *
	 * @param sourceDir
	 *            the source dir
	 * @throws DataFeedException
	 *             the validation exception
	 */
	private static void validateIfDir(final File sourceDir) throws DataFeedException {
		if (!sourceDir.isDirectory()) {
			/*throw new ValidationException("Exception: ",
					ValidationUtility.getValidationResFromString("\"" + sourceDir + "\" is not a directory."));*/

			throw new DataFeedRuntimeException(new Exception(""));
		}
	}

}
