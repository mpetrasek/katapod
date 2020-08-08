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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.sign.TestData;

class StampTest {

	@Test
	void test() throws IOException {
		Stamp stamp = new Stamp();
		Optional<String> usrname = Optional.empty();
		Optional<String> passwd = Optional.empty();
	
		Path testDir = TestData.TEST_DIR;
		Path signatureFile = testDir.resolve("Overeni_UOZI.txt.p7s");
		Files.write(signatureFile, "SignResult.getSignedData".getBytes());
	
		//Czech Post TSA URL =  "https://www3.postsignum.cz/TSS/TSS_user/"
		try {
			/*byte[] result = */ stamp.makeTimestamp(signatureFile.toFile(), "https://freetsa.org/tsr", usrname, passwd);
		} catch (Exception e) {
			fail("Should be OK");
		}
	}
}
