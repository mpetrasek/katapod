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
package cz.petrary.geo.katapod.textfile;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.sign.TestData;

class TextFileTest {

	@Test
	void test() {
		try {
			BaseDirHelper.prune(TestData.TEST_DIR);
			List<String> hashes = HashHelper.hashForAllFiles(TestData.TEST_DIR);
			String result = TextFile.create(TestData.OUZI_NAME,TestData.NUMBER, hashes);
			assertEquals(TestData.correctTextContent(), result);
			//System.out.println(result);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

}
