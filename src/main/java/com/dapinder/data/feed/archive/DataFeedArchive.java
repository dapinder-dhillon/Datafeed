/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.archive;

import java.io.File;

import com.dapinder.data.feed.util.FeedSupportedFileTypes;



public interface DataFeedArchive {
	void archiveFiles(final File sourceDir, final FeedSupportedFileTypes fileType);
}
