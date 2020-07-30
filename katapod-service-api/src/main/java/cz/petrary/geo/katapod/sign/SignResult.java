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

/**
 * Vysledek cele operace podpisu. 
 * Obsahuje jednak vygenerovany textovy soubor "Overeni_OUZI.txt" - to co se ma podepsat a
 * podepsana data.
 * 
 * Vlastni textovy soubor ani podepsana data se nikam neukladaji. To si dela klient sam podle svych potreb.
 */
public class SignResult {
	
	private String textFile;
	private byte[] signedData;
	
	
	/**
	 * Konstruktor s naplnenim hodnot
	 * @param text
	 * @param data
	 */
	public SignResult(String text, byte[] data) {
		textFile = text;
		signedData = data;
	}
	
	/**
	 * Obsah sestaveneho souboru "Overeni_OUZI.txt".
	 * @return textova data
	 */
			
	public String getTextFile() {
		return textFile;
	}
	
	/**
	 * Podepsana data
	 * @return Podepsany textovy soubor
	 */
	public byte[] getSignedData() {
		return signedData;
	}

	
//	/**
//	 * Nastavi text. soubor 
//	 * @param textFile sestaveny text urceny k podpisu
//	 */
//	void setTextFile(String textFile) {
//		this.textFile = textFile;
//	}
//	
//	/**
//	 * Nastav podepsana data
//	 * @param signedData podepsana data
//	 */
//	void setSignedData(byte[] signedData) {
//		this.signedData = signedData;
//	}
//	
	

}
