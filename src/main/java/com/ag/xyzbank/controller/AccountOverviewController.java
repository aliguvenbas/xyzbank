package com.ag.xyzbank.controller;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.service.AccountService;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import com.ag.xyzbank.service.validation.UserNotEligibleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class AccountOverviewController {
	private final AccountService accountService;

	public AccountOverviewController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("overview")
	public AccountOverviewDto getOverview(@RequestParam String token) {
		try {
			Account account = accountService.getByToken(token);
			return new AccountOverviewDto(account.getIban(), account.getAccountType(), account.getBalance(), account.getCurrency());
		} catch(UserExistanceException | UserNotEligibleException userExistenceException) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, userExistenceException.getMessage());
		} catch(TokenNotValidException ex) {
			throw new HttpServerErrorException(HttpStatus.FORBIDDEN, ex.getMessage());
		}
	}
}
