/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.DataFeedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


/**
 * @author Dapinder Singh
 *
 */
@Component("csvParser")
public class CSVParser extends EnclosingWrapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CSVParser.class);

	private static CSVParser INSTANCE = new CSVParser();

	public static CSVParser getInstance() {
		return INSTANCE;
	}

	@Override
	protected <T> List<T> getData(final Class<?> innerListClassType, final File file) throws DataFeedException {
		LOGGER.info("Get Data: " + innerListClassType);
		final CsvMapper mapper = new CsvMapper();
		Class<?> innerListClass = null;
		try {
			innerListClass = Class.forName(innerListClassType.getTypeName());
		} catch (ClassNotFoundException e) {
			throw new DataFeedRuntimeException(e);
		}
		final CsvSchema schema = mapper.schemaFor(innerListClass);
		MappingIterator<T> it = null;
		List<T> csvRowList = null;
		try {
			it = mapper.reader(innerListClass).with(schema).readValues(file);
			csvRowList = new ArrayList<T>();
			while (it.hasNextValue()) {
				csvRowList.add(it.nextValue());
			}
		} catch (JsonProcessingException e) {
			throw new DataFeedRuntimeException(e);

		} catch (IOException e) {
			throw new DataFeedRuntimeException(e);
		}

		return csvRowList;
	}

}
