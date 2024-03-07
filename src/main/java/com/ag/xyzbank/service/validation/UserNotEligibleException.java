package com.ag.xyzbank.service.validation;

public class UserNotEligibleException extends RuntimeException{
	public UserNotEligibleException(String message) {
		super(message);
	}
}
