package com.ag.xyzbank.repository.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String username;
	private String token;
	private boolean active;

	Token() {
	}

	public Token(String username, String token, boolean active) {
		this.username = username;
		this.token = token;
		this.active = active;
	}
}
