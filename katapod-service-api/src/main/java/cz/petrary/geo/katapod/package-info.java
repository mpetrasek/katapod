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


/**
 * Interface sluzby pro podpis a orazitkovani souboru v adresari, jak je 
 * pozadovano podle § 18 odst. 5 vyhlášky č. 31/1995 Sb:
 * 
 * Ověření výsledků v elektronické podobě se provádí připojením textového souboru obsahujícího 
 * text podle § 16 odst. 4 zákona, číslo z evidence ověřovaných výsledků a hašovací funkcí 
 * vytvořené otisky souborů, které obsahují ověřované výsledky. 
 * Textový soubor podepíše úředně oprávněný zeměměřický inženýr uznávaným elektronickým podpisem, 
 * ke kterému připojí kvalifikované časové razítko. Ověření výsledků tvořených jedním souborem je 
 * možné provést bez vyhotovování textového souboru, pokud tento výsledek přímo obsahuje text 
 * podle § 16 odst. 4 zákona a číslo z evidence ověřovaných výsledků.
 * 
 * (https://www.zakonyprolidi.cz/cs/1995-31)
 */
package cz.petrary.geo.katapod;