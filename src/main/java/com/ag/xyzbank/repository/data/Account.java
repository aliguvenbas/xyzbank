package com.ag.xyzbank.repository.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String username;
	private String iban;
	private float balance;
	private AccountType accountType;

	public Account() {
	}

	public Account(String username, String iban, float balance, AccountType accountType) {
		this.username = username;
		this.iban = iban;
		this.balance = balance;
		this.accountType = accountType;
	}
}
