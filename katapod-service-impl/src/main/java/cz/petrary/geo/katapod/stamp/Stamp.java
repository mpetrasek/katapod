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
package cz.petrary.geo.katapod.stamp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.KatapodException;

/**
 * Vytvor casove razitko 
 *
 */
public class Stamp {

	private static final Logger log = LoggerFactory.getLogger(Stamp.class);


	/**
	 * K zadanym datum vytvor casove razitko u pozadovane TSA (time stamping authority)
	 * @param dataToStamp data urcena k opatreni casovym razitkem
	 * @param tsaUrl URL TSA
	 * @param userName login uzivatele u TSA (pokud je prazdne, pak je sluzba volana bez overeni uzivatele)
	 * @param password heslo uzivatele u TSA (pokud je prazdne, pak je sluzba volana bez overeni uzivatele)
	 * @return data vracena z TSA
	 * @throws StampException nepovedlo se ziskat razitko 
	 * @throws KatapodException 
	 */
	public byte[] makeTimestamp(File fileToStamp, String tsaUrl, 
			                    Optional<String> userName, Optional<String> password)
			throws KatapodException {
		//create request
		byte[] dataToStamp;
		try (FileInputStream fis = new FileInputStream(fileToStamp)) {
			dataToStamp = fis.readAllBytes();
		} catch (IOException e) {
			throw new KatapodException("Nelze cist data ze souboru " + fileToStamp.getAbsolutePath(), e);
		}
		TimeStampRequest req = timestampRequest(dataToStamp);
		try {
			//send request to TSA
			log.debug("Pripojuji se k TSA {}", tsaUrl);
			byte[] request = req.getEncoded();
			HttpsURLConnection conn = HttpsHelper.getConnection(tsaUrl, userName, password);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/timestamp-query");
			conn.setRequestProperty("Content-length", String.valueOf(request.length));
			OutputStream requestOutStream = conn.getOutputStream();
			requestOutStream.write(request);
			requestOutStream.flush();
			
			log.info("Zadost o casove razitko byla zaslana TSA {}. Response code = {}", tsaUrl, conn.getResponseCode());

			//get response code from TSA
			if (conn.getResponseCode() != 200) {
				throw new KatapodException(
					"HTTP status: " + conn.getResponseCode() + ", message: " + conn.getResponseMessage());
			}
			
			//response is OK, start decoding response data
			InputStream responseInStream = conn.getInputStream();
			TimeStampResp resp = TimeStampResp.getInstance((new ASN1InputStream(responseInStream)).readObject());
			responseInStream.close();
			requestOutStream.close();
			TimeStampResponse response = new TimeStampResponse(resp);

			byte[] result = response.getEncoded();
			log.info("Odpoved z TSA prectena. Velikost casoveho razitka = {} [byte]", result.length);
			return result;
			
		} catch (Exception ex) {
			throw new KatapodException("Nelze ziskat casove razitko z TSA", ex);
		}
	}
	
	
	/**
	 * Vytvor pozadavek, ktery bude odeslan do TSA
	 * @param dataToStamp co se ma oznackovat
	 * @return pozadavek k odeslani na TSA
	 * @throws StampException
	 */
	TimeStampRequest timestampRequest(byte[] dataToStamp) throws KatapodException {
		log.debug("Vytvarim zadost o casove razitko");
		ASN1ObjectIdentifier hashOID = TSPAlgorithms.SHA512;
		byte[] digestData;
		try {
			digestData = MessageDigest.getInstance(hashOID.getId(), new BouncyCastleProvider())
					.digest(dataToStamp);
		} catch (NoSuchAlgorithmException e) {
			throw new KatapodException("Nelze vytvorit digest data!", e);
		}
		TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
		reqgen.setCertReq(true);
		TimeStampRequest result = reqgen.generate(hashOID, digestData, new BigInteger(16, new Random())); 
		log.info("Zadost o casove razitko byla vytvorena");
		return result;
	}

	

}
