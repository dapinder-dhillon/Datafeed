/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.test.vo.others;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordsJSON {
	@JsonProperty("record")
	private List<RecordJSON> records;

	public List<RecordJSON> getRecords() {
		return records;
	}

	public void setRecords(List<RecordJSON> record) {
		this.records = record;
	}

	@Override
	public String toString() {
		return "Records [records=" + records + "]";
	}
}
