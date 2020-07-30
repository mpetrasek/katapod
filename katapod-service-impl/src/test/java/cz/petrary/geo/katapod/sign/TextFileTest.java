package cz.petrary.geo.katapod.sign;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class TextFileTest {

	@Test
	void test() {
		try {
			List<String> hashes = HashHelper.hashForStreams(TestData.testFiles());
			String result = TextFile.create(TestData.OUZI_NAME,TestData.NUMBER, hashes);
			assertEquals(TestData.correctTextContent(), result);
			//System.out.println(result);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

}
