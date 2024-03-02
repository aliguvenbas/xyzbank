package com.ag.xyzbank.service.validation;

import org.springframework.stereotype.Service;

@Service
public class UsernameValidator {
	public boolean isUsernameUniq(String username){
		return true;
	}
}
