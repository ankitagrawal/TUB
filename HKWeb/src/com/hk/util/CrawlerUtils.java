package com.hk.util;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/7/12
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public abstract class CrawlerUtils {
    public static void setHttpsRequestConfig() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
//        sslContext.init(null, new TrustManager[]{new HttpsTrustManager()}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//        HttpsURLConnection.setDefaultHostnameVerifier(new HttpsHostnameVerifier());
    }
}
