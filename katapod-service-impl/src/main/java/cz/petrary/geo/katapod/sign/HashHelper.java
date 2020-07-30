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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pomocna trida pro praci s SHA-512 hash.
 * Krome HASH umi vytvaret retezce zapisovane do vysledneho textoveho souboru "Overeni_OUZI"
 */
public class HashHelper {
	
	private static final Logger log = LoggerFactory.getLogger(HashHelper.class);
	
	/**
	 * Oddelovac mezi jmenem souboru a jeho HASH - definovano CUZK
	 * Viz https://www.cuzk.cz/Zememerictvi/Zememericke-cinnosti/Overovani-vysledku-zememerickych-cinnosti-v-elektr/Format_textoveho_souboru.aspx
	 */
	private static final String DELIMITER = ";";
	
	
	/**
	 * Vypocti HASH pro vsechny soubory v adresari
	 * @param dirPath vstupni adresar
	 * @return nazvy souboru a jejich HASH - spodni cast souboru Overeni_OUZI - to co je za oddelovacem '----'
	 * @throws IOException pokud se nepovede spocitat hash
	 */
	public static List<String> hashForAllFiles(String dirPath) throws IOException {
		File[] dirList = new File(dirPath).listFiles();
		Map<String, InputStream> streams = new HashMap<>();
		for (File file : dirList) {
			streams.put(file.getName(),new FileInputStream(file));
		}
		
		return hashForStreams(streams);
	}
	
	/**
	 * Vypocti HASH pro vsechny soubory v adresari
	 * @param streams vstupni data. klicem je nazev souboru, hodnotou jsou jeho data jako InputStream
	 * @return nazvy souboru a jejich HASH - spodni cast souboru Overeni_OUZI - to co je za oddelovacem '----'
	 * @throws IOException pokud se nepovede spocitat hash
	 */
	public static List<String> hashForStreams(Map<String, InputStream> streams) throws IOException {
		List<String> result = new ArrayList<>();
		MessageDigest md = getMessageDigest();
		Set<String> keys = streams.keySet();
		for (String key : keys) {
			result.add(countStreamHash(streams.get(key), key, md));
		}
		
		return result;
	}
	

	
	/**
	 * Vypocti HASH pro jeden soubor/InputStream
	 * @param stream vstupni data pro nez se hash pocita
	 * @param fileName jmeno souboru
	 * @param md instance SHA-512 
	 * @return nazev souboru a jeho hash
	 * @throws IOException  pokud se nepovede spocitat hash
	 */
	static String countStreamHash(InputStream stream, String fileName, MessageDigest md) throws IOException {
		byte[] messageBytes = stream.readAllBytes();
		byte[] messageHash = md.digest(messageBytes);
		String result =  fileName + DELIMITER + hex(messageHash);
		log.info("count hash = {}", result);
		
		return result;
	}


	
    /**
     * Ziskej instanci SHA-512
     * @return SHA-512 MessageDigest
     */
	static MessageDigest getMessageDigest() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			return md;
		} catch (NoSuchAlgorithmException ex) {
			log.error("No SHA-512 algorithm!");
			throw new RuntimeException(ex);
		}
		
	}
	
	
	/**
	 * Preved vysledny HASH do textove podoby. Pouzita primitivni, zato vsak ucinna a citelna metoda prevodu.
	 * @param bytes HASH v binarni podobe
	 * @return HASH v textove podobe
	 */
    private static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
           result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }


}
