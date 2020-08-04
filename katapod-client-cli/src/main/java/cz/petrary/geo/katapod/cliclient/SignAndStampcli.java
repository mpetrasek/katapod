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
import java.net.MalformedURLException;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "pao", header = "podepis a orazitkuj")
public class SignAndStampcli implements Callable<Integer> {

private static final Logger log = LoggerFactory.getLogger(SignAndStampcli.class);
	
	@ParentCommand
	private Katacli parent;

	@Parameters(paramLabel = "ADRESAR", description = "cesta k adresari se soubory k podpisu")
    private File signDir;
	
	@Parameters(paramLabel = "CISLO", description = "cislo overeni")
    private String verNumber;

	
	@Override
	public Integer call() throws Exception { 
		try {
		init();
		Signcli.signDir(signDir, verNumber, parent.cfg);
		Stampcli.stampDir(signDir, parent.cfg);
		
		return 0;
		} catch (Exception ex) {
			log.error("Doslo k chybe.");
			log.debug("Detail: ", ex);
		}

		return 1;
	}


	public void init() throws FileNotFoundException, MalformedURLException {
		parent.initConfig();

		if (!signDir.exists() || !signDir.isDirectory()) {
			log.error("Cesta {} nevede k zadnemu existujicimu adresari!", signDir.getAbsolutePath());
			throw new RuntimeException("Chybny vstupni adresar");
		}
	}


	public static void main(String[] args) {
		int exitCode = new CommandLine(new Katacli()).execute(args);
		System.exit(exitCode);
	}

	
}
