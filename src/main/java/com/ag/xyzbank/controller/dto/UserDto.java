package com.ag.xyzbank.controller.dto;

import lombok.Value;

//TODO record instead of dto??
@Value
public class UserDto {
	private String name;
	private String address;
	private String dateOfBirth;
	//TODO document
	private String ID_document;
	private String username;
}
