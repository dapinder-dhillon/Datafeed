/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.xls;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordsXLS {
	@JsonProperty("record")
	private List<RecordXLS> records;

	public List<RecordXLS> getRecords() {
		return records;
	}

	public void setRecords(List<RecordXLS> record) {
		this.records = record;
	}

	@Override
	public String toString() {
		return "Records [records=" + records + "]";
	}
}
