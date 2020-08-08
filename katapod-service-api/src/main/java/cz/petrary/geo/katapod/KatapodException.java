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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Vyjimka vyhozena pri jakekoliv chybe v prubehu podpisu.
 *
 */
public class KatapodException extends Exception {
	
	private static final Logger log = LoggerFactory.getLogger(KatapodException.class);

	
	private static final long serialVersionUID = 1L;

	public KatapodException(String msg, Throwable thr) {
		super(msg, thr);
		log.error(msg, thr);
	}

	public KatapodException(String msg) {
		super(msg);
		log.error(msg);
	}

}
