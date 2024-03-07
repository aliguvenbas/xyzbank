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
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.AddressValidator;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UserServiceTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final AddressValidator addressValidator = mock(AddressValidator.class);
	private final UserService userService = new UserService(addressValidator, userRepository);

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

		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(userArgumentCaptor.capture());

		User userArgumentCaptorValue = userArgumentCaptor.getValue();

		assertNotNull(userArgumentCaptorValue.getPassword());
		assertEquals("test-name", userArgumentCaptorValue.getName());
		assertEquals("test-addr", userArgumentCaptorValue.getAddress());
		assertEquals("12-12-2012", userArgumentCaptorValue.getDateOfBirth());
		assertEquals("id-document-details", userArgumentCaptorValue.getIdDocument());
		assertEquals("any-username", userArgumentCaptorValue.getUsername());
	}
}
