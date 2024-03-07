package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.service.TokenService;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class TokenController {
	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("token")
	public String getToken(@RequestBody AuthCredentialsDto authCredentialsDto) {
		try {
			return tokenService.createToken(authCredentialsDto.getUsername(), authCredentialsDto.getPassword());
		} catch(InvalidUserDataException userExistenceException) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, userExistenceException.getMessage());
		} catch(Exception exception) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		}
	}

	@GetMapping("logon")
	public String logon(@RequestParam String token) {
		try {
			tokenService.activateToken(token);
			return "Successfully login";
		} catch(TokenNotValidException ex) {
			throw new HttpServerErrorException(HttpStatus.FORBIDDEN, ex.getMessage());
		} catch(Exception exception) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		}
	}
}
