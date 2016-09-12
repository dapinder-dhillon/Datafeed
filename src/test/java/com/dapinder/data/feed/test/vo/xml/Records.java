/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "records")
public class Records {

	private List<Record> records;

	public List<Record> getRecords() {
		return records;
	}

	@XmlElement(name = "record")
	public void setRecords(List<Record> record) {
		this.records = record;
	}

	@Override
	public String toString() {
		return "Records [records=" + records + "]";
	}

}
