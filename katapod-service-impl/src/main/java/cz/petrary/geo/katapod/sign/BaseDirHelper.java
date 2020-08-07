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
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pomocna trida pro 
 * <ul>
 * <li>overeni, zda je odkazovana cesta adresarem</li>
 * <li>smazani jiz existujicich souboru "Overeni_OUZI"
 * </ul>
 */
public class BaseDirHelper {

	private static final Logger log = LoggerFactory.getLogger(TextFile.class);
	
	
	/**
	 * Cesta je platna, pokud se jedna o adresar a ma alespon jeden soubor
	 * @param path proverovana cesta
	 * @return true - cesta je platna, false - neplatna cesta nebo prazdny adresar
	 */
	public static boolean dirCheck(Path path) {
		File dir = path.toFile();
		if (!dir.exists() || !dir.isDirectory()) {
			log.error("Invalid directory {}", path);
			return false;
		}
		
		File[] dirList = dir.listFiles();
		
		if (dirList.length == 0) {
			log.warn("This directory is empty - {}", path);
			return false;
		}
		
		return true;
	}
	

	/**
	 * Procisti adresar. Vymaze vsechny generovane soubory. To jsou ty, ktere zacinaji na ${text.file.name} - coz je "Overeni_UOZI.txt"
	 * @param path cesta k adresari
	 */
	public static void prune(Path path) {
		log.debug("Cleaning base dir");
		ResourceBundle rb = ResourceBundle.getBundle("application");
		final String filter = rb.getString("text.file.name");
		File dir = path.toFile();
		File[] textFiles = dir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.startsWith(filter);
	        }
	    });
		
		for (File file : textFiles) {
			file.delete();
			log.info("File {} was removed", file.getName());
		}
	}
	
}
