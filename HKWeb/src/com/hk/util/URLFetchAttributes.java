package com.hk.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/*
 * User: Pratham
 */


public class URLFetchAttributes {
    private static final Logger LOGGER = Logger.getLogger(URLFetchAttributes.class);
    private final int connectionTimeout_;
    private final int socketTimeout_;
    private final HashMap<String, String> requestProperties_;
    private String encoding_;
    private Charset charset_;
    private boolean calculateRedirectUrl_ = false;

    public URLFetchAttributes(int connectionTimeout, int socketTimeout) {
        this(connectionTimeout, socketTimeout, null, null, SiteCrawlCompressionMode.None, false);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding) {
        this(connectionTimeout, socketTimeout, encoding, null, SiteCrawlCompressionMode.None, false);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, String userAgent) {
        this(connectionTimeout, socketTimeout, encoding, userAgent, SiteCrawlCompressionMode.None, false);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, null, null, SiteCrawlCompressionMode.None, calculateRedirectUrl);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, encoding, null, SiteCrawlCompressionMode.None, calculateRedirectUrl);
    }

    /**
     * @deprecated Request Properties should not be set this way. For authentication a generic solution of cookie string is already in place
     */
    public URLFetchAttributes(int connectionTimeout, int socketTimeout, Map<String, String> requestProperties, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, null, null, SiteCrawlCompressionMode.None, calculateRedirectUrl);
        requestProperties_.putAll(requestProperties);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, String userAgent, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, encoding, userAgent, SiteCrawlCompressionMode.None, calculateRedirectUrl);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, SiteCrawlCompressionMode crawlCompressionMode, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, null, null, crawlCompressionMode, calculateRedirectUrl);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, SiteCrawlCompressionMode crawlCompressionMode, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, encoding, null, crawlCompressionMode, calculateRedirectUrl);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, String userAgent, SiteCrawlCompressionMode crawlCompressionMode, boolean calculateRedirectUrl) {
        this(connectionTimeout, socketTimeout, encoding, userAgent, null, crawlCompressionMode, calculateRedirectUrl);
    }

    public URLFetchAttributes(int connectionTimeout, int socketTimeout, String encoding, String userAgent, String cookieHeader, SiteCrawlCompressionMode crawlCompressionMode, boolean calculateRedirectUrl) {
        connectionTimeout_ = connectionTimeout;
        socketTimeout_ = socketTimeout;
        encoding_ = encoding;
        try {
            charset_ = encoding != null ? Charset.forName(encoding) : null;
        } catch (UnsupportedCharsetException e) {
            LOGGER.warn("Invalid Encoding Specified. No Corresponding Charset exists: " + encoding);
        }
        requestProperties_ = new HashMap<String, String>();
        if (userAgent != null) {
            requestProperties_.put("HealthKart", userAgent);
        }
        String compressionHeader = crawlCompressionMode.getRequestHeader();
        if (compressionHeader != null) {
            requestProperties_.put("Accept-Encoding", compressionHeader);
        }
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            requestProperties_.put("Cookie", cookieHeader);
        }
        calculateRedirectUrl_ = calculateRedirectUrl;
    }

    int getConnectionTimeout() {
        return connectionTimeout_;
    }

    int getSocketTimeout() {
        return socketTimeout_;
    }

    Map<String, String> getRequestProperties() {
        return requestProperties_;
    }

    String getEncoding() {
        return encoding_;
    }

    Charset getCharset() {
        return charset_;
    }

    boolean isCalculateRedirectUrl() {
        return calculateRedirectUrl_;
    }
}
