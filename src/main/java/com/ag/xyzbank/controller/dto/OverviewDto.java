package com.ag.xyzbank.controller.dto;

import lombok.Value;

@Value
public class OverviewDto {
	private String accountNumber; // IBAN
	// TODO Enum
	private String accountType;
	private float balance;
	// TODO Enum
	private String currency;

}
