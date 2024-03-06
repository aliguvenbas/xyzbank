package com.ag.xyzbank.service;

import com.ag.xyzbank.controller.dto.AccountOverviewDto;
import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.data.Account;
import org.springframework.stereotype.Service;

@Service
public class OverviewCalculator {
	private final AccountRepository accountRepository;

	public OverviewCalculator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account calculate(String username) {
		return accountRepository.findByUsername(username);}
}
