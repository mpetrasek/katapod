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
