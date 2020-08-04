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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.Katapod;
import cz.petrary.geo.katapod.KatapodObjectFactory;
import cz.petrary.geo.katapod.sign.SignResult;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;


/**
 * Podepsani adresare.
 * Prikaz umoznuje prepsani hodnot z konfiguracniho souboru pro jedno konkretni pouziti 
 * Priklad: Katacli podepis -cert /cesta/k/certifikatu.p12 -cpasswd "heslo k cert" -rm
 * 
 * Program nacte hodnoty z default konfigurace a nasledne je nahradi hodnotami z prikazu.
 */
@Command(name = "podepis", header = "Podepis soubory v adresari")
public class Signcli implements Callable<Integer> {

private static final Logger log = LoggerFactory.getLogger(Signcli.class);
	
	@ParentCommand
	private Katacli parent;

	@Option(names = { "-cert", "--certifacni-soubor" }, 
			paramLabel = "CERTIFIKAT", 
			description = "cesta k osobnimu certifikatu, pokud neni uvedena, pouzije se hodnota z konfiguracniho souboru")
	private File certFile = null;
	
	@Option(names = { "-cpasswd", "--cert-heslo" }, 
			paramLabel = "HESLO", 
			description = "heslo k osobnimu certifikatu, pokud neni uvedeno, pouzije se hodnota z konfiguracniho souboru")
	private String certPasswd = null;

	@Option(names = { "-rm", "-vycistit"}, 
			description = "pokud uvedeno, provede se smazani drive vygenerovanych souboru v adresari. Pokud neni uvedeno, pouzije se hodnota z konfiguracniho souboru")
	private Boolean cleanDir = null;

	/* Povinne parametry: 
	 *  adresar k podepsani
	 *  cislo overeni
	 */
	
	@Parameters(paramLabel = "ADRESAR", description = "cesta k adresari se soubory k podpisu")
    private File signDir;
	
	@Parameters(paramLabel = "CISLO", description = "cislo overeni")
    private String verNumber;

	
	@Override
	public Integer call() throws Exception { 
		try {
		init();
		signDir(signDir, verNumber, parent.cfg);
		return 0;

		} catch (Exception ex) {
			log.error("Doslo k chybe.");
			log.debug("Detail: ", ex);
			return 1;
		}
	}


	/**
	 * prepsani default konfiguracnich hodnot temi z prikazu
	 * @throws FileNotFoundException
	 */
	public void init() throws FileNotFoundException {
		parent.initConfig();

		if (certFile != null) {
			parent.cfg.setCertificateFile(certFile);
		}
		if (certPasswd != null) {
			parent.cfg.setCertificatePassword(certPasswd);
		}
		if (cleanDir != null) {
			parent.cfg.setCleanOldResultFiles(cleanDir);
		}

		if (!signDir.exists() || !signDir.isDirectory()) {
			log.error("Cesta {} nevede k zadnemu existujicimu adresari!", signDir.getAbsolutePath());
			throw new RuntimeException("Chybny vstupni adresar");
		}

	}

	/**
	 * Vlastni podpis celeho adresare a ulozeni vygenerovanych souboru.
	 * @param dir
	 * @param number
	 * @param cfg
	 * @throws Exception
	 */
	public static void signDir(File dir, String number, ConfigValues cfg) throws Exception {
		Katapod service = KatapodObjectFactory.getKatapod();
		SignResult result = service.signDir(dir.getAbsolutePath(),number, cfg.toSignConfig());
		
		 //write files to disk
		 Files.write(Paths.get(dir.getAbsolutePath() + "/" + Katacli.GENERATED_FILE_NAME), result.getTextFile().getBytes("UTF-8"));
		 Files.write(Paths.get(dir.getAbsolutePath() + "/" + Katacli.GENERATED_FILE_NAME + ".p7s"), result.getSignedData());
	}

	
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Katacli()).execute(args);
		System.exit(exitCode);
	}

	
}
