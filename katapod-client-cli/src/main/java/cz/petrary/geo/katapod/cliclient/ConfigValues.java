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

package cz.petrary.geo.katapod.cliclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PropertyResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.Configuration;

/**
 * Konfiguracni hodnoty klienta.
 * Hodnoty lze nacist s properties souboru a opet do nej ulozit.
 * Hodnoty slouzi pro vytvoreni objektu SignConfiguration a StampConfiguration
 *
 */
public class ConfigValues implements Configuration {
	
	private static final Logger log = LoggerFactory.getLogger(Katacli.class);
	
	/**
	 * Default property soubor. Pokud neni zadan uzivatelem definovany 
	 * property soubor, pak se pokusi hodnoty nacist z tohoto default souboru
	 */
	private static final String DEFAULT_FILE_NAME = "katapod.properties";
	
	
	/**
	 * cesta na soubor s osobnim certifikatem
	 */
	private File certificateFile = null;
	
	/**
	 * Heslo k osobnimu certifikatu
	 */
	private String certificatePassword = null;
	
	/**
	 * Maji se pred vypoctem odstranit existujici generovene soubory?
	 */
	private boolean cleanOldResultFiles = true;
	
	/**
	 * URL na TSA
	 */
	private URL tsaUrl;
	
	/**
	 * Jmeno uzivatele, ktery se bude do TSA hlasit.
	 * Pokud je prazdne, pak se pouzije TSA bez autentizace
	 */
	private Optional<String> tsaUserName;
	
	/**
	 * Heslo uzivatele, ktery se bude do TSA hlasit.
	 * Pokud je prazdne, pak se pouzije TSA bez autentizace
	 */
	private Optional<String> tsaUserPasswd;

	
	/**
	 * Konfiguracni soubor - bud odkazuje na uzivatelem predany soubor nebo
	 * na default soubor, ktery je distribuovan s kazdym klientem
	 */
	private File configFile;
	
	
	
	/**
	 * Inicializuj atributy hodnotami z konfiguracniho souboru.
	 * Pokud je zadana hodnota cfgFile, pak se pouziji hodnoty z tohoto souboru.
	 * Pokud je hodnota null, pak se pouzije defaultni konfiguracni soubor.
	 * @param cfgFile konfiguracni soubor
	 * @return true - hodnoty nacteny a jsou v poradky
	 */
	public ConfigValues(Optional<File> cfgFile) {
		if (cfgFile.isPresent()) { //user config file was defined
			configFile = cfgFile.get();
			} else { //read default config file
			configFile = new File(DEFAULT_FILE_NAME);
		}
		if (!readFromPropertyFile()) {
			throw new RuntimeException("Chyba pri praci s konfiguracnim souborem");
		}
	}
	
	
	/**
	 * Zapis konfiguracni hodnoty do souboru
	 */
	public void store() {
		Writer dest = null;
		try {
			dest = new FileWriter(configFile);
			this.writeProperties(dest);
		} catch (IOException ex) {
			log.error("Nelze zaspat konfiguracni hodnoty do souboru!");
			log.debug("Detail:", ex);
		} finally {
			try {
				dest.close();
			} catch (Exception e) {
				log.warn("Nelze uzavrit konfiguracni soubor pro zapis");
			}
		}
	}
		
	
	//-------------------------------- PACKAGE ----------------------------------------------------
		
	/**
	 * Nacti konfiguracni hodnoty z properties souboru
	 * @return true - hodnoty nacteny a jsou v poradky
	 */
	boolean readFromPropertyFile() {
		try {
			return readProperties(new FileReader(configFile));
		} catch (FileNotFoundException ex) {
			log.error("Zadany konfiguracni soubor neexistuje");
			return false;
		}
	}
	
	
	
	/**
	 * Vlastni operace cteni a kontroly konfiguracnich hodnot
	 * @param rbStream
	 * @return true - hodnoty nacteny a jsou v poradky
	 */
	boolean readProperties(Reader  rbStream) {
		try { //read the content
			PropertyResourceBundle rb = new PropertyResourceBundle(rbStream);
			String certificatePath =  rb.getString("certifikat.cesta");
			setCertificateFile(new File(certificatePath)); 
			this.certificatePassword =  rb.getString("certifikat.heslo");
			this.cleanOldResultFiles = "ano".equalsIgnoreCase( rb.getString("vycistit"));
			this.tsaUrl = new URL( rb.getString("tsa.url"));
			this.tsaUserName = createOptionalFromString(rb.getString("tsa.uzivatel"));
			this.tsaUserPasswd = createOptionalFromString(rb.getString("tsa.heslo"));
			
			return true;
		} catch (Exception ex) { //problem with file or invalid file format
			log.error("Chyba v konfiguracnim souboru!");
			return false;
		} finally {
			try {
				rbStream.close();
			} catch (Exception e) {
				log.warn("Nelze uzavrit konfiguracni soubor pro cteni");
			}
		}
		
	}
	
	
	/**
	 * Zapis hodnoty konfiguracnich parametru do souboru.
	 * @param rbStream
	 * @return
	 */
	boolean writeProperties(Writer  rbStream) {
		StringBuffer sb = new StringBuffer();
		sb.append("# Plna cesta k osobnimu certifikatu\n");
		sb.append("certifikat.cesta=").append(this.certificateFile.getAbsolutePath()).append("\n");
		
		sb.append("\n# Heslo k osobnimu certifikatu = to, co bylo zadano pri exportu nebo tvorbe certifikatu\n");
		sb.append("certifikat.heslo=").append(this.certificatePassword).append("\n");
		
		sb.append("\n# Ma se vzdy pred vytvorenim souboru Overeni_OUZI.txt smazat jeho stara verze vcetne .p7s a casoveho razitka?\n");
		sb.append("# Uvedte ano nebo ne. Priklad: vycistit=ano\n");
		sb.append("vycistit=").append((this.cleanOldResultFiles?"ano":"ne")).append("\n");

		sb.append("\n# Adresa pro casova razitka. Pokud mate predplaceny balicky casovych razitek u Ceske Posty, uvedte nasledujici\n");
		sb.append("# tsa.url=https://www3.postsignum.cz/TSS/TSS_user/\n");
		sb.append("tsa.url=").append(this.tsaUrl).append("\n");

		sb.append("\n# Uzivatelske jmeno pro casova razitka.\n");
		sb.append("# Pokud neni uvedena zadna hodnota, nebude uzivatelske jmeno a heslo pouzito.\n");
		sb.append("tsa.uzivatel=").append(createStringFromOptional(this.tsaUserName)).append("\n");

		sb.append("\n# Heslo pro casova razitka.\n");
		sb.append("tsa.heslo=").append(createStringFromOptional(this.tsaUserPasswd)).append("\n");

		try {
			rbStream.write(sb.toString());
			rbStream.close();
			return true;
		} catch (IOException e) {
			log.error("Nelze zapsat nove hodnoty konfiguracnich parametru! {}",e.getMessage());
			log.debug("Detail error:", e);
			return false;
		}
	}
	
	
	private Optional<String> createOptionalFromString(String str) {
		if (str != null && str.isBlank()) {
			return Optional.empty();
		}
		return Optional.ofNullable(str);
	}

	private String createStringFromOptional(Optional<String> val) {
		if (val.isEmpty()) {
			return "";
		}
		return val.get();
	}
	//--------------------------------------------------------------------------------------------------
	

	public File getCertificateFile() {
		return certificateFile;
	}



	public void setCertificateFile(File certificateFile) throws FileNotFoundException {
		if (!certificateFile.exists() || !certificateFile.isFile()) {
			log.error("Soubor s certifikatem neexistuje! {}", certificateFile);
			throw new FileNotFoundException("Soubor s certifikatem neexistuje!");
		}
		this.certificateFile = certificateFile;
	}



	public String getCertificatePassword() {
		return certificatePassword;
	}



	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}



	public boolean isCleanOldResultFiles() {
		return cleanOldResultFiles;
	}



	public void setCleanOldResultFiles(boolean cleanOldResultFiles) {
		this.cleanOldResultFiles = cleanOldResultFiles;
	}



	public URL getTsaUrl() {
		return tsaUrl;
	}



	public void setTsaUrl(URL tsaUrl) {
		this.tsaUrl = tsaUrl;
	}



	public Optional<String> getTsaUserName() {
		return tsaUserName;
	}



	public void setTsaUserName(Optional<String> tsaUserName) {
		this.tsaUserName = tsaUserName;
	}



	public Optional<String> getTsaUserPasswd() {
		return tsaUserPasswd;
	}



	public void setTsaUserPasswd(Optional<String> tsaUserPasswd) {
		this.tsaUserPasswd = tsaUserPasswd;
	}


	@Override
	public InputStream getCertificate() {
		try {
			return new FileInputStream(this.certificateFile);
		} catch (FileNotFoundException e) {
			log.error("Soubor s certifikatem neexistuje! {}", certificateFile);
			throw new RuntimeException("Soubor s certifikatem neexistuje!");
		}
	}


	@Override
	public Map<String, String> getAdditionalConfiguration() {
		if (!isCleanOldResultFiles()) {
			Map<String, String> result = new HashMap<>();
			result.put("keepResultFiles", "true");
			return result;
		} else {
			return null;
		}
	}

	
	
	//------------------------------------------------------------------------------------------
	
	

}
