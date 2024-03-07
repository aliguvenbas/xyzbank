package com.ag.xyzbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.repository.UserRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.AddressValidator;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.OptimisticLockingFailureException;

class UserServiceTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final AddressValidator addressValidator = mock(AddressValidator.class);
	private final AccountService accountService = mock(AccountService.class);
	private final UserService userService = new UserService(addressValidator, userRepository, accountService);

	@Test
	public void shouldCallUserRepositoryForQueryingByUsername() {
		when(userRepository.findByUsername(any())).thenReturn(any(User.class));

		userService.getUserByUsername("any-username");

		verify(userRepository, times(1)).findByUsername(eq("any-username"));
	}

	@Test
	public void shouldThrowExceptionUserIfUsernameIsAlreadyExist() {
		User existedUserWithSameUsername =
				new User("some-name", "test-addr", "12-12-2012", "id-document-details", "existed-username", "some-password");
		when(userRepository.findByUsername(any())).thenReturn(existedUserWithSameUsername);

		UserDto userDto = new UserDto("test-name", "test-addr", "12-12-2012", "id-document-details", "existed-username");

		assertThrows(UserExistanceException.class, () -> userService.registerUser(userDto));

		verifyNoInteractions(addressValidator);
	}

	@Test
	public void shouldThrowExceptionUserAddressIsInInvalidCountryList() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		when(addressValidator.isUserInValidCountry(any())).thenReturn(false);

		UserDto userDto = new UserDto("test-name", "test-addr", "12-12-2012", "id-document-details", "any-username");

		assertThrows(InvalidUserDataException.class, () -> userService.registerUser(userDto));

		verify(userRepository, times(1)).findByUsername(eq("any-username"));
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldThrowExceptionIfUserDtoIsNull() {
		assertThrows(InvalidUserDataException.class, () -> userService.registerUser(null));

		verifyNoInteractions(addressValidator);
		verifyNoInteractions(userRepository);
	}

	@Test
	public void shouldRegisterUser() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		when(addressValidator.isUserInValidCountry(any())).thenReturn(true);

		UserDto userDto = new UserDto("test-name", "test-addr", "12-12-2012", "id-document-details", "any-username");

		User user = userService.registerUser(userDto);

		assertNotNull(user.getPassword());
		assertEquals("test-name", user.getName());
		assertEquals("test-addr", user.getAddress());
		assertEquals("12-12-2012", user.getDateOfBirth());
		assertEquals("id-document-details", user.getIdDocument());
		assertEquals("any-username", user.getUsername());

		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(userArgumentCaptor.capture());

		assertEquals(user, userArgumentCaptor.getValue());

		ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
		verify(accountService).save(accountArgumentCaptor.capture());

		Account accountArgumentCaptorValue = accountArgumentCaptor.getValue();
		assertNotNull(accountArgumentCaptorValue.getIban());
		assertEquals("any-username", accountArgumentCaptorValue.getUsername());
		assertEquals(0, accountArgumentCaptorValue.getBalance());
		assertEquals(AccountType.TYPE1, accountArgumentCaptorValue.getAccountType());
		assertEquals(Currency.EUR, accountArgumentCaptorValue.getCurrency());
	}

	@Test
	public void shouldNotCreateAccountIfSthHappensWrongDuringUserCreation(){
		when(userRepository.findByUsername(any())).thenReturn(null);

		when(addressValidator.isUserInValidCountry(any())).thenReturn(true);

		when(userRepository.save(any())).thenThrow(OptimisticLockingFailureException.class);

		UserDto userDto = new UserDto("test-name", "test-addr", "12-12-2012", "id-document-details", "any-username");

		assertThrows(Exception.class, () -> userService.registerUser(userDto));

		verifyNoInteractions(accountService);
	}

}
