package com.ag.xyzbank.service.validation;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class AddressValidator {

	private final Set<String> availableCountries = Set.of("Netherlands","Belgium");

	public boolean isUserInValidCountry(String address) {
		return availableCountries.stream()
				.anyMatch(country -> address.contains(country));
	}

	public void addCountry(String country) {
		availableCountries.add(country);
	}
}
