package com.ag.xyzbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ag.xyzbank.repository.TokenRepository;
import com.ag.xyzbank.repository.data.Token;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.TokenNotValidException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class TokenServiceTest {

	private final UserService userService = mock(UserService.class);
	private final TokenRepository tokenRepository = mock(TokenRepository.class);
	private final TokenService tokenService = new TokenService(userService, tokenRepository);

	@Test
	public void shouldThrowExceptionIfUserDoesNotExist() {
		when(userService.getUserByUsername(any())).thenReturn(null);

		assertThrows(UserExistanceException.class, () -> tokenService.createToken("random-username", "random-pass"));
	}

	@Test
	public void shouldThrowExceptionIfUserPasswordIsNotSameWithTheExistedUser() {
		User existedUser = new User("some-name", "test-addr", "12-12-2012", "id-document-details", "existed-username", "some-password");

		when(userService.getUserByUsername(any())).thenReturn(existedUser);

		assertThrows(InvalidUserDataException.class, () -> tokenService.createToken("existed-username", "different-password"));

		verifyNoInteractions(tokenRepository);
	}

	@Test
	public void shouldCreateToken() {
		User existedUser = new User("some-name", "test-addr", "12-12-2012", "id-document-details", "existed-username", "some-password");

		when(userService.getUserByUsername(any())).thenReturn(existedUser);

		when(tokenRepository.save(any())).thenReturn(new Token("existed-username", "some-token", false));

		String savedToken = tokenService.createToken("existed-username", "some-password");

		assertNotNull(savedToken);

		ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
		verify(tokenRepository).save(tokenArgumentCaptor.capture());

		Token tokenArgumentCaptorValue = tokenArgumentCaptor.getValue();
		assertEquals("existed-username", tokenArgumentCaptorValue.getUsername());
		assertNotNull(tokenArgumentCaptorValue.getToken());
		assertFalse(tokenArgumentCaptorValue.isActive());
	}

	@Test
	public void shouldThrowExceptionIfTokenDoesNotExist() {
		when(tokenRepository.findByToken(any())).thenReturn(null);

		assertThrows(TokenNotValidException.class, () -> tokenService.activateToken("some-token"));
	}

	@Test
	public void shouldNotActivateTheTokenIfItIsAlreadyActivated() {
		var token = new Token("some-username", "some-token", true);

		when(tokenRepository.findByToken(any())).thenReturn(token);

		boolean tokenActivation = tokenService.activateToken("some-token");

		assertTrue(tokenActivation);

		verify(tokenRepository, times(1)).findByToken(any());
		verifyNoMoreInteractions(tokenRepository);
	}

	@Test
	public void shouldActivateTheToken() {
		when(tokenRepository.findByToken(any())).thenReturn(new Token("some-username", "some-token", false));

		when(tokenRepository.save(any())).thenReturn(new Token("some-username", "some-token", true));

		boolean tokenActivation = tokenService.activateToken("some-token");

		ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
		verify(tokenRepository).save(tokenArgumentCaptor.capture());

		Token tokenArgumentCaptorValue = tokenArgumentCaptor.getValue();
		assertEquals("some-username", tokenArgumentCaptorValue.getUsername());
		assertEquals("some-token", tokenArgumentCaptorValue.getToken());
		assertTrue(tokenArgumentCaptorValue.isActive());

		assertTrue(tokenActivation);
	}

	@Test
	public void shouldThrowExceptionIfTokenDoesNotExistDuringTokenValidation() {
		when(tokenRepository.findByToken(any())).thenReturn(null);

		assertThrows(TokenNotValidException.class, () -> tokenService.checkTokenStatus("some-token"));
	}

	@Test
	public void shouldReturnUsernameIfTheTokenActive() {
		var token = new Token("some-username", "some-token", true);

		when(tokenRepository.findByToken(any())).thenReturn(token);

		String username = tokenService.checkTokenStatus("some-token");

		assertEquals("some-username", username);
	}

	@Test
	public void shouldThrowExceptionIfIfTheTokenIsNotActive() {
		var token = new Token("some-username", "some-token", false);

		when(tokenRepository.findByToken(any())).thenReturn(token);

		assertThrows(TokenNotValidException.class, () -> tokenService.checkTokenStatus("some-token"));

	}
}
