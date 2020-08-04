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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.SignConfiguration;


/**
 * Konfiguracni hodnoty nutne pro vytvoreni podpisu.
 * Jedna se o:
 *  <ul>
 *    <li>osobni certifikat UOZI - povinna hodnota, musi byt vzdy definovan</li>
 *    <li>heslo k osobnimu certifikatu - povinna hodnota, musi byt vzdy definovana</li>
 *    <li>moznost vymazat jiz existujici vystupni soubory z adresare - volitelne, nastaveno na <pre>true</pre> = mazat</li>
 * </ul>
 * 
 * Vyuziva pattern Builder pro sestaveni objektu
 */
public class SignConfigurationImpl implements SignConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(SignConfigurationImpl.class);
	
	/**
	 * Osobni certifikat
	 */
	private InputStream certificateStream = null;
	
	/**
	 * Heslo k osobnimu certifikatu
	 */
	private String certificatePassword = null;
	
	/**
	 * Maji se pred vypoctem odstranit existujici generovene soubory?
	 */
	private boolean cleanOldResultFiles = true;
	
	
	/**
	 * Privatni konstruktor - pouzij Builder
	 */
	private SignConfigurationImpl() {
	}
	
	/**
	 * Spravne sestaveni objektu SignConfigurationImpl
	 *
	 */
	public static class Builder {
		private String certificatePath = null;
		private String certificatePassword = null;
		private boolean cleanOldResultFiles = true;
		private InputStream certificateStream = null; 
		
		public Builder() {
		}
		
        public Builder withCertificateFile(String certificatePath){
        	this.certificatePath = certificatePath;
            return this;
        }

        public Builder withCertificate(InputStream certificateStream){
        	this.certificateStream = certificateStream;
            return this;
        }

        public Builder andPassword(String certificatePassword){
        	this.certificatePassword = certificatePassword;
            return this;
        }

        public Builder keepOldResultFiles(){
        	this.cleanOldResultFiles = false;
            return this;
        }

        public Builder setCleanResultFiles(boolean clean){
        	this.cleanOldResultFiles = clean;
            return this;
        }

        /**
         * Over platnost nastaveni a vrat sestavenou konfiguraci
         * @return
         * @throws SignException
         */
        public SignConfigurationImpl build() throws SignException {
        	SignConfigurationImpl config = new SignConfigurationImpl();
        	config.cleanOldResultFiles = this.cleanOldResultFiles;
        	
        	if (this.certificatePassword == null || this.certificatePassword.isBlank()) {
        		log.error("Certificate password is empty. Invalid configuration.");
        		throw new SignException("Certificate password is empty");
        	}
        	config.certificatePassword = this.certificatePassword;
        	
        	if (this.certificateStream != null) { //uprednostni certifikat ve streamu pres souborem
        		config.certificateStream = this.certificateStream;
        		
        	} else if (this.certificatePath != null && !this.certificatePath.isBlank()) {
        		try {
					config.certificateStream = Files.newInputStream(Paths.get(this.certificatePath), StandardOpenOption.READ);
				} catch (IOException e) {
					log.error("Cannot open certificate file on path {}. Invalid configuration.", this.certificatePath, e);
					throw new SignException("Cannot open certificate file on path " + this.certificatePath, e);
				}
        	} else { //certifikat neni nijak definovan - ani stream ani soubor
        		log.error("No certificate was configured. Set certificate with method 'withCertificateFile' or 'withCertificate'.");
        		throw new SignException("No certificate was configured. Set certificate with method 'withCertificateFile' or 'withCertificate'.");
        	}
        	
        	return config;
        }

	}
	
	@Override
	public InputStream  getCertificate() {
		return certificateStream;
	}
	
	@Override
	public String getCertificatePassword() {
		return certificatePassword;
	}
	
	@Override
	public boolean isCleanOldResultFiles() {
		return cleanOldResultFiles;
	}

	

}
