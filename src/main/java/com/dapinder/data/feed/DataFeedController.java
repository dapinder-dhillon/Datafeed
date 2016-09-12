package com.dapinder.data.feed;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.impl.DataFeed;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;

@Component
public class DataFeedController {

	@Autowired
	private DataFeed dataFeed;

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
	 *             the data feed exception
	 */
	public <T> Map<String, T> process(final Class<T> clazz, final File sourceDir, final FeedSupportedFileTypes fileType)
			throws DataFeedException {
		Map<String, T> fedData = dataFeed.process(clazz, sourceDir, fileType);
		return fedData;
	}

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
	 *             the data feed exception
	 */
	public <T> Map<String, T> process(final Class<T> clazz, final File[] files, final FeedSupportedFileTypes fileType)
			throws DataFeedException {
		Map<String, T> fedData = dataFeed.process(clazz, files, fileType);
		return fedData;
	}

	/**
	 * Validate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param fileType
	 *            the file type
	 * @param fedData
	 *            the fed data
	 * @return the notification
	 */
	public <T> Notification validate(final Class<T> clazz, final FeedSupportedFileTypes fileType,
			final Map<String, T> fedData) {
		return dataFeed.validate(fedData, clazz, fileType);
	}

	/**
	 * Archive files.
	 *
	 * @param sourceDir
	 *            the source dir
	 * @param fileType
	 *            the file type
	 */
	public void archiveFiles(final File sourceDir, final FeedSupportedFileTypes fileType) {
		dataFeed.archiveFiles(sourceDir, fileType);
	}

}
