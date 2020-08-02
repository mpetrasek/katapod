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

import org.junit.jupiter.api.Test;

import cz.petrary.geo.katapod.sign.SignException;
import cz.petrary.geo.katapod.sign.SignResult;
import cz.petrary.geo.katapod.sign.TestData;
import cz.petrary.geo.katapod.stamp.StampException;

class KatapodImplTest {

	@Test
	void testSign() throws SignException {
		Katapod service = KatapodObjectFactory.getKatapod();
		SignConfiguration sconf = KatapodObjectFactory.configureSignature()
				.withCertificate(TestData.keystore())
				.andPassword("password")
				.build();
		SignResult result = service.signDir(TestData.testFiles(),TestData.NUMBER, sconf);
		
		assertEquals(TestData.correctTextContent(), result.getTextFile());
		 
		//write files to disk
		//Files.write("/path/to/dir/Overeni_UOZI.txt"), result.getTextFile().getBytes("UTF-8"));
		//Files.write("/path/to/dir/Overeni_UOZI.txt.p7s"), result.getSignedData());
		
	}

	
	@Test
	void testStamp() throws StampException {
		Katapod service = KatapodObjectFactory.getKatapod();
		StampConfiguration sconf = KatapodObjectFactory.configureStamp()
				.withTSA("https://freetsa.org/tsr")
				.build();
		
		 @SuppressWarnings("unused")
		byte[] result = service.stamp("SignResult.getSignedData".getBytes(), sconf);
		 
		 //write file to disk
		 //Files.write("/path/to/dir/Overeni_UOZI.txt.p7s.tsr"), result);
	}

}

