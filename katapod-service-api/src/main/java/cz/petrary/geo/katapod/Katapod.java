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

import java.io.InputStream;
import java.util.Map;

import cz.petrary.geo.katapod.sign.SignException;
import cz.petrary.geo.katapod.sign.SignResult;
import cz.petrary.geo.katapod.stamp.StampException;

/**
 * Interface pro nasledne klienty.
 *
 * Kazdy klient musi provest konfiguraci sluzby, kterou chce pouzivat a nasledne sluzbu zavolat.
 * Sluzby nezapisuji zadne soubory na disk. Pripadny zapis si musi zajistit klient.
 *
 * Prikad podepsani adresare:
 * <pre>
 *      Katapod service = new KatapodImpl();
 *      SignConfiguration sconf = new SignConfigurationImpl.Builder()
 *										.withCertificateFile("/path/to/certificate.p12")
 *										.andPassword("certificate_password")
 *										.build();
 *      SignResult result = service.signDir("/path/to/dir","verification_number", sconf);
 *
 *      //write files to disk
 *      Files.write("/path/to/dir/Overeni_UOZI.txt"), result.getTextFile().getBytes("UTF-8"));
 *      Files.write("/path/to/dir/Overeni_UOZI.txt.p7s"), result.getSignedData());
 *
 * </pre>
 *
 * Priklad orazitkovani adresare:
 * <pre>
 *      Katapod service = new KatapodImpl();
 *      //free TSA service
 * 		StampConfiguration sconf = new StampConfigurationImpl.Builder()
 *									.withTSA("https://freetsa.org/tsr")
 *									.build();
 *
 *	    //paid Czech Post service  - packages
 *		StampConfiguration sconf = new StampConfigurationImpl.Builder()
 *										.withTSA("https://www3.postsignum.cz/TSS/TSS_user/")
 *										.andUser("user_name")
 *										.andPassword("user_password")
 *										.build();
 *
 *      byte[] result = service.stamp(my_binary_data, sconf);
 *
 *      //write file to disk
 *      Files.write("/path/to/dir/Overeni_UOZI.txt.p7s.tsr"), result);
 * </pre>
 */
public interface Katapod {

	/**
	 * Vytvor text soubor s SHA512 hash od vsech souboru v adresari. Tento podepis osobnim certifikatem UOZI.
	 * Viz  § 18 odst. 5 vyhlášky č. 31/1995 Sb
	 * @param dirPath plna cesta k adresari se soubory "k podpisu"
	 * @param verifNumber cislo overeni
	 * @param config konfigurace podpisu (osobni certifikat OUZI, heslo)
	 * @return Vraci obsah nove vytvorenych dat dle § 18 odst. 5 vyhlášky č. 31/1995 Sb a jejich podpis
	 * @throws DirSignException pokud nelze soubor vytvorit nebo podepsat
	 */
	public SignResult signDir(String dirPath, String verifNumber, SignConfiguration config) throws SignException;


	/**
	 * Vytvor text soubor s SHA512 hash od vsech souboru v adresari. Tento podepis osobnim certifikatem UOZI.
	 * Viz  § 18 odst. 5 vyhlášky č. 31/1995 Sb
	 * @param files seznam souboru k podpisu, kde klicem je nazev souboru a obsah je jeho InputStream
	 * @param verifNumber cislo overeni
	 * @param config konfigurace podpisu (osobni certifikat OUZI, heslo)
	 * @return Vraci obsah nove vytvorenych dat dle § 18 odst. 5 vyhlášky č. 31/1995 Sb a jejich podpis
	 * @throws DirSignException pokud nelze soubor vytvorit nebo podepsat
	 */
	public SignResult signDir(Map<String, InputStream> files, String verifNumber, SignConfiguration config) throws SignException;


	/**
	 * Vytvor casove razitko k datum.
	 * @param data - jsou opatreny casovym razitkem v externim souboru
	 * @param config konfigurace razitek (URL TSA sluzby, prihlasovaci udaje uzivatele)
	 * @return vysledek vraceny z TSA
	 * @throws DirSignException pokud nelze casove razitko vytvorit
	 */
	public byte[] stamp(byte[] data, StampConfiguration config) throws StampException;


	/**
	 * Vytvor casove razitko k datum.
	 * @param data - jsou opatreny casovym razitkem v externim souboru
	 * @param config konfigurace razitek (URL TSA sluzby, prihlasovaci udaje uzivatele)
	 * @return vysledek vraceny z TSA
	 * @throws DirSignException pokud nelze casove razitko vytvorit
	 */
	public InputStream stamp(InputStream data, StampConfiguration config) throws StampException;;

}