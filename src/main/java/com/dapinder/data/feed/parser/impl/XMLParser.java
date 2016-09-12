/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.parser.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Component;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.parser.IParse;


@Component("xmlParser")
public class XMLParser implements IParse {

	private static XMLParser INSTANCE = new XMLParser();

	public static XMLParser getInstance() {
		return INSTANCE;
	}

	/**
	 * JaxB+STAX Implementation
	 *
	 * @throws Exception
	 */
	public <T> T parse(final Class<T> clazz, final File file)throws DataFeedException {
		T records = null;

			try {
				final FileReader fr = new FileReader(file);
				final XMLInputFactory xmlif = XMLInputFactory.newInstance();
				final XMLStreamReader xmler = xmlif.createXMLStreamReader(fr);
				final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				records = jaxbUnmarshaller.unmarshal(xmler, clazz).getValue();
			} catch (FileNotFoundException e) {
				throw new DataFeedRuntimeException(e);
			} catch (FactoryConfigurationError e) {
				//throw new JaduRuntimeException(e);
			} catch (XMLStreamException e) {
				throw new DataFeedRuntimeException(e);
			} catch (JAXBException e) {
				throw new DataFeedRuntimeException(e);
			}

		return records;
	}

}
