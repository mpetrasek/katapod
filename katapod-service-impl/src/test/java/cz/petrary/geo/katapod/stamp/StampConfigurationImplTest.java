package cz.petrary.geo.katapod.stamp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.StampConfiguration;

class StampConfigurationImplTest {

	@Test
	void testValidConfig() throws StampException {
		StampConfiguration sconf = new StampConfigurationImpl.Builder()
										.withTSA("https://freetsa.org/tsr")
										.andUser("me")
										.andPassword("password")
										.build();
		assertEquals("https://freetsa.org/tsr", sconf.getUrl().toString());
		assertEquals("me", sconf.getUserName().get());
		assertEquals("password", sconf.getUserPasswd().get());

	}

	@Test
	void testValidConfig2() throws StampException {
		StampConfiguration sconf = new StampConfigurationImpl.Builder()
				.withTSA("https://freetsa.org/tsr")
				.build();
		assertEquals("https://freetsa.org/tsr", sconf.getUrl().toString());
		assertTrue(sconf.getUserName().isEmpty());
		assertTrue(sconf.getUserPasswd().isEmpty());
	}

	@Test
	void testInvalidURL() {
		//invalid url
		Assertions.assertThrows(StampException.class, () -> { new StampConfigurationImpl.Builder().build();    });
		Assertions.assertThrows(StampException.class, () -> { new StampConfigurationImpl.Builder().withTSA("invalid address").build();    });
	}

}
