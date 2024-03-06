package com.ag.xyzbank.service.validation;

import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.model.ValidationResponse;
import org.springframework.stereotype.Service;

//TODO service interfaces can be used
@Service
public class ValidationService {
	private final UserValidator userValidator;
	private final AddressValidator addressValidator;

	public ValidationService(UserValidator usernameValidator, AddressValidator addressValidator) {
		this.userValidator = usernameValidator;
		this.addressValidator = addressValidator;
	}

	public ValidationResponse validateUserForRegistration(UserDto userDto) {
		if(!userValidator.isUsernameUniq(userDto.getUsername())) {
			return ValidationResponse.error("userName not uniq");
		}

		if(!addressValidator.isUserInValidCountry(userDto.getAddress())) {
			return ValidationResponse.error("user is not in valid area");
		}

		return ValidationResponse.success("successfully registered user");
	}

	public ValidationResponse validateUserForOverview(String username) {
		if(!userValidator.isAgeValid(username)) {
			return ValidationResponse.error("user is not eligable to see overview");
		}

		return ValidationResponse.success("here is the overview");
	}

	public ValidationResponse validateUserForAuth(String username, String password) {
		if(!userValidator.isCredentialsValid(username,password)) {
			return ValidationResponse.error("user credentials are not valid");
		}

		return ValidationResponse.success("user credentials are valid");
	}
}
