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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.petrary.geo.katapod.StampConfiguration;


/**
 * Konfiguracni hodnoty nutne pro vytvoreni casoveho razitka.
 * Jedna se o: 
 *  <ul>
 *    <li>URL na poskytovatele casovych razitek - povinna informace</li>
 *    <li>jmeno uzivatele pro sluzbu casovych razitek - volitelna</li>
 *    <li>heslo uzivatele do sluzby casovych razitek - volitelna</li>
 * </ul>      
 * 
 * Implementuje Builder pattern pro zjednoduseni prace s konfiguraci.
 */
public class StampConfigurationImpl implements StampConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(StampConfigurationImpl.class);
	
	//Stamper configuration
	private URL tsaUrl;
	private Optional<String> tsaUserName;
	private Optional<String> tsaUserPasswd;
	
	private StampConfigurationImpl() {
	}
	
	public static class Builder {
		private String tsaUrl = null;
		private Optional<String> tsaUserName = Optional.empty();
		private Optional<String> tsaUserPasswd = Optional.empty();
		
		public Builder() {
		}
		
        public Builder withTSA(String tsaUrl){
        	this.tsaUrl = tsaUrl;
            return this;
        }

        public Builder andUser(String tsaUserName){
        	this.tsaUserName = Optional.ofNullable(tsaUserName);
            return this;
        }

        public Builder andPassword(String tsaUserPasswd){
        	this.tsaUserPasswd = Optional.ofNullable(tsaUserPasswd);
            return this;
        }

        
        /**
         * Sestav StampConfiguration a hlavne zkontroluj platnost zadanych hodnot.
         * @return StampConfiguration
         * @throws StampException
         */
        public StampConfigurationImpl build() throws StampException {
        	StampConfigurationImpl config = new StampConfigurationImpl();
        	
        	if (this.tsaUrl == null || this.tsaUrl.isBlank()) {
        		log.error("TSA service URL is empty. Invalid configuration.");
        		throw new StampException("TSA service URL is empty");
        	}
        	
        	try {
				config.tsaUrl = new URL(this.tsaUrl);
			} catch (MalformedURLException e) {
				log.error("Invalid TSA service URL - {}", e.getMessage());
				throw new StampException("Invalid TSA service URL", e);
			}
        	
        	config.tsaUserName = this.tsaUserName;
        	config.tsaUserPasswd = this.tsaUserPasswd;
        	return config;
        }

	}

	@Override
	public URL getUrl() {
		return this.tsaUrl;
	}

	@Override
	public Optional<String> getUserName() {
		return this.tsaUserName;
	}

	@Override
	public Optional<String> getUserPasswd() {
		return this.tsaUserPasswd;
	}

	

}
