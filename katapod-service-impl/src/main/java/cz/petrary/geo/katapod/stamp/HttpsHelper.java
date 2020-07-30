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

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Vytvoreni HTTPS pripojeni. Akceptuje vsechny certifikaty a vsechny hosty.
 * Je potreba pro komunikaci s TSA.
 *
 */
public class HttpsHelper {
	
	
	/**
	 * Create new HTTPS connection and set username + password.
	 * @param targetUrl
	 * @param userName
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	static HttpsURLConnection getConnection(String targetUrl, 
			                                Optional<String> userName, 
			                                Optional<String> userPassword) throws Exception {
	    URL url = new URL(targetUrl);
	    SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts(), new SecureRandom());
	    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	    conn.setSSLSocketFactory(sc.getSocketFactory());
	    conn.setHostnameVerifier(allHostnameVerified());
	    
	    if (userName.isPresent() && userPassword.isPresent()) {
	    	String credentials = Base64.getEncoder().encodeToString((userName.get() + ":" + userPassword.get()).getBytes("UTF-8"));
	    	String basicAuth = "Basic " + credentials;
	    	conn.setRequestProperty("Authorization", basicAuth);
	    }
	    
	    return conn;
	  }
	
	
	/**
	 * Create a trust manager that does not validate certificate chains.
	 * @return
	 */
    private static TrustManager[] trustAllCerts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        } };
        return trustAllCerts;
    }
	
    
    /**
     * Every hostname is verified
     * @return
     */
    private static HostnameVerifier allHostnameVerified() {
    	return new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }


}
