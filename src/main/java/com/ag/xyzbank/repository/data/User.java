package com.ag.xyzbank.repository.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String address;
	private String dateOfBirth;//dd-mm-yyyy
	//TODO document
	private String ID_document;
	private String username;
	private String password;

	User() {
	}

	public User(String name, String address, String dateOfBirth, String ID_document, String username, String password) {

		this.name = name;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
		this.ID_document = ID_document;
		this.username = username;
		this.password = password;
	}
}
