package com.ag.xyzbank.model;

import lombok.Value;

@Value
public class ValidationResponse {
	private final String message;
	private final boolean isValid;

	public static ValidationResponse error(String message) {
		return new ValidationResponse(message, false);
	}

	public static ValidationResponse success(String message) {
		return new ValidationResponse(message, true);
	}

}
