/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.parser.IParse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component("jsonParser")
public final class JSONParser implements IParse {
	private static JSONParser INSTANCE = new JSONParser();

	public static JSONParser getInstance() {
		return INSTANCE;
	}

	public <T> T parse(final Class<T> clazz, final File file) throws DataFeedException {
		ObjectMapper mapper = new ObjectMapper();
		T records = null;
		try {
			records = mapper.readValue(file, clazz);
		} catch (JsonParseException e) {
			throw new DataFeedRuntimeException(e);
		} catch (JsonMappingException e) {
			throw new DataFeedRuntimeException(e);
		} catch (IOException e) {
			throw new DataFeedRuntimeException(e);
		}
		return records;
	}

}
