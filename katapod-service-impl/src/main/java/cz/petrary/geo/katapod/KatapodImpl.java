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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.sign.BaseDirHelper;
import cz.petrary.geo.katapod.sign.HashHelper;
import cz.petrary.geo.katapod.sign.Sign;
import cz.petrary.geo.katapod.sign.SignException;
import cz.petrary.geo.katapod.sign.SignResult;
import cz.petrary.geo.katapod.sign.TextFile;
import cz.petrary.geo.katapod.stamp.Stamp;
import cz.petrary.geo.katapod.stamp.StampException;

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

	
	
	@Override
	public SignResult signDir(String dirPath, String verifNumber, SignConfiguration config) throws SignException {
		if (!BaseDirHelper.dirCheck(dirPath)) {
			log.error("Invalid directory!");
			throw new SignException("Invalid directory");
		}
		if (config.isCleanOldResultFiles()) {
			BaseDirHelper.prune(dirPath);
		}

		try {
			List<String> hashes = HashHelper.hashForAllFiles(dirPath);
			return sign(hashes, verifNumber, config);
		} catch (IOException ioe) {
			log.error("Unable to compute hash for files in directory {}", dirPath,ioe);
			throw new SignException("Unable to compute hash for files", ioe);
		}
		
	}

	
	
	@Override
	public SignResult signDir(Map<String, InputStream> streams, String verifNumber, SignConfiguration config)
			throws SignException {
		try {
			List<String> hashes = HashHelper.hashForStreams(streams);
			return sign(hashes, verifNumber, config);
		} catch (IOException ioe) {
			log.error("Unable to compute hash for files in the list", ioe);
			throw new SignException("Unable to compute hash for files", ioe);
		}
	}

	
	
	@Override
	public byte[] stamp(byte[] data, StampConfiguration config) throws StampException {
		if (config == null) {
			log.error("Invalid configuration!");
			throw new StampException("Invalid configuration!");
		}
		
		Stamp stamp = new Stamp();
		byte[] result = stamp.makeTimestamp(data, config.getUrl().toString(), 
				                            config.getUserName(), config.getUserPasswd());
		return result;
	}

	
	
	@Override
	public InputStream stamp(InputStream data, StampConfiguration config) throws StampException {
		try {
			return new ByteArrayInputStream(stamp(data.readAllBytes(), config));
		} catch (IOException e) {
			log.error("Unable to read input data", e);
			throw new StampException("Unable to read input data", e);
		}
	}
	
	
	/**
	 * Vytvor obsah textoveho souboru a ten podepis
	 * @param hashes
	 * @param verifNumber
	 * @param config
	 * @return
	 * @throws SignException
	 */
	private SignResult sign(List<String> hashes, String verifNumber, SignConfiguration config) throws SignException {
		if (config == null) {
			log.error("Invalid configuration!");
			throw new SignException("Invalid configuration!");
		}

		Sign sign = new Sign(config.getCertificate(), config.getCertificatePassword().toCharArray());
		String textFile = TextFile.create(sign.getAlias(),verifNumber, hashes);
			
		byte[] signedData = sign.sign(textFile);
		SignResult result = new SignResult(textFile, signedData);
			
		log.info("Final text file was created and signed");
		return result;	
	}

}
