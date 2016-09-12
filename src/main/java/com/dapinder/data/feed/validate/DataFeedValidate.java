/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.validate;

import java.util.Map;

import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;


public interface DataFeedValidate {
	<T> Notification validate(Map<String, T> fedData, Class<T> clazz, final FeedSupportedFileTypes fileType);
}
