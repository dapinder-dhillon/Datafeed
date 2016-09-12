/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.impl;

import java.io.File;
import java.util.Map;

import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;
//import com.qr.jadu.validation.ValidationException;

/**
 * The Interface DataFeed.
 */
public interface DataFeed {

	/**
	 * Process.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param sourceDir
	 *            the source dir
	 * @param fileType
	 *            the file type
	 * @return the map
	 * @throws DataFeedException
	 *             the validation exception
	 */
	<T> Map<String, T> process(final Class<T> clazz, final File sourceDir, final FeedSupportedFileTypes fileType)
			throws DataFeedException;

	/**
	 * Process.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param files
	 *            the files
	 * @param fileType
	 *            the file type
	 * @return the map
	 * @throws DataFeedException
	 *             the validation exception
	 */
	<T> Map<String, T> process(final Class<T> clazz, final File[] files, final FeedSupportedFileTypes fileType)
			throws DataFeedException;

	/**
	 * Validate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param fedData
	 *            the fed data
	 * @param clazz
	 *            the clazz
	 * @param fileType
	 *            the file type
	 * @throws DataFeedException
	 *             the validation exception
	 */
	<T> Notification validate(Map<String, T> fedData, Class<T> clazz, final FeedSupportedFileTypes fileType);

	/**
	 * Archive files.
	 *
	 * @param sourceDir
	 *            the source dir
	 * @param fileType
	 *            the file type
	 */
	void archiveFiles(final File sourceDir, final FeedSupportedFileTypes fileType);
}
