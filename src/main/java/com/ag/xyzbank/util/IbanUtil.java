package com.ag.xyzbank.util;

import java.util.Random;
import java.util.UUID;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

@Service
public class IbanUtil {

	//TODO what??
	private static String generateRandomBankAccountNumber() {
		// Generate a random 9-digit bank account number
		Random random = new Random();
		StringBuilder bankAccountNumber = new StringBuilder();
		for(int i = 0; i < 9; i++) {
			bankAccountNumber.append(random.nextInt(10));
		}
		return bankAccountNumber.toString();
	}

	private static String generateIBAN(String bankAccountNumber) {
		// IBAN generation logic for Netherlands (NL)
		String countryCode = "NL";
		String bban = "0000" + bankAccountNumber; // Add zeros to match the length of the BBAN
		String ibanBase = bban + countryCode + "00"; // Add 00 for the check digits

		// Calculate the check digits (kk)
		int mod97 = new java.math.BigInteger(ibanBase).mod(new java.math.BigInteger("97")).intValue();
		int checksum = 98 - mod97;

		// Format the checksum to have leading zeros if necessary
		String formattedChecksum = String.format("%02d", checksum);

		// Construct the final IBAN
		return countryCode + formattedChecksum + bankAccountNumber;
	}

	public static String generateNetherlandsIBAN() {
//		String bankAccountNumber = generateRandomBankAccountNumber();
//		System.out.println("Generated bankAccountNumber: " + bankAccountNumber);
//
//		Iban iban = new Iban.Builder()
//				.countryCode(CountryCode.NL)
//				.bankCode("XYZBN")
//				.accountNumber(bankAccountNumber)
//				.build();
//
//		System.out.println("Generated IBAN: " + iban.toString());

		return UUID.randomUUID().toString();
	}
}
