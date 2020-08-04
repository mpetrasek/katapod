package cz.petrary.geo.katapod.cliclient;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;


class ConfigValuesTest {

	@Test
	void test() throws IOException {
		//priprava
		Path root = Paths.get("/tmp/katacli");
		if (!Files.exists(root)) {
			Files.createDirectory(root);
		}
		File myCfgFile = Paths.get("/tmp/katacli/myconfig.properties").toFile();
		File certFile = Paths.get("/tmp/katacli/podpis.p12").toFile();
		
		writeTestCfg(myCfgFile, false);
		writeTestCert(certFile);
		
		//vlastni test
		ConfigValues cfg = new ConfigValues(Optional.of(myCfgFile));
		cfg.readProperties(new StringReader(testData(true)));
		
		performTests(cfg);
		
		//zapis hodnoty a zkus je znovu precist
		cfg.store();
		cfg = new ConfigValues(Optional.of(myCfgFile));
		performTests(cfg);
		
	}

	private void performTests(ConfigValues cfg) {
		assertEquals("/tmp/katacli/podpis.p12", cfg.getCertificateFile().getAbsolutePath());
		assertEquals("password", cfg.getCertificatePassword());
		assertTrue(cfg.isCleanOldResultFiles());
		assertEquals("https://freetsa.org/tsr", cfg.getTsaUrl().toString());
		assertTrue(cfg.getTsaUserName().isEmpty());
		assertTrue(cfg.getTsaUserPasswd().isEmpty());		
	}
	
	private String testData(boolean valid) {
		StringBuffer sb = new StringBuffer();
		sb.append("certifikat.cesta=/tmp/katacli/podpis.p12\n");
		sb.append("certifikat.heslo=password\n");
		if (valid) { //zamerne vynech jeden parametr aby vysledna data nebyla validni
			sb.append("vycistit=ano\n");
		}
		sb.append("tsa.url=https://freetsa.org/tsr\n");
		sb.append("tsa.uzivatel=\n");
		sb.append("tsa.heslo=\n");
		return sb.toString();
	}

	
	private void writeTestCfg(File cfgFile, boolean valid) throws IOException {
		FileWriter w = new FileWriter(cfgFile);
		w.write(testData(true));
		w.close();
			}
	
	private void writeTestCert(File certFile) throws IOException {
		OutputStream cert = new FileOutputStream(certFile);
		TestData.keystore().transferTo(cert);
		cert.close();
	}

}
