package com.ag.xyzbank.service.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddressValidatorTest {

	private final AddressValidator addressValidator = new AddressValidator();

	@ParameterizedTest
	@ValueSource(strings = {"Netherlands", "Belgium"})
	void isUserInValidCountry(String country) {

		assertTrue(addressValidator.isUserInValidCountry(country));
	}

	@Test
	void shouldANewCountryBeAddedToWhiteList() {

		addressValidator.addCountry("England");

		assertTrue(addressValidator.isUserInValidCountry("England"));
	}
}
