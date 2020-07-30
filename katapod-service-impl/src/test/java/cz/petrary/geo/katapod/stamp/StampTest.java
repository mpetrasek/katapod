package cz.petrary.geo.katapod.stamp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class StampTest {

	@Test
	void test() {
		Stamp stamp = new Stamp();
		Optional<String> usrname = Optional.empty();
		Optional<String> passwd = Optional.empty();
		
		//Czech Post TSA URL =  "https://www3.postsignum.cz/TSS/TSS_user/"
		try {
			/*byte[] result = */ stamp.makeTimestamp("Timestamp me!".getBytes(), "https://freetsa.org/tsr", usrname, passwd);
		} catch (StampException e) {
			fail("Should be OK");
		}
	}
}
