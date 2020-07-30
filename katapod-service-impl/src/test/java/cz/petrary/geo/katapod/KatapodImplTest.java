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

