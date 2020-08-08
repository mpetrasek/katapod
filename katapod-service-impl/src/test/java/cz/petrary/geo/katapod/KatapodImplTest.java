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
package cz.petrary.geo.katapod;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;


import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.sign.TestData;


class KatapodImplTest {

	@Test
	void testSign() throws Exception {
		Path testDir = TestData.TEST_DIR;
		Katapod service = new KatapodImpl();
		service.setConfiguration(new TestData());
		Path textFilePath = service.createTextFile(testDir, TestData.NUMBER);
		service.signDir(textFilePath);
		try (FileInputStream fis = new FileInputStream(textFilePath.toFile())) {
			String text = new String(fis.readAllBytes(),"UTF-8");
			assertEquals(TestData.correctTextContent(), text);
		}
		 
	}

	
	@Test
	void testStamp() throws Exception {
		Katapod service = new KatapodImpl();
		service.setConfiguration(new TestData());
		
		Path testDir = TestData.TEST_DIR;
		Path signatureFile = testDir.resolve("Overeni_UOZI.txt.p7s");
		Files.write(signatureFile, "SignResult.getSignedData".getBytes());

		service.stamp(signatureFile);
	}

}

