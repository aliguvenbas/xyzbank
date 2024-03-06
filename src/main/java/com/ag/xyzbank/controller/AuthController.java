package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.model.ValidationResponse;
import com.ag.xyzbank.service.TokenService;
import com.ag.xyzbank.service.UserService;
import com.ag.xyzbank.service.validation.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
@RequestMapping("/api")
public class AuthController {

	private final ValidationService validationService;
	private final UserService userRegistrationService;
	private final TokenService tokenService;

	public AuthController(ValidationService validationService, UserService userRegistrationService, TokenService tokenService) {
		this.validationService = validationService;
		this.userRegistrationService = userRegistrationService;
		this.tokenService = tokenService;
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

	@PostMapping("token")
	public String getToken(@RequestBody AuthCredentialsDto authCredentialsDto) {

		ValidationResponse validation = validationService.validateUserForAuth(
				authCredentialsDto.getUsername(),
				authCredentialsDto.getPassword());
		if(validation.isValid()) {
			return tokenService.createToken(authCredentialsDto.getUsername());
		} else {
			//TODO put more details
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, validation.getMessage());
		}
	}

	@GetMapping("logon")
	public String validateToken(@RequestParam String token) {
		var b = tokenService.activateToken(token);
		if(b) {
			return "Successfully login";
		} else {
			return "invalid token";
		}
	}

}
