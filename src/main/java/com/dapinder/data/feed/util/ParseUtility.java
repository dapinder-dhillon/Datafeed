/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.util;

import java.io.File;
import java.io.FilenameFilter;

public class ParseUtility {
	/**
	 * @param sourceDir
	 * @return
	 */
	public static File[] listFiles(final File sourceDir, final FeedSupportedFileTypes fileType) {
		return sourceDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("." + fileType.getFileType());
			}
		});
	}
}
