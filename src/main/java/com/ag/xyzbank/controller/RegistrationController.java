package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.service.AccountService;
import com.ag.xyzbank.service.UserService;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RegistrationController {

	private final UserService userRegistrationService;
	private final AccountService accountService;

	public RegistrationController(UserService userRegistrationService, AccountService accountService) {
		this.userRegistrationService = userRegistrationService;
		this.accountService = accountService;
	}

	@PostMapping("register")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = AccountOverviewDto.class))}),
			@ApiResponse(responseCode = "400", description = "User does exist", content = @Content)})
	public AuthCredentialsDto registerUser(@RequestBody UserDto userDto) throws HttpStatusCodeException {
		try {
			var registrationResponse = userRegistrationService.registerUser(userDto);

			accountService.createAccountFor(registrationResponse.getUsername());

			return new AuthCredentialsDto(registrationResponse.getUsername(), registrationResponse.getPassword());
		} catch(UserExistanceException | InvalidUserDataException userExistanceException) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userExistanceException.getMessage());
		}
	}
}
