/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.util;

public enum FeedSupportedFileTypes {
	XML("xml"), JSON("json"), CSV("csv"), XLS("xlsx");
	private String fileType;

	private FeedSupportedFileTypes(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
