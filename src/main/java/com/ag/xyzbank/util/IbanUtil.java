package com.ag.xyzbank.util;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class IbanUtil {
	public static String generateNetherlandsIBAN() {
		// Country code and check digits
		String countryCode = "NL";
		String checkDigits = "00";

		// Generate a random account number
		String accountNumber = generateRandomNumberString(10); // 10 digits for Dutch IBAN

		// Concatenate country code, check digits, and account number
		String ibanBase = accountNumber + countryCode + checkDigits;

		// Calculate checksum
		int remainder = 98 - (new java.math.BigInteger(ibanBase).mod(new java.math.BigInteger("97"))).intValue();
		String checksum = String.format("%02d", remainder);

		// Final IBAN
		String iban = countryCode + checksum + accountNumber;

		return iban;
	}

	private static String generateRandomNumberString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
