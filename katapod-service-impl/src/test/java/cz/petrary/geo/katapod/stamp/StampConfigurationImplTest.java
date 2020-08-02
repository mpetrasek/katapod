/**
 * Copyright (c) 2020 PETRARY s.r.o. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of KATAPOD.
 *
 *  KATAPOD is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KATAPOD is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KATAPOD.  If not, see <https://www.gnu.org/licenses/>.
 */
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
