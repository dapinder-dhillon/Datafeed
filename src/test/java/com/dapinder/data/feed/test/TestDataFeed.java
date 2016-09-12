/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.CollectionUtils;

import com.dapinder.data.feed.DataFeedController;
import com.dapinder.data.feed.exception.DataFeedException;
import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.test.vo.others.RecordJSON;
import com.dapinder.data.feed.test.vo.others.RecordsJSON;
import com.dapinder.data.feed.test.vo.xls.RecordXLS;
import com.dapinder.data.feed.test.vo.xls.RecordsXLS;
import com.dapinder.data.feed.test.vo.xml.Record;
import com.dapinder.data.feed.test.vo.xml.Records;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class TestDataFeed {

	@Autowired
	private DataFeedController dataFeedController;

	@Test
	public void testXML() {
		ClassLoader classLoader = getClass().getClassLoader();
		File sourceDir = new File(classLoader.getResource("xml").getFile());
		try {
			Map<String, Records> rtnObj = dataFeedController.process(Records.class, sourceDir,
					FeedSupportedFileTypes.XML);
			if (!CollectionUtils.isEmpty(rtnObj)) {
				final Notification notification = dataFeedController.validate(Records.class, FeedSupportedFileTypes.XML,
						rtnObj);
				System.out.println("Has errors: " + notification.hasErrors());
				if (notification.hasErrors()) {

					System.err.println(notification.errorMessage());
				}
				System.out.println("####################### Test XML ########################");
				Set<String> keys = rtnObj.keySet();
				for (String key : keys) {
					Records recs = rtnObj.get(key);
					if (!CollectionUtils.isEmpty(recs.getRecords())) {
						for (Record record : recs.getRecords()) {
							System.out.println(record);
						}
					}
				}
				System.out.println("##########################################################");
			}
			assertEquals(rtnObj.size(), 6);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception encountered");
		}

	}

	@Test
	public void testJSON() {
		ClassLoader classLoader = getClass().getClassLoader();
		File sourceDir = new File(classLoader.getResource("json").getFile());
		try {
			Map<String, RecordsJSON> rtnObj = dataFeedController.process(RecordsJSON.class, sourceDir,
					FeedSupportedFileTypes.JSON);
			if (!CollectionUtils.isEmpty(rtnObj)) {
				System.out.println("####################### Test JSON ########################");
				Set<String> keys = rtnObj.keySet();
				for (String key : keys) {
					RecordsJSON recs = rtnObj.get(key);
					if (!CollectionUtils.isEmpty(recs.getRecords())) {
						for (RecordJSON record : recs.getRecords()) {
							System.out.println(record);
						}
					}
				}
				System.out.println("##########################################################");
			}
			assertEquals(rtnObj.size(), 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception encountered");
		}
	}

	@Test
	public void testCSV() {
		ClassLoader classLoader = getClass().getClassLoader();
		File sourceDir = new File(classLoader.getResource("csv").getFile());
		try {
			Map<String, RecordsJSON> rtnObj = dataFeedController.process(RecordsJSON.class, sourceDir,
					FeedSupportedFileTypes.CSV);
			if (!CollectionUtils.isEmpty(rtnObj)) {
				System.out.println("####################### Test CSV ########################");
				Set<String> keys = rtnObj.keySet();
				for (String key : keys) {
					RecordsJSON recs = rtnObj.get(key);
					if (!CollectionUtils.isEmpty(recs.getRecords())) {
						for (RecordJSON record : recs.getRecords()) {
							System.out.println(record);
						}
					}
				}
				System.out.println("##########################################################");
			}
			assertEquals(rtnObj.size(), 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception encountered");
		}
	}

	@Test
	public void testXLS() {
		ClassLoader classLoader = getClass().getClassLoader();
		File sourceDir = new File(classLoader.getResource("xls").getFile());

		try {
			Map<String, RecordsXLS> rtnObj = dataFeedController.process(RecordsXLS.class, sourceDir,
					FeedSupportedFileTypes.XLS);
			if (!CollectionUtils.isEmpty(rtnObj)) {
				System.out.println("####################### Test Excel ########################");
				Set<String> keys = rtnObj.keySet();
				for (String key : keys) {
					RecordsXLS recs = rtnObj.get(key);
					if (!CollectionUtils.isEmpty(recs.getRecords())) {
						for (RecordXLS record : recs.getRecords()) {
							System.out.println(record);
						}
					}
				}
				System.out.println("##########################################################");
			}
			assertEquals(rtnObj.size(), 1);
		} catch (DataFeedException e) {
			System.err.println("===================================================================");
			System.err.println("========================VALIDATION ERRORS==========================");
			System.err.println("===================================================================");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception encountered");
		}
	}
}
