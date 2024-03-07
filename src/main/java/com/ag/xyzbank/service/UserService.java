package com.ag.xyzbank.service;

import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.repository.AccountRepository;
import com.ag.xyzbank.repository.UserRepository;
import com.ag.xyzbank.repository.data.Account;
import com.ag.xyzbank.repository.data.AccountType;
import com.ag.xyzbank.repository.data.Currency;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.AddressValidator;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
import com.ag.xyzbank.util.IbanUtil;
import com.ag.xyzbank.util.PasswordUtil;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private static final int PASSWORD_LENGTH = 10;

	private final AddressValidator addressValidator;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

	public UserService(AddressValidator addressValidator, UserRepository userRepository, AccountRepository accountRepository) {
		this.addressValidator = addressValidator;
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	// TODO transactional
	public User registerUser(UserDto userDto) {
		User user = userRepository.findByUsername(userDto.getUsername());

		if(user != null) {
			throw UserExistanceException.userExist(userDto.getUsername());
		}

		if(!addressValidator.isUserInValidCountry(user.getAddress())) {
			throw new InvalidUserDataException("user is not in valid area");
		}

		var newUser = new User(userDto.getUsername(),
				userDto.getAddress(),
				userDto.getDateOfBirth(),
				userDto.getID_document(),
				userDto.getUsername(),
				PasswordUtil.generateRandomPassword(PASSWORD_LENGTH));

		userRepository.save(newUser);

		assignIBANTo(userDto.getUsername());

		return newUser;
	}

	private void assignIBANTo(String username) {
		String iban = IbanUtil.generateNetherlandsIBAN();

		accountRepository.save(new Account(username, iban, 0, AccountType.TYPE1, Currency.EUR));
	}

	public boolean isAgeValid(String username) {
		User user = userRepository.findByUsername(username);

		String dateOfBirth = user.getDateOfBirth();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(birthDate, currentDate);
		int age = period.getYears();

		return age >= 18;
	}
}
