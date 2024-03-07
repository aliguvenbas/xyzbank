package com.ag.xyzbank.service;

import com.ag.xyzbank.controller.dto.UserDto;
import com.ag.xyzbank.repository.UserRepository;
import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.service.validation.AddressValidator;
import com.ag.xyzbank.service.validation.InvalidUserDataException;
import com.ag.xyzbank.service.validation.UserExistanceException;
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

	public UserService(AddressValidator addressValidator, UserRepository userRepository) {
		this.addressValidator = addressValidator;
		this.userRepository = userRepository;
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	// TODO transactional
	public User registerUser(UserDto userDto) {
		if(userDto != null) {
			User user = userRepository.findByUsername(userDto.getUsername());

			if(user != null) {
				throw UserExistanceException.userExist(userDto.getUsername());
			}

			if(!addressValidator.isUserInValidCountry(userDto.getAddress())) {
				throw new InvalidUserDataException("User is not in valid area");
			}

			var newUser = new User(userDto.getName(),
					userDto.getAddress(),
					userDto.getDateOfBirth(),
					userDto.getIdDocument(),
					userDto.getUsername(),
					PasswordUtil.generateRandomPassword(PASSWORD_LENGTH));

			try {
				return userRepository.save(newUser);
			} catch(Exception ex) {
				throw ex;
			}
		} else {
			throw new InvalidUserDataException("Invalid user data");
		}
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
