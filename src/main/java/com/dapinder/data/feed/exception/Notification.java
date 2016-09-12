/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notification {

	private List<Error> errors = new ArrayList<Error>();

	public void addError(String message) {
		addError(message, null);
	}

	public void addError(String message, Exception e) {
		errors.add(new Error(message, e));
	}

	public String errorMessage() {
		return errors.stream().map(e -> e.message).collect(Collectors.joining(", "));
	}

	public List<Error> errorMessageList() {
		return errors;
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	private static class Error {
		private String message;
		private Exception cause;

		private Error(String message, Exception cause) {
			this.message = message;
			this.cause = cause;
		}
	}

}
