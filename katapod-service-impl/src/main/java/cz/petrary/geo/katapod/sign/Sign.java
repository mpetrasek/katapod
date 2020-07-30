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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Podepise obsah textoveho souboru osobnim podpisem 
 *
 */
public class Sign {

	private static final Logger log = LoggerFactory.getLogger(Sign.class);

	/**
	 * Keystore s osobnim certifikatem
	 */
	private KeyStore keystore;
	
	/**
	 * Privatni klic od certifikatu, ktery je ulozen v keyStore
	 */
	private PrivateKey privateKey;
	
	/**
	 * Prvni nalezeny alias v key store.
	 * Pracuje se pouze s prvnim nalezenym, vice bych se jich v keystore nemelo nalezat!
	 */
	private String alias;

	
	/**
	 * Konstruktor - over platnost cest a hesla ke key store
	 * @param keystorePath plna cesta ke keystore s osobnim certifikatem
	 * @param password heslo ke keystore a privatnimu klici - ocekava se, ze je shodne!
	 * @throws SignException nepovedlo se pracovat s osobnim certifikatem
	 */
	public Sign(String keystorePath, char[] password) throws SignException {
		try {
			InputStream input = Files.newInputStream(Paths.get(keystorePath), StandardOpenOption.READ);
			this.configure(input, password);
		} catch (IOException e) {
			log.error("Unable to work with keystore",e);
			throw new SignException("Unable to work with keystore",e);
		}
	}
	
	
	/**
	 * Konstruktor - over platnost cest a hesla ke key store
	 * @param keystoreData obsah keystore s osobnim certifikatem
	 * @param password heslo ke keystore a privatnimu klici - ocekava se, ze je shodne!
	 * @throws SignException nepovedlo se pracovat s osobnim certifikatem
	 */
	public Sign(InputStream keystoreData, char[] password) throws SignException {
		this.configure(keystoreData, password);
	}

	
	/**
	 * Vrat hodnotu prvniho (a ocekavame ze jedineho) aliasu v keystore
	 * @return hodnota prvniho aliasu v keystore
	 */
	public String getAlias() {
		return alias.toUpperCase();
	}

	/**
	 * Podepis textovy obsah. Jedna se o obsah souboru Overeni_OUZI
	 * @param text obsah souboru Overeni_OUZI (ten ale nemusi byt nikde ulozen)
	 * @return podepsany text
	 * @throws SignException nepovedlo se podepsat
	 */
	@SuppressWarnings("rawtypes")
	public byte[] sign(String text) throws SignException {
		try {
			addSecurityProvider();
			byte[] data = text.getBytes();
			ArrayList<X509CertificateHolder> certList = new ArrayList<>();
			CMSProcessableByteArray msg = new CMSProcessableByteArray(data);
			X509CertificateHolder signerCertificate = new X509CertificateHolder(
					keystore.getCertificate(alias).getEncoded());
			certList.add(signerCertificate);
			JcaCertStore certs = new JcaCertStore(certList);
			CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
			ContentSigner sha512Signer = (new JcaContentSignerBuilder("SHA512withRSA")).setProvider("BC")
					.build(privateKey);
			gen.addSignerInfoGenerator((new JcaSignerInfoGeneratorBuilder(
					(new JcaDigestCalculatorProviderBuilder()).setProvider("BC").build())).build(sha512Signer,
							signerCertificate));
			gen.addCertificates((Store) certs);
			return gen.generate((CMSTypedData) msg, false).getEncoded();
		} catch (Exception ex) {
			log.error("Unable to sign the file!", ex);
			throw new SignException("Unable to sign the file!", ex);
		}
	}

	
	/**
	 * Pridej BouncyCastleProvider pokud neexistuje
	 */
	private void addSecurityProvider() {
		if (Security.getProvider("org.bouncycastle.jce.provider.BouncyCastleProvider") == null) { // BC provider doesn't exist
			Security.addProvider(new BouncyCastleProvider());
			log.info("...new BC provider added to Security...");
		}

	}

	
	/**
	 * Over platnost key store
	 * @param keystoreData obsah keystore s osobnim certifikatem
	 * @param password heslo ke keystore a privatnimu klici - ocekava se, ze je shodne!
	 * @throws SignException nepovedlo se pracovat s osobnim certifikatem
	 */
	private void configure(InputStream keystoreData, char[] password) throws SignException {
		try {
			KeyStore keystore = KeyStore.getInstance("pkcs12");
			try (keystoreData) {
				keystore.load(keystoreData, password);
				this.keystore = keystore;
			}
			this.alias = this.keystore.aliases().nextElement(); //vem prvni
			log.info("Keystore alias = {}", alias);
			this.privateKey = (PrivateKey) keystore.getKey(alias, password); //heslo musi byt shodne!
		} catch (Exception ex) {
			log.error("Unable to work with user certificate!", ex);
			throw new SignException("Unable to work with user certificate!", ex);
		}
	}
}
