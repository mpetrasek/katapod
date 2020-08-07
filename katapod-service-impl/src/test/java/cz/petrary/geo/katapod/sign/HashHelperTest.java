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

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class HashHelperTest {

	@Test
	void testOneHash() {
		Path testFile = TestData.TEST_DIR.resolve("soubor0.txt");
		try {
			String result = HashHelper.countHash(testFile.toFile(), HashHelper.getMessageDigest());
			assertEquals("soubor0.txt;E42C5E45751ABC4DB025B60BA3E477737B3A85C0633AFCDA90D3C68E441B5B8A9038B11065944DE961CA09454FC287BF39CFD3AE802FAEC5776F84AFC1C3854D",
					result);
		} catch (IOException e) {
			fail("Wrong hash");
		}
	}

}
