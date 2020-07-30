/**
 * Copyright (c) 2020 PETRARY s.r.o. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This file is part of @XXX@.
 *
 *  @XXX@ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Foobar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with @XXX@.  If not, see <https://www.gnu.org/licenses/>.
 */

package cz.petrary.geo.katapod.sign;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.SignConfiguration;

class SignConfigurationImplTest {

	@Test
	void testValidConfig() throws SignException {
		InputStream certificate = new ByteArrayInputStream("mock certificate data".getBytes());
		
		SignConfiguration sconf = new SignConfigurationImpl.Builder()
										.withCertificate(certificate)
										.andPassword("password")
										.keepOldResultFiles()
										.build();
		assertNotNull(sconf);
	}

	@Test
	void testInValidCertificate() {
		//no certificate definition at all
		Assertions.assertThrows(SignException.class, () -> { new SignConfigurationImpl.Builder().andPassword("password").build();    });

		//empty certificate inputstream
		Assertions.assertThrows(SignException.class, () -> { new SignConfigurationImpl.Builder().withCertificate(null).andPassword("password").build();    });
		
		//empty certificate file
		Assertions.assertThrows(SignException.class, () -> { new SignConfigurationImpl.Builder().withCertificateFile(" ").andPassword("password").build();    });
	}

	@Test
	void testInValidPassword() {
		InputStream certificate = new ByteArrayInputStream("mock certificate data".getBytes());
		//no password defined
		Assertions.assertThrows(SignException.class, () -> { new SignConfigurationImpl.Builder().withCertificate(certificate).build();    });
		
		//empty password
		Assertions.assertThrows(SignException.class, () -> { new SignConfigurationImpl.Builder().withCertificate(certificate).andPassword(" ").build();    });
	}

}
