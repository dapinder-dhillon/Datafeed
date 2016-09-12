/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser.impl;

import java.beans.Statement;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.parser.IParse;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Dapinder Singh
 *
 */
public abstract class EnclosingWrapper implements IParse {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnclosingWrapper.class);

	@Override
	public <T> T parse(final Class<T> clazz, final File file) throws DataFeedException {
		T t = null;
		Class<?> innerListClassType = null;
		final Field[] fields = clazz.getDeclaredFields();

		Field selectedField = null;

		for (final Field field : fields) {
			final Annotation[] annotations = field.getDeclaredAnnotations();
			for (final Annotation annotate : annotations) {
				if (annotate instanceof JsonProperty) {
					final ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
					innerListClassType = (Class<?>) stringListType.getActualTypeArguments()[0];
					selectedField = field;
					break;
				}
			}
		}
		LOGGER.info("Inner List class: " + innerListClassType);

		if (null != innerListClassType && null != selectedField) {
			List<T> csvRowList = getData(innerListClassType, file);
			final String methodName = new StringBuilder("set")
					.append(selectedField.getName().substring(0, 1).toUpperCase())
					.append(selectedField.getName().substring(1)).toString();
			try {
				t = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new DataFeedRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new DataFeedRuntimeException(e);
			}
			final Statement stmt = new Statement(t, methodName, new Object[] { csvRowList });
			try {
				stmt.execute();
			} catch (Exception e) {
				throw new DataFeedRuntimeException(e);
			}
		}
		return t;
	}

	/**
	 * Gets the data.
	 *
	 * @param <T>
	 *            the generic type
	 * @param innerListClassType
	 *            the inner list class type
	 * @param fileName
	 *            the file name
	 * @return the data
	 * @throws DataFeedException
	 *             the validation exception
	 */
	protected abstract <T> List<T> getData(Class<?> innerListClassType, final File file) throws DataFeedException;

}
