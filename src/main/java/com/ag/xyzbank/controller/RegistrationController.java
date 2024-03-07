package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.service.UserService;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class RegistrationController {

	private final UserService userRegistrationService;

	public RegistrationController(UserService userRegistrationService) {
		this.userRegistrationService = userRegistrationService;
	}

	@PostMapping("register")
	public AuthCredentialsDto registerUser(@RequestBody UserDto userDto) throws HttpStatusCodeException {
		try {
			var registrationResponse = userRegistrationService.registerUser(userDto);

			return new AuthCredentialsDto(registrationResponse.getUsername(), registrationResponse.getPassword());
		} catch(UserExistanceException | InvalidUserDataException userExistanceException) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, userExistanceException.getMessage());
		} catch(Exception exception) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		}
	}
}
