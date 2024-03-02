package com.ag.xyzbank.service.validation;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.model.ValidationResponse;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
	private final UsernameValidator usernameValidator;
	private final AddressValidator addressValidator;
	private final UserAgeValidator userAgeValidator;

	public ValidationService(UsernameValidator usernameValidator, AddressValidator addressValidator, UserAgeValidator userAgeValidator) {
		this.usernameValidator = usernameValidator;
		this.addressValidator = addressValidator;
		this.userAgeValidator = userAgeValidator;
	}

	public ValidationResponse validateUserForRegistration(UserDto userDto) {
		if(!usernameValidator.isUsernameUniq(userDto.getUsername())) {
			return ValidationResponse.error("userName not uniq");
		}

		if(!addressValidator.isUserInValidCountry(userDto.getAddress())) {
			return ValidationResponse.error("user is not in valid area");
		}

		return ValidationResponse.success("successfully registered user");
	}

	public ValidationResponse validateUserForOverview(String username) {
		if(!userAgeValidator.isAgeValid(username)) {
			return ValidationResponse.error("user is not eligable to see overview");
		}

		return ValidationResponse.success("here is the overview");
	}
}
