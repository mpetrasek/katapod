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

import java.nio.file.Path;


/**
 * Interface pro nasledne klienty.
 *
 * Kazdy klient musi provest konfiguraci sluzby, kterou chce pouzivat a nasledne sluzbu zavolat.
 * Sluzby nezapisuji zadne soubory na disk. Pripadny zapis si musi zajistit klient.
 *
 * Prikad vytvoreni textoveho souboru:
 * <pre>
 *      Katapod service = new KatapodImpl();
 *      service.setConfiguration(conf);
 *      Path textFile = service.createTextFile(Paths.get("/path/to/dir"),"verification_number");
 *
 * </pre>
 
 * Prikad podepsani adresare:
 * <pre>
 *      Path signedFile = service.signDir(textFile);
  * </pre>
 *
 * Priklad orazitkovani adresare:
 * <pre>
 *      Path stampFile = service.stamp(signedFile);
 * </pre>
 */
public interface Katapod {

	/**
	 * Nastav konfiguracni hodnoty pro podpis a razitko.
	 * @param config konfigurace podpisu a razitka (osobni certifikat OUZI, heslo, TSA URL...)
	 * @throws KatapodException
	 */
	public void setConfiguration(Configuration config) throws KatapodException;
	
	
	/**
	 * Vytvor text soubor s SHA512 hash od vsech souboru v adresari. Tento podepis osobnim certifikatem UOZI.
	 * Viz  § 18 odst. 5 vyhlášky č. 31/1995 Sb
	 * @param dirPath plna cesta k adresari se soubory "k podpisu"
	 * @param verifNumber cislo overeni
	 * @return Cesta k nove vytvorenemu textovemu souboru
	 * @throws KatapodException pokud nelze soubor vytvorit
	 */
	public Path createTextFile(Path dirPath, String verifNumber) throws KatapodException;

	/**
	 * Vytvor text soubor s SHA512 hash od vsech souboru v adresari. Tento podepis osobnim certifikatem UOZI.
	 * Viz  § 18 odst. 5 vyhlášky č. 31/1995 Sb
	 * @param dirPath plna cesta k textovemu souboru, ktery se ma podepsat
	 * @return Cesta k nove vytvorenemu souboru s podpisem
	 * @throws KatapodException pokud nelze soubor vytvorit nebo podepsat
	 */
	public Path signDir(Path textFilePath) throws KatapodException;


	/**
	 * Vytvor casove razitko k datum.
	 * @param data - jsou opatreny casovym razitkem v externim souboru
	 * @param dirPath - adresar kam se ulozi vysledny soubor. Pokud je prazdny, zadny soubor se neulozi
	 * @return Cesta k nove vytvorenemu souboru s casovym razitkem
	 * @throws KatapodException pokud nelze casove razitko vytvorit
	 */
	public Path stamp(Path signedFilePath) throws KatapodException;


}
