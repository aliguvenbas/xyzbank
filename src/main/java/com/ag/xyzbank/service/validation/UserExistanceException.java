package com.ag.xyzbank.service.validation;

import lombok.NonNull;

public class UserExistanceException extends RuntimeException {

	private UserExistanceException(@NonNull String message) {
		super(message);
	}

	public static UserExistanceException userExist(@NonNull String username) {
		return new UserExistanceException("User with username " + username + " is already exist");

	}

	public static UserExistanceException userDoesntExist(@NonNull String username) {
		return new UserExistanceException("User with username " + username + " does not exist");
	}
}
