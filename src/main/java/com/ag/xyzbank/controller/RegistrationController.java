package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.model.ValidationResponse;
import com.ag.xyzbank.service.UserService;
import com.ag.xyzbank.service.validation.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class RegistrationController {

	private final ValidationService validationService;
	private final UserService userRegistrationService;

	public RegistrationController(ValidationService validationService, UserService userRegistrationService) {
		this.validationService = validationService;
		this.userRegistrationService = userRegistrationService;
	}

	@PostMapping("register")
	public AuthCredentialsDto registerUser(@RequestBody UserDto userDto) throws HttpStatusCodeException {

		ValidationResponse validation = validationService.validateUserForRegistration(userDto);

		if(validation.isValid()) {
			var registrationResponse = userRegistrationService.registerUser(userDto);
			return registrationResponse;
		} else {
			//TODO put more details
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, validation.getMessage());
		}
	}
}
