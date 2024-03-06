package com.ag.xyzbank.service;

import com.ag.xyzbank.repository.TokenRepository;
import com.ag.xyzbank.repository.data.Token;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

	private final TokenRepository tokenRepository;

	public TokenService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	public boolean activateToken(String token) {
		Token savedToken = tokenRepository.findByToken(token);

		if(savedToken == null) {
			//TODO warning
		}

		if(savedToken.isActive()) {
			// TODO info
		}

		savedToken.setActive(true);

		Token save = tokenRepository.save(savedToken);
		return save.isActive();
	}

	public String createToken(String username) {
		String token = UUID.randomUUID().toString();
		Token savedToken = tokenRepository.save(new Token(username, token, false));
		return savedToken.getToken();
	}

	public String checkTokenStatus(String token) {
		Token savedToken = tokenRepository.findByToken(token);

		if(savedToken.isActive()){
			return savedToken.getUsername();
		}else {
			return null;
		}
	}
}
