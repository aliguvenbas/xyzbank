package com.ag.xyzbank.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IbanUtilTest {

	@Test
	void generateNetherlandsIBAN() {
		String s = IbanUtil.generateNetherlandsIBAN();

		System.out.printf(""+s);
	}
}
