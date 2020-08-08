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
package cz.petrary.geo.katapod.textfile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sestavi obsah textoveho souboru Overeni_OUZI presne podle
 * https://www.cuzk.cz/Zememerictvi/Zememericke-cinnosti/Overovani-vysledku-zememerickych-cinnosti-v-elektr/Format_textoveho_souboru.aspx
 *
 */
public class TextFile {
	
	private static final Logger log = LoggerFactory.getLogger(TextFile.class);
	
	public static final String CRLF = "\r\n";

	/**
	 * Vytvor obsah souboru 
	 * @param signerName - jmeno OUZI
	 * @param number - cislo overeni
	 * @param hashes - seznam vsech souboru a jejich hash
	 * @return obsah souboru Overeni_OUZI podle vyhlasky
	 */
	public static String create(String signerName, String number, List<String> hashes) {
		StringBuffer result = new StringBuffer();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
        result.append(resourceBundle.getString("text.header")).append(CRLF);
        result.append(number).append(CRLF);
        result.append(getDate()).append(CRLF);
        result.append(signerName).append(CRLF);
        result.append(resourceBundle.getString("text.divider")).append(CRLF);
        for (String filehash : hashes) {
        	result.append(filehash).append(CRLF);
		}
        
        log.debug("Result = \n{}", result.toString());
        return result.toString();
	}
	
	/**
	 * Pouzij vzdy aktualni datum. Datum razitka take bude aktualni a to co je
	 * v Overeni_OUZI se musi shodovat s datem TSA razitka.
	 * @return aktualni datum
	 */
	private static String getDate() {
		LocalDate date = LocalDate.now();
		return date.format(DateTimeFormatter.ofPattern("d.M.yyyy"));
	}


}
