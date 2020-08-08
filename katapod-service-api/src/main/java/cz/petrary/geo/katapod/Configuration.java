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
import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * Konfiguracni hodnoty nutne pro vytvoreni podpisu a casoveho razitka.
 * U podpisu se jedna o:
 *  <ul>
 *    <li>osobni certifikat UOZI - povinna hodnota, musi byt vzdy definovan</li>
 *    <li>heslo k osobnimu certifikatu - povinna hodnota, musi byt vzdy definovana</li>
 *    <li>moznost vymazat jiz existujici vystupni soubory z adresare - volitelne, nastaveno na <pre>true</pre> = mazat</li>
 * </ul>
 * 
 * U razitka se jedna o:
 *  <ul>
 *    <li>URL na poskytovatele casovych razitek - povinna informace</li>
 *    <li>jmeno uzivatele pro sluzbu casovych razitek - volitelna</li>
 *    <li>heslo uzivatele do sluzby casovych razitek - volitelna</li>
 * </ul>

 */
public interface Configuration {


	/**
	 * InputStream predstavujici osobni certifikat UOZI - ocekavan ve formatu PKCS12 = tak jak ho dostane z ceske posty.
	 * @return InputStream predstavujici osobni certifikat UOZI
	 */
	public InputStream  getCertificate();

	/**
	 * Heslo k osobnimu certifikatu - povinna hodnota, musi byt vzdy definovana
	 * @return heslo k certifikatu
	 */
	public String getCertificatePassword();
	
	
	/**
	 * URL na poskytovatele casovych razitek - povinna informace
	 * @return URL na poskytovatele casovych razitek - povinna informace
	 */
	public URL  getTsaUrl();

	/**
	 * jmeno uzivatele pro sluzbu casovych razitek - volitelna
	 * @return jmeno uzivatele pro sluzbu casovych razitek
	 */
	public Optional<String> getTsaUserName();

	/**
	 * heslo uzivatele pro sluzbu casovych razitek - volitelna
	 * @return heslo uzivatele pro sluzbu casovych razitek
	 */
	public Optional<String> getTsaUserPasswd();

	
	
	/**
	 * Dodatecna (volitelna) konfigurace. Muze se jednat napr. o moznost promazat adresar pred podpisem apod.  
	 * @return mapa hodnot pro dodatecnou konfiguraci konkretni implementace.
	 */
	public Map<String, String> getAdditionalConfiguration();

}
