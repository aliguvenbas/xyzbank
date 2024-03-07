package com.ag.xyzbank.controller.dto;

import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import lombok.Value;

@Value
public class AccountOverviewDto {
	private String accountNumber; // IBAN
	private AccountType accountType;
	private float balance;
	private Currency currency;
}
