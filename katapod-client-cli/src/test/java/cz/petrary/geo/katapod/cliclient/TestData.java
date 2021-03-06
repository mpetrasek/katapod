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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

public class TestData {
	
	
	//openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 3650
	//openssl pkcs12 -export -out keyStore.p12 -inkey key.pem -in cert.pem -name tester
	
	private static final String KEYSTORE_P12 = 
			"MIIQhgIBAzCCEEwGCSqGSIb3DQEHAaCCED0EghA5MIIQNTCCBk8GCSqGSIb3DQEHBqCCBkAwggY8AgEAMIIGNQYJKoZIhvcNAQcBMBwGCiqGSIb3DQEMAQYwDgQI+zkQnbPA8J8CAggAgIIGCAEx/USGYRe+aiffRIHMjQyTIYN0z5U0HKY0YaQSpzxu8xRfgnK20rTvhMIsxIZKxCKfZ0YL/WCnj8CMuZ+TUwf4lx5xj+NeeSZ1HI6xTv95kgJT2GkUCkqP537M/8h6VZ7pjcoMlf7P2S9zVWwZl/fya/4I5DdzhzT8H5KfYUlSutiSFc7wn/WLa16UO3+VDWsnzv8vO/Q52hVgk/iwOLONQycoViGiJlZmoIGrM2HQeTvRZ5jXL9UVuG1o7YMccb6XvwEio3Ud2oj7SL+LCHB4LJGgXOjB2C9DCky+ZRxjBpOX7hSXT01nFIoX6+nR4Hi2V9RFn3CTwiw3CEYdPDazCYgEQMJ3bPA6CsLPAAyqdcL52GA/13ef2Ih8budLCmCt1f2g0oKeagD3KvejN2dxLg295hWbKYeZ52lkYY/V4iARM+IG+In3KKdH/hOYQQsECaWFHsCxw9OVYdQcK8o9ievSTcJ57L1mZtRml1tfTWDRyx9Lc84ueAM31Tu1kKlcgWfVOdK2/4YrnkPV/u/Dv7mI73hQcrgzsd9nRryzS0KDmWvbcM6+XMnzp7i3yA2NdZ5XHSBOqkTnbH4lwmZ3GKdRfv7hQnZYH8t0lnjM1BSaFRrNgAQUWKQ/9ZbBCNznS9NU7CQuc/cUJm+m10cDTYP8HUmoAgQrVS3KRfQR8Cg89ZQvSAQSfg3K1aDKCh2lzlw1slXwZHrSZeIMW8NAH9Cqb17LtNn7CRiUmY7sRdEjxn7zBhqEb3zLfQxKea7w5AOs341rouUYKHYhnpZAOXIcNY2bPUK33bjB7J8Vhm4sT77vRrT+o1I+z+jio4d56xhBAfLicFs1wL28M3NwQd0dIsL5ynZywa38ATvo4z9AhzeqwdCDrMau0REB64SMUQusYaSXgkUOj+tI6tM11LMiY6JO1LmobGfDMdBmFiHqZimJ1PN0y26CqPCTha0W2lG4sX/jksQSxL9qJ8eUIAUyBST12jWg+8eFYRUry9KPWecyhJuYx/pYVEDdM9oadS0KaMrScYEKcvVSIbK1kwW0y2PdmY/BIfdj56B8xm+A5y73nMinda+A2XoW1Z/sfe+Yrs2WYF52P4kOfpV2hICQfZoQDGueCd1xfbGMjpBPeSc61AapZgR1qyd6T+Q7cvq/w29lBEz7PVsy1Kq9X7LmzRiVotSKjMlHLsEdIM9C/XN0OHKm7ZN20CAZpKj2hVZlYTknlq+bv5iBCoRopuHcnBC/TCfZFQSg1RRF+agF+MAMzaj1U/L6bjQPbFwHij3TT78ke0+vEBNPM3N+HAVoiO3jh7j7UBwipxWiwgzXtmwg6Ep0S5lKuRttPyXoIZfQNUMOivaX6WrD8rXgHyraJ5aG/0jpbSDaTkkYe+GosR84PVSVT4NW06EdCv7zNvYUXaD797ZSvISDyvgYnkobBNf/4FZAPVt5cIaAJsrpgcMLUfbrN+IGUSkEi1KI7FwrPO3VS0AQ6TrSppIlgfWamBwZr7uSY/3C1C08hJO5Ws996MEDhy5JFC51Leu3h4wzBX6+hRPQwN7L6Ht5YQGpzMHgxQIMI5+i2At/2Si6m6hTnbe4eqYSz1b7oJVAEMmprhppBJ3rkd93XAmL8dFvgihwyUzfRbGqr69K/ug6gnj6WaWTmDdXmaiOAnyfuy/ecU/Yd/QfsePg4VlNV5l+lykRax4zeaIYZAJq03Eiuee1RUQqFLD3JhmcyMPOQf5iASRnrLDwJJ+Hzr/JyTlsXn9WJxwocfz0OeokeGM0qi4ZGTmeFA1fal8s5Pyg+4YTKpjIVBm0Fmnfkgwo8V0iR/puqCds49rjxdiKS3vv/9kOGX3fE40AismXEVAOzoSN3Kwo1G5ITLbIR3GSYT6tuf9zY6yGT+R4RuWLNtOq+SDIJ7Gg7fQYUwA3o3tnueeCgO889vCOmkA/GOUHOLAAHT49DLScVpskgT/XthozvyM2DNMp+KNLUCdgEi+TOSUJuUOTOmb8rPsn+BfN7agIdUk5PkkWuVAr1q/82RP4h8et6VEg6Yqv0bnbblLfSZX+pgHTMIIJ3gYJKoZIhvcNAQcBoIIJzwSCCcswggnHMIIJwwYLKoZIhvcNAQwKAQKgggluMIIJajAcBgoqhkiG9w0BDAEDMA4ECPh+jD8vQNucAgIIAASCCUjHXRLMvf+27CQl+042t7PFFFS76rm+K6vDRiij/Fty2YCJBc16v0dxxaomVVo1AswSZCkLaXL96PjVFXGH96Gt3/WOjTh7BlumNj7xXq25jygZ+tLERnyZXiCPE0pMRE+ZzCp36T2N5K3ZDzF7oA2niD9DAemEvogGqymyIYKSyX125EW+trQQSAc0qwVsOTWboZo05eKDBt7477dhkCZm9Kag+CA0t8IJeihXJzdZZ4RXWCKFDt9wEUP7R9BDV3rgV32jxvf9brMJK0LqBlgHFy+ddpfQrvKICKdGqqcvh8aisYg3XPJFyU7q0iDXIEBjUAfPaYevoIrb+S2udP3zPJmCHd4s0r8EsiJNsv6TFmWEwV3kxT3Sc4nAlF5/fQUsXstyham6BOpVK3Nof5fv0YHVpdDK5vRN0xp8IoCw2cTCd2KUiBdIbSlLyXOVLW57hSf1CaqVjiz8FlPcdbRAxLjJQHw9ppHqe8Kp1Lk16JJqrIwCR1kDGDQ1ES3oljJPoFCsBJGzMutVNAQFPiSGqU7k8ctR3c+LUL8avQHCBugk6dcWhus2+vgFSXCo9/LPKpKhkEqnFB2wBx2ivzEosTIO4At2uHB4a7qDM6qPXLOebl2VjdMDre9En6wdX7jpE2awTO60ZbJ+IIvjNHI52xsVtvzC3zGjxQWJqExvaMLp35YXue4/wuVVSLp51/oKeTUbZy/CBWCdRnFLRTvr8TXHuO3eN7d5dhuGg5FD2v3+Ig/mdY9foRP26aZwHNQH2jjz6DGXtO5wAL1QjRLrE2+L6t/IMvWcTLERU+wvOFIuE3srCYeU/1UK/M2S3LCNJ2cDwkV+XJIbC/9aRmqSBeq/Jwz1RPKs/ipxAgU6QiMtbPH4z9dcmmcJK/DtwcXcPvSNw8jq1chEKdyow1981vA1Cxd1dSqm4XRNW2zM4TnxAdxuccjaGr7px2cstijnxFBiE7yTaVrtustOqAK7rM01Y1mS2m1GPWJb5u7z9zMiXZe84lF0JDtSGHT0xYfhjpK++1hexJyADLzvHiuSvFE2wCV61lRm29eTt0e7MyolEaA9an3g7KLVC/KuD/pkVO1VQmMpRoaMgcSlxV207vUzr3xuVhVlAEc8ohHU3K9pJRVm6JnpNVH4kQPE4xb0a2SOLJOJVCA+FFnqhzDOvTsHTgd7VMU8/7XS7f1kNFrOttBpxawA10JvKKc7hquLYgOUC1IJX4tzdbFOJUs/N6WZ4ruW6Q9M04eR39lYNocMaORF/wTYulCZgM5zz4CRtFdgbv2SwrlNIEse0VGxJysjIKCKkPFB3Vaj3e00OPmAI2cJRQ8Xvw4NRqWS9I5vq/CNPCj9+a5TDvthtsHZN9EtG213KbdY+w9X/UisJcgGeJnlUgsgGABneww5fs7BfhZ8/3APH7XnYxsGU3TWdKCHnUhAGVV3svqaBGD0Q+Q8mIc85eGxxGdIqtPBjqIbUra+4NBSASkNJz8i9/qvL8J5nycsoEdC01aGK2uk4zhAAYew6r0WhSvHM1EdA3/Fa37I5r2QDPT2kpuTdr/EOhPi6hZIZIGt3gIBsHZzQpDILD6iauGp1Ubo4MWoD3iQQr+Mb+pjPP+oorJKQfpeCYu/TvvddtbQ89lOxxrNm29BuaVBn2D7sHh/KpP9I0KLNa5EPiZxKhaT0lDHn8Q933SWdoGelgKFSgtpy6FDFsJQJOPs6jtSEt6n6Sr6WGNNy8L3HMd6KtNnYZtQEBdBCjhyyGeGRzJZ6swwvL+fjZJR9AvJdCNg47IPAxEIsHESIlN4P86ybXzOiqtLt+GRHz32nVX1KiBz/v0xijBZOe1IqoqMXuFSpjg3rhFqAPjlkAe6tTl9aoH/A0dzdhMVysqniGV4vcle6sbJm3lROtsPI6h5EcSDNwKtV5PpSfO2IAL2Nk9ZqT6nTyMiYOamuwV7ISBSZu5iIiLFB6iT3Dj2iimZiGmUmkxd2UtZjvQcptBXLP3YjJbHjj/AlajEGJADSCs/0GppUsKYen+yw0h8X/7GwH7I+Xbjf6GfT7g0EgsJaSBhDFGjp+MXngLNhBAtqiq9K99qk6lwUIHuz90y72psikK+SY+KttGA8skf0aICQr1E5CTwTfl8CULSktisjd37YdmC6gR4hxY7IvbAvhv74md9v1fIVYpD6/5QxWO3ny06SiyGuYrtuB5sP/DdOoCyDBnq/DBvCO631/mM5+OwDsOKRzSW887G7dTxTkwZUFOTjkdDwC7nSGWcnSnhJP2eygFT5IXCAf36Dv2bwZnrKDAFRbJfytocyugnK3qN8PYaE9jz2hsECsvSDsWqhDus1KHsuG7uK1IiuLCObNWFi/Qyb4AKRRes5OXce96yJDzKrkAq1qGFqD+9dkRWnh8j/YA0jIczu3KVh7Ew3ZCUj3/En2xWMP8hUieYkXqBRnBu8zdVkjF+Zs6P0+M7xrZABTeMeFzr/nsxLGbUZpAguGSxss3ZCgD9Lw9s1GIzXnQ52RZBeEku+KGaD80kYe98XDJRkD0L7II75WkF5hTgwSX1uax+Sqxzqf4NKBBrMCsOZ/6fm/8qnhhnyo2DAii6co7oGDSNXV4MhLZvJTuXSbOD8H7a0BZtoQ7quqR2GkzkhZTr0U7Yez4u1iJvR3wVxQWxE0twu6F/OaQiT6bctbGOprt/pxtgTpk0W2+i4Hq9NZ6z6fGOX73+Vcb/UecPG3ZAP4RYpUB37d6nUj6SCaGaei0rKY0EWdvmqF4d+c712ehhD3C5kNCq86d8E7xjL+pqrR+2TIj0tgERDYeaNjjmAX2BpTugzJtjAzwOdplfdf+nzAsdGwTPMp/9JFtdK6qxIuaxRPS7Z6D3wYjvtwV1EOp6TrOmaVZm6PNj7nhEt5HfvsnJTgkx+E0XODrRjXN5yztxuumdN47PEqM45EGGa7ZGBsIHfaWHAC/7L8CJhkdCNQ6K+gnPhiyHLLPhWUJlsuPD7SdssrSg7p9JyLMRzdKqHE86c2r3/pjvZwHpNUPQJ/Hfg3eBYjRvI5hdDHP+OSrwq5qYpRHbbA99ZzAJRz6xCU4ye5sOii77LbTx7IquN0HUCy0EKpyvNh5Cp8AeZSQWkvQPXxR+1kCdhj0WoqQD3qklOOdVIcuz7YMgluqlLImyUXGmlVowEjZG+Q0xQjAbBgkqhkiG9w0BCRQxDh4MAHQAZQBzAHQAZQByMCMGCSqGSIb3DQEJFTEWBBQq0P/9M0LEeF6gTVrPW8RDn8A2wDAxMCEwCQYFKw4DAhoFAAQUh7tlSq66oM75g3XLKjZl4/5w11UECAyyRtegbhqvAgIIAA==";

	public static final String PASSWORD = "password";
	public static final String OUZI_NAME = "TESTER";
	public static final String NUMBER = "45/2020";
	
	
	public static InputStream keystore() {
		return new ByteArrayInputStream(Base64.decode(KEYSTORE_P12));
	}
	
	
	
	public static Map<String, InputStream> testFiles() {
		Map<String, InputStream> result = new LinkedHashMap<>();
		for (int i = 0; i < 5; i++) {
			result.put("soubor"+i+".txt", new ByteArrayInputStream(("HASH"+i+"\n").getBytes()));
		}
		return result;		
	}
	
	
	public static String correctTextContent() {
		StringBuffer sb = new StringBuffer();
		sb.append("Náležitostmi a přesností odpovídá právním předpisům.\n");
		sb.append(NUMBER).append("\n");
		sb.append(LocalDate.now().format(DateTimeFormatter.ofPattern("d.M.yyyy"))).append("\n");
		sb.append(OUZI_NAME).append("\n");
		sb.append("----\n");
		
		sb.append("soubor0.txt;");
		sb.append("E42C5E45751ABC4DB025B60BA3E477737B3A85C0633AFCDA90D3C68E441B5B8A9038B11065944DE961CA09454FC287BF39CFD3AE802FAEC5776F84AFC1C3854D");
		sb.append("\n");
		
		sb.append("soubor1.txt;");
		sb.append("4FC1D08E974B702E77C3165615459D9BAE74E6487B78CEEB8D6F7CA794B92A2331CF79D9372F63430DBCDB48C4368FDB08D4EF7708F0427F1A20AC153799D295");
		sb.append("\n");

		sb.append("soubor2.txt;");
		sb.append("C45471D87A2BBD19FA7EDDC1F1A4D3026E41E1EDB0757B9BADD23EB6B403703E04000167387886133E80C8B6B4BEA1C93459702B82271000957FCC34D5F25778");
		sb.append("\n");
		
		sb.append("soubor3.txt;");
		sb.append("A207CADAA16CB59F0D8BD26D9EB3267BFF14CC87DE335F69DE0949C5CBC6D393F278573F579CC39A2E9AFA03F02F33D5C5E416372A4089B780AFA2E67876E725");
		sb.append("\n");

		sb.append("soubor4.txt;");
		sb.append("DC54FCAC04A998CD694FF67CA8CA295A914A31940AA41D5C1E0F5614D968E1377249C2A3BEB6BB67FEB7903E7DCBB25DB2B21956B5DD21877947F1ED2E556A8B");
		sb.append("\n");

		return sb.toString();
	}

}
