package com.ag.xyzbank.service.validation;

import lombok.NonNull;

public class TokenNotValidException extends RuntimeException{

	public TokenNotValidException(@NonNull String message) {
		super(message);
	}
}
