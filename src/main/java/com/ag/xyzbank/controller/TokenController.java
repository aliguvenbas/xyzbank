package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.service.TokenService;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TokenController {
	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("token")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = AccountOverviewDto.class))}),
			@ApiResponse(responseCode = "400", description = "User credentials are not valid", content = @Content)})
	public String getToken(@RequestBody AuthCredentialsDto authCredentialsDto) {
		try {
			return tokenService.createToken(authCredentialsDto.getUsername(), authCredentialsDto.getPassword());
		} catch(InvalidUserDataException userExistenceException) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userExistenceException.getMessage());
		}
	}

	@GetMapping("logon")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = AccountOverviewDto.class))}),
			@ApiResponse(responseCode = "403", description = "Invalid token", content = @Content)})
	public String logon(@RequestParam String token) {
		try {
			tokenService.activateToken(token);
			return "Successfully login";
		} catch(TokenNotValidException ex) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
		}
	}
}
