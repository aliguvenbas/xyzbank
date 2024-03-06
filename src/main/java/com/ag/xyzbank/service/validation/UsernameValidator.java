package com.ag.xyzbank.service.validation;

import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UsernameValidator {

	private final UserRepository userRepository;

	public UsernameValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public boolean isUsernameUniq(String username){
		User user = userRepository.findByUsername(username);

		return user == null;
	}
}
