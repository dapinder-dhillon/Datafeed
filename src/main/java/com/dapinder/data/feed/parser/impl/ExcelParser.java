/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser.impl;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.util.ExcelReporter;



@Component("excelParser")
public class ExcelParser extends EnclosingWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelParser.class);

	private static ExcelParser INSTANCE = new ExcelParser();

	public static ExcelParser getInstance() {
		return INSTANCE;
	}

	@Override
	protected <T> List<T> getData(final Class<?> innerListClassType, final File file) throws DataFeedException {
		LOGGER.info("Get Data: " + innerListClassType);
		List<T> csvRowList = null;
		final ExcelReporter excelReporter = new ExcelReporter(file.getPath());

		try {
			csvRowList = excelReporter.readData(innerListClassType.getName());
		} catch (Exception e) {
			throw new DataFeedRuntimeException(e);
		}
		return csvRowList;
	}
}
