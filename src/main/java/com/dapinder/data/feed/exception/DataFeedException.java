/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.exception;

public class DataFeedException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 28525434204732638L;

	public DataFeedException() {
		super();
	}

	public DataFeedException(Exception ex) {
		super(ex);
	}

	/*public ValidationException(String message, ValidationResult res) {

	}*/

	public DataFeedException(String message, Notification res) {

	}

}
