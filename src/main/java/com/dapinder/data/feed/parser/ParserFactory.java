/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.parser.impl.CSVParser;
import com.dapinder.data.feed.parser.impl.ExcelParser;
import com.dapinder.data.feed.parser.impl.JSONParser;
import com.dapinder.data.feed.parser.impl.XMLParser;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;



@Component
public class ParserFactory {

	private static ParserFactory INSTANCE = new ParserFactory();

	private IParse parser;

	@Autowired
	@Qualifier("csvParser")
	private CSVParser csvParser;

	@Autowired
	@Qualifier("xmlParser")
	private XMLParser xmlParser;

	@Autowired
	@Qualifier("excelParser")
	private ExcelParser excelParser;

	@Autowired
	@Qualifier("jsonParser")
	private JSONParser jsonParser;

	/**
	 * Gets the single instance of ParserFactory.
	 *
	 * @return single instance of ParserFactory
	 */
	public static ParserFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the file parser.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param fileName
	 *            the file name
	 * @param fileType
	 *            the file type
	 * @return the file parser
	 * @throws DataFeedException
	 *             the validation exception
	 */
	public <T> T getFileParser(final Class<T> clazz, final File file, final FeedSupportedFileTypes fileType)
			throws DataFeedException {
		switch (fileType) {
		case JSON:
			parser = jsonParser;
		case CSV:
			parser = csvParser;
		case XLS:
			parser = excelParser;
		default:// Default XML
			parser = xmlParser;
		}
		return parser.parse(clazz, file);
	}

	/**
	 * Gets the parser.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 * @param fileName the file name
	 * @param fileType the file type
	 * @return the parser
	 * @throws DataFeedException the validation exception
	 */
	public <T> T getParser(final Class<T> clazz, final File file, final FeedSupportedFileTypes fileType)
			throws DataFeedException {
		switch (fileType) {
		case JSON:
			return JSONParser.getInstance().parse(clazz, file);
		case CSV:
			return CSVParser.getInstance().parse(clazz, file);
		case XLS:
			return ExcelParser.getInstance().parse(clazz, file);
		default:// Default XML
			return XMLParser.getInstance().parse(clazz, file);
		}
	}
}
