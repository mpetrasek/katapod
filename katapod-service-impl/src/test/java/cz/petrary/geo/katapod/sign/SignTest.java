package cz.petrary.geo.katapod.sign;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SignTest {

	@Test
	void test() {
		try {
			Sign sign = new Sign(TestData.keystore(), TestData.PASSWORD.toCharArray());
			/*byte[] result = */ sign.sign("Sign me!");
		} catch (SignException e) {
			fail("Should be OK");
		}
	}

	
	
}
