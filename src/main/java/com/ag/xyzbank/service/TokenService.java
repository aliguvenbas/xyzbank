package com.ag.xyzbank.service;

import com.ag.xyzbank.repository.TokenRepository;
import com.ag.xyzbank.repository.data.Token;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

	private final UserService userService;
	private final TokenRepository tokenRepository;

	public TokenService(UserService userService, TokenRepository tokenRepository) {
		this.userService = userService;
		this.tokenRepository = tokenRepository;
	}

	public String createToken(String username, String password) {
		User user = userService.getUserByUsername(username);

		if(user == null) {
			throw UserExistanceException.userDoesntExist(username);
		}

		if(!user.getPassword().equals(password)) {
			throw new InvalidUserDataException("User credentials are not valid");
		}

		String token = UUID.randomUUID().toString();

		Token savedToken = tokenRepository.save(new Token(username, token, false));
		return savedToken.getToken();
	}

	public boolean activateToken(String token) {
		Token savedToken = tokenRepository.findByToken(token);

		if(savedToken == null) {
			throw new TokenNotValidException("Invalid token");
		}

		if(savedToken.isActive()) {
			log.info("Token is already active");
			return true;
		}

		savedToken.setActive(true);

		Token save = tokenRepository.save(savedToken);
		return save.isActive();
	}

	public String checkTokenStatus(String token) {
		Token savedToken = tokenRepository.findByToken(token);

		if(savedToken == null) {
			throw new TokenNotValidException("Invalid token");
		}

		if(savedToken.isActive()) {
			return savedToken.getUsername();
		} else {
			throw new TokenNotValidException("Token is not activated. Please login");
		}
	}
}
