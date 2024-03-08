package com.ag.xyzbank.service;

import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import com.ag.xyzbank.service.validation.UserExistanceException;
import com.ag.xyzbank.service.validation.UserNotEligibleException;
import com.ag.xyzbank.util.IbanUtil;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	private static final int MAX_IBAN_CREATION_RETRY_COUNT = 10;
	private final UserService userService;
	private final TokenService tokenService;
	private final AccountRepository accountRepository;

	public AccountService(UserService userService, TokenService tokenService, AccountRepository accountRepository) {
		this.userService = userService;
		this.tokenService = tokenService;
		this.accountRepository = accountRepository;
	}

	public void createAccountFor(String username) {
		String iban = IbanUtil.generateNetherlandsIBAN();

		// Since we have uuid as an iban, it will be created at the first time
		// and it will be uniq
		// But if I have a decent iban for NL, in order to prevent duplicate ibans
		// I added this logic. Maybe too over-engineering
		Account existedIban;
		int ibanCreationRetryAccount = 0;
		do {
			existedIban = accountRepository.findByIban(iban);
			ibanCreationRetryAccount++;
			if(ibanCreationRetryAccount == MAX_IBAN_CREATION_RETRY_COUNT) {
				throw new RuntimeException("Iban can not be created");
			}
		} while(existedIban != null && ibanCreationRetryAccount < MAX_IBAN_CREATION_RETRY_COUNT);

		accountRepository.save(new Account(username, iban, 0, AccountType.TYPE1, Currency.EUR));
	}

	public Account getByToken(String token) {
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
