package com.ag.xyzbank.model;

import lombok.Value;

@Value
public class User {
	private String name;
	private String address;
	private String dateOfBirth;
	//TODO document
	private String ID_document;
	private String username;
	private String password;
	private String iban;
}
