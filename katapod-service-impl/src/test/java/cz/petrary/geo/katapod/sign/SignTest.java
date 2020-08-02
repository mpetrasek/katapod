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
