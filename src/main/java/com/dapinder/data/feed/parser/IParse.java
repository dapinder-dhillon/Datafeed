/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser;

import java.io.File;

import com.dapinder.data.feed.exception.DataFeedException;



/**
 * The Interface IParse.
 * @author Dapinder Singh
 */
public interface IParse {

	/**
	 * Parses the.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 * @param fileName the file name
	 * @return the t
	 * @throws DataFeedException the validation exception
	 */
	<T> T parse(final Class<T> clazz, final File file) throws DataFeedException;
}
