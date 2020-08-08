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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.sign.Sign;
import cz.petrary.geo.katapod.stamp.Stamp;
import cz.petrary.geo.katapod.textfile.BaseDirHelper;
import cz.petrary.geo.katapod.textfile.HashHelper;
import cz.petrary.geo.katapod.textfile.TextFile;

/**
 * Interface pro nasledne klienty.
 *
 * Kazdy klient musi provest konfiguraci sluzby, kterou chce pouzivat a nasledne sluzbu zavolat.
 * Sluzby nezapisuji zadne soubory na disk. Pripadny zapis si musi zajistit klient.
 * 
 * Priklad a detailni popis @see Katapod
 */
public class KatapodImpl implements Katapod {
	
	private static final Logger log = LoggerFactory.getLogger(KatapodImpl.class);
	
	ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
	
	private Sign sign = null;
	
	private Configuration config = null;


	@Override
	public void setConfiguration(Configuration config) throws KatapodException {
		if (this.checkConfiguration(config)) {
			this.config = config;
		} else {
			throw new KatapodException("Konfiguracni soubory nejsou validni!");
		}
		
	}



	@Override
	public Path createTextFile(Path dirPath, String verifNumber) throws KatapodException {
		if (!BaseDirHelper.dirCheck(dirPath)) {
			throw new KatapodException("Neexistujici adresar " + dirPath);
		}
		if (config == null || config.getAdditionalConfiguration() == null || config.getAdditionalConfiguration().get("keepResultFiles") == null) {
			BaseDirHelper.prune(dirPath);  //promaz drive vygenerovane soubory. Ponech pouze pokud v additional config existuje klic "keepResultFiles"
		}
		List<String> hashes;
		try {
			hashes = HashHelper.hashForAllFiles(dirPath);
		} catch (IOException e) {
			throw new KatapodException("Nelze vytvorit HASH pro jednotlive soubory!", e);
		}
		sign = new Sign(config.getCertificate(), config.getCertificatePassword().toCharArray());

		//text file
		String text = TextFile.create(sign.getAlias(),verifNumber, hashes);
		Path textFile = dirPath.resolve(resourceBundle.getString("text.file.name"));
		try {
			Files.write(textFile, text.getBytes("UTF-8"));
			log.info("Textovy soubor byl vytvoren");
		} catch (IOException e) {
			throw new KatapodException("Nelze zapsat vysledny textovy soubor!", e);
		}

		return textFile;
	}

	
	
	@Override
	public Path signDir(Path textFilePath) throws KatapodException {
		try {
			//signature file
			byte[] signedData = sign.sign(textFilePath.toFile());
			Path signatureFile = Paths.get(textFilePath.toString()+".p7s");
			Files.write(signatureFile, signedData);

			log.info("Soubor s podpisem byl vytvoren");
			return signatureFile;
		} catch (IOException ioe) {
			throw new KatapodException("Nelze vytvorit soubor s podpisem", ioe);
		}
		
	}

	

	@Override
	public Path stamp(Path signedFilePath) throws KatapodException {
		Stamp stamp = new Stamp();
		byte[] result = stamp.makeTimestamp(signedFilePath.toFile(), config.getTsaUrl().toString(), 
				                            config.getTsaUserName(), config.getTsaUserPasswd());
		Path stampFile = Paths.get(signedFilePath.toString()+".tsr");
		try {
			Files.write(stampFile, result);
			return stampFile;
		} catch (IOException e) {
			throw new KatapodException("Nelze vytvorit soubor s casovym razitkem!", e);
		}
	}




	private boolean checkConfiguration(Configuration config) {
		/** @TODO */
		return true;
	}






}
