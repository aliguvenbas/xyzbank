package com.ag.xyzbank.service;

import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.service.validation.UserExistanceException;
import com.ag.xyzbank.service.validation.UserNotEligibleException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	private final UserService userService;
	private final TokenService tokenService;
	private final AccountRepository accountRepository;

	public AccountService(UserService userService, TokenService tokenService, AccountRepository accountRepository) {
		this.userService = userService;
		this.tokenService = tokenService;
		this.accountRepository = accountRepository;
	}

	public void save(Account account) {
		accountRepository.save(account);
	}

	public Account getBytoken(String token) {
		var username = tokenService.checkTokenStatus(token);

		var user = userService.getUserByUsername(username);

		if(user == null) {
			throw UserExistanceException.userDoesntExist(username);
		}

		if(!userService.isAgeValid(user.getUsername())) {
			throw new UserNotEligibleException("User " + user.getUsername() + " is under age 18. The account overview can not be shown");
		}

		return accountRepository.findByUsername(username);
	}
}
