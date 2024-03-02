package com.ag.xyzbank.service;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
	public AuthCredentialsDto registerUser(UserDto userDto) {

		// register user successfully
		String randomPassword = generateRandomPassword();
		// save user

		assignIBANTo(userDto.getUsername());



		return new AuthCredentialsDto("new user","fake-pass");
	}

	private String generateRandomPassword() {
		return "random-pass";
	}

	private String assignIBANTo(String username){
		// create iban
		// validate it
		// save it
		return "new -iban";
	}
}
