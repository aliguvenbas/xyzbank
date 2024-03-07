package com.ag.xyzbank.controller.dto;

import lombok.Value;

@Value
public class UserDto {
	private String name;
	private String address;
	private String dateOfBirth;
	//TODO document
	private String idDocument;
	private String username;
}
