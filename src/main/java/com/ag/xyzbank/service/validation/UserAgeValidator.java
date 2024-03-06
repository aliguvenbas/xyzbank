package com.ag.xyzbank.service.validation;

import com.ag.xyzbank.repository.data.User;
import com.ag.xyzbank.repository.UserRepository;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class UserAgeValidator {

	private final UserRepository userRepository;

	public UserAgeValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
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
