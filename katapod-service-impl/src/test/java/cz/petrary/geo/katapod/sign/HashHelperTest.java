package cz.petrary.geo.katapod.sign;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class HashHelperTest {

	@Test
	void testOneHash() {
		ByteArrayInputStream bais = new ByteArrayInputStream("Hash me!\n".getBytes());
		//echo "Hash me!" | sha512sum | awk '{ print toupper($0) }'
		//sha512sum test.txt | sed 's/^\([a-z0-9]*\)\ \ \([a-zA-Z0-9_\.]*\)/\2\U;\1/'
		try {
			String result = HashHelper.countStreamHash(bais, "test.txt", HashHelper.getMessageDigest());
			assertEquals("test.txt;4C4A59A7D958CFF035EB7D752B319B90337037E352B89D58AA5BE29EF051EA0A565133781AA3279384654274C4786893287EA8037F0A1A15D9B40EA0BC4BE73C",
					result);
		} catch (IOException e) {
			fail("Wrong hash");
		}
	}

}
