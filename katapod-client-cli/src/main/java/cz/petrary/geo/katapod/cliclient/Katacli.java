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
import java.util.Optional;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

/**
 * Klient pro podpis a casove razitko ovladany pres prikazovou radku.
 * 
 */
@Command(name = "Katacli", mixinStandardHelpOptions = true,
         header = "%n@|green Klient pro podpis a casove razitko ovladany pres prikazovou radku|@",
         subcommands = {Signcli.class, Stampcli.class, SignAndStampcli.class})
public class Katacli implements Callable<Integer> {
	
	public static final String GENERATED_FILE_NAME = "Overeni_UOZI.txt";
	
	private static final Logger log = LoggerFactory.getLogger(Katacli.class);

	@Option(names = { "-cfg", "--config-soubor" }, 
			paramLabel = "CONFIG", 
			scope = ScopeType.INHERIT,
			description = "konfiguracni soubor. Pokud neni zadan, pak se pouzije 'katapod.properties' ulozeny primo u programu")
	private File cfgFile = null;
	
	/**
	 * Zde jsou potrebne konfiguracni hodnoty
	 */
	ConfigValues cfg = null;

	
	
//    @Option(names = "-v", scope = ScopeType.INHERIT,
//    		description = "zapni detailni logovani") // option is shared with subcommands
//    public void setVerbose(boolean[] verbose) {
//        //Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
//        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//        Configuration config = ctx.getConfiguration();
//        //LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.getRootLogger().getName()); 
//        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.getLogger(Katacli.class.getName()).getName()); 
//
//
//        loggerConfig.setLevel(Level.DEBUG);
//        ctx.updateLoggers(config);
//
//        log.info("Nastaven rezim detailniho logovani: {}", verbose.length);
//        log.debug("verbose=true");
//    }

	
	/**
	 * Call je volan pouze v pripade, ze nebyl zadan zadny prikaz - a to je spatne
	 */
	@Override
	public Integer call() throws Exception { 
		log.error("Chybi prikaz. Zavolejte nektery z prikazu");
	    new CommandLine(new Katacli()).usage(System.out);
		return 0;
	}


	/**
	 * Inicializuje konfiguracni hodnoty. Nacti je z predaneho souboru nebo z default souboru.
	 */
	public void initConfig() {
		cfg = new ConfigValues(Optional.ofNullable(cfgFile)); 
	}

	
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Katacli()).execute(args);
		System.exit(exitCode);
	}

}
