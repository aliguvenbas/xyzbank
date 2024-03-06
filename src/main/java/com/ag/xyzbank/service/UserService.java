package com.ag.xyzbank.service;

import com.ag.xyzbank.controller.dto.AuthCredentialsDto;
import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.UserRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.util.IbanUtil;
import com.ag.xyzbank.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private static final int PASSWORD_LENGTH = 10;

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

	public UserService(UserRepository userRepository, AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
	}

	// TODO transactional
	public AuthCredentialsDto registerUser(UserDto userDto) {
		var user = new User(userDto.getUsername(),
				userDto.getAddress(),
				userDto.getDateOfBirth(),
				userDto.getID_document(),
				userDto.getUsername(),
				PasswordUtil.generateRandomPassword(PASSWORD_LENGTH));

		userRepository.save(user);

		assignIBANTo(userDto.getUsername());

		return new AuthCredentialsDto(user.getUsername(), user.getPassword());
	}


	private void assignIBANTo(String username) {

		String iban = IbanUtil.generateNetherlandsIBAN();

		accountRepository.save(new Account(username, iban, 0, AccountType.TYPE1, Currency.EUR));
	}
}
