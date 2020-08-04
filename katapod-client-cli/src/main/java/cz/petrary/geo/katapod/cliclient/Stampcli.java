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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.Katapod;
import cz.petrary.geo.katapod.KatapodObjectFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

/**
 * Vytvoreni casoveho razitka pro soubor podepsaneho adresare.
 * Prikaz umoznuje prepsani hodnot z konfiguracniho souboru pro jedno konkretni pouziti 
 * Priklad: Katacli orazitkuj -url https://nejaka/TSA -tsausr uzivatel	-tsapasswd "heslo k TSA"
 * 
 * Program nacte hodnoty z default konfigurace a nasledne je nahradi hodnotami z prikazu.
 */
@Command(name = "orazitkuj", header = "Orazitkuj soubor Overeni_UOZI.txt.p7s")
public class Stampcli implements Callable<Integer> {

private static final Logger log = LoggerFactory.getLogger(Stampcli.class);
	
	@ParentCommand
	private Katacli parent;

	@Option(names = { "-url", "--tsa-url" }, 
			paramLabel = "URL", 
			description = "URL autority s casovymi razitky, pokud neni uveden pouzije se hodnota z konfiguracniho souboru")
	private String url = null;
	
	@Option(names = { "-tsausr", "--tsa-uzivatel" }, 
			paramLabel = "UZIVATEL", 
			description = "uzivatel s opravnenim ziskavat razitka z TSA, pokud neni uvedeno pouzije se hodnota z konfiguracniho souboru")
	private String user = null;

	@Option(names = { "-tsapasswd", "--tsa-heslo" }, 
			paramLabel = "HESLO", 
			description = "heslo k TSA, pokud neni uvedeno pouzije se hodnota z konfiguracniho souboru")
	private String passwd = null;


	/* Povinne parametry: 
	 *  adresar s vyslednym souborem urcenym k orazitkovani
	 */

	@Parameters(paramLabel = "ADRESAR", description = "cesta k adresari se soubory k podpisu")
    private File signDir;
	
	
	@Override
	public Integer call() throws Exception { 
		try {
		init();
		stampDir(signDir, parent.cfg);
		
		return 0;
		} catch (Exception ex) {
			log.error("Doslo k chybe.");
			log.debug("Detail: ", ex);
		}

		return 1;
	}


	/**
	 * prepsani default konfiguracnich hodnot temi z prikazu
	 * @throws FileNotFoundException
	 * @throws MalformedURLException
	 */
	public void init() throws FileNotFoundException, MalformedURLException {
		parent.initConfig();

		if (url != null) {
			parent.cfg.setTsaUrl(new URL(url));
		}
		if (user != null) {
			parent.cfg.setTsaUserName(Optional.of(user));
		}
		if (passwd != null) {
			parent.cfg.setTsaUserPasswd(Optional.of(passwd));
		}

		if (!signDir.exists() || !signDir.isDirectory()) {
			log.error("Cesta {} nevede k zadnemu existujicimu adresari!", signDir.getAbsolutePath());
			throw new RuntimeException("Chybny vstupni adresar");
		}

		File toStampFile = new File(signDir.getAbsolutePath() + "/Overeni_UOZI.txt.p7s");
		if (!toStampFile.exists()) {
			
			
		}
	}

	
	/**
	 * Vlastni orazitkovani souboru
	 * @param dir
	 * @param cfg
	 * @throws Exception
	 */
	public static void stampDir(File dir, ConfigValues cfg) throws Exception {
		Katapod service = KatapodObjectFactory.getKatapod();		
		try(InputStream stream = Files.newInputStream(Paths.get(dir.getAbsolutePath() + "/" + Katacli.GENERATED_FILE_NAME + ".p7s"))) {
			 byte[] result = service.stamp(stream.readAllBytes(), cfg.toStampConfig());
			 Files.write(Paths.get(dir.getAbsolutePath() + "/" + Katacli.GENERATED_FILE_NAME + ".p7s.tsr"), result);
	     } 
	}

	public static void main(String[] args) {
		int exitCode = new CommandLine(new Katacli()).execute(args);
		System.exit(exitCode);
	}

	
}
