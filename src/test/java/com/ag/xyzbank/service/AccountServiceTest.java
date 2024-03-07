package com.ag.xyzbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.UserExistanceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AccountServiceTest {
	private final UserService userService = mock(UserService.class);
	private final TokenService tokenService = mock(TokenService.class);
	private final AccountRepository accountRepository = mock(AccountRepository.class);
	private final AccountService accountService = new AccountService(userService, tokenService, accountRepository);

	@Test
	public void shouldCreateAccount() {
		accountService.createAccountFor("some-username");

		ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
		verify(accountRepository).save(accountArgumentCaptor.capture());

		Account accountArgumentCaptorValue = accountArgumentCaptor.getValue();
		assertNotNull(accountArgumentCaptorValue.getIban());
		assertEquals("some-username", accountArgumentCaptorValue.getUsername());
		assertEquals(0, accountArgumentCaptorValue.getBalance());
		assertEquals(AccountType.TYPE1, accountArgumentCaptorValue.getAccountType());
		assertEquals(Currency.EUR, accountArgumentCaptorValue.getCurrency());
	}

	@Test
	public void shouldGetTheAccountByToken() {
		when(tokenService.checkTokenStatus(any())).thenReturn("some-username");

		when(userService.getUserByUsername(any())).thenReturn(
				new User("some-name", "test-addr", "12-12-2012",
						"id-document-details", "some-username", "some-password"));
		when(userService.isAgeValid(any())).thenReturn(true);

		accountService.getByToken("some-token");

		verify(accountRepository).findByUsername("some-username");
	}

	@Test
	public void shouldThrowExceptionIfUserDoesNotExistByToken() {
		when(tokenService.checkTokenStatus(any())).thenReturn("some-username");

		when(userService.getUserByUsername(any())).thenReturn(null);

		assertThrows(UserExistanceException.class, () -> accountService.getByToken("some-token"));

		verifyNoInteractions(accountRepository);
		verify(userService, times(1)).getUserByUsername(any());
		verifyNoMoreInteractions(userService);
	}
}
