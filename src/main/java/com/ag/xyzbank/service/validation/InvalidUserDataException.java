package com.ag.xyzbank.service.validation;

import lombok.NonNull;

public class InvalidUserDataException extends RuntimeException{

	public InvalidUserDataException(@NonNull String message) {
		super(message);
	}
}
