package com.hk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/16/12
 * Time: 3:12 AM
 * To change this template use File | Settings | File Templates.
 */


public class UrlFetch {
  private Logger LOGGER = LoggerFactory.getLogger(UrlFetch.class);
    private String urlStr_;
    private final URLFetchAttributes attributes_;
    private int responseCode_;
    private String redirectedURL_;

    public UrlFetch(String urlStr, URLFetchAttributes attributes) {
        try {
//            String crawlerPageServeMode = CommonApplicationProperties.crawlerPageServeMode();
//            if (crawlerPageServeMode.equalsIgnoreCase("test")) {
//                String pageServer = CommonApplicationProperties.crawlerPageServer();
//                urlStr = pageServer + URLEncoder.encode(urlStr, attributes.getEncoding() != null ? attributes.getEncoding() : new DefaultEncoding().getEncoding());
//            } else if (crawlerPageServeMode.equalsIgnoreCase("proxy")) {
//                String[] proxyTokens = CommonApplicationProperties.crawlerPageServeProxy().split("\\:");
//                System.setProperty("http.proxyHost", proxyTokens[0]);
//                System.setProperty("http.proxyPort", proxyTokens.length > 1 ? proxyTokens[1] : "");
//            }
        } catch (Exception e) {
            LOGGER.error("Exception caught while configuring urlFetch. Exception: ", e);
        }
        urlStr_ = urlStr;
        attributes_ = attributes;
    }

    public int getResponseCode() {
        return responseCode_;
    }

    public void fetchResponseCode() throws IOException {
        HttpURLConnection connection = null;
        try {
            responseCode_ = -1;
            URL url = new URL(urlStr_);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(attributes_.getConnectionTimeout());
            connection.setReadTimeout(attributes_.getSocketTimeout());
            for (Map.Entry<String, String> entry : attributes_.getRequestProperties().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.setRequestMethod("HEAD");
            responseCode_ = connection.getResponseCode();
        } catch (SocketTimeoutException ex) {
            processErrorStream(connection);
            throw ex;
        } catch (IOException ex) {
            processErrorStream(connection);
            throw ex;
        }
    }

    public String fetchPage() throws IOException {
        InputStream iStream = null;
        InputStreamReader iStreamReader = null;
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr_);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(attributes_.getConnectionTimeout());
            connection.setReadTimeout(attributes_.getSocketTimeout());
            for (Map.Entry<String, String> entry : attributes_.getRequestProperties().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            responseCode_ = connection.getResponseCode();
            if (responseCode_ == HttpURLConnection.HTTP_NOT_FOUND || responseCode_ == HttpURLConnection.HTTP_INTERNAL_ERROR || responseCode_ == HttpURLConnection.HTTP_NOT_ACCEPTABLE) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            if (attributes_.getCharset() == null) {
//                iStream = new SmartEncodingInputStream(connection, getInputStream(connection));
//                Charset streamCharset = ((SmartEncodingInputStream) iStream).getIdentifiedCharset();
//                iStreamReader = new InputStreamReader(iStream, streamCharset);
            } else {
                iStream = getInputStream(connection);
                iStreamReader = new InputStreamReader(iStream, attributes_.getCharset());
            }
            reader = new BufferedReader(iStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            if (attributes_.isCalculateRedirectUrl()) {
                redirectedURL_ = connection.getURL().toString();
            }
            return sb.toString();
        } catch (SocketTimeoutException ex) {
            processErrorStream(connection);
            throw ex;
        } catch (IOException ex) {
            processErrorStream(connection);
            throw ex;
        } finally {
            if (reader != null) {
                reader.close();
            } else if (iStreamReader != null) {
                iStreamReader.close();
            } else if (iStream != null) {
                iStream.close();
            }
        }
    }

    private void processErrorStream(URLConnection connection) throws IOException {
        if (connection instanceof HttpURLConnection) {
            InputStream es = ((HttpURLConnection) connection).getErrorStream();
            if (es != null) {
                while ((es.read()) > 0) ;
                es.close();
            }
        }
    }

    public String fetchPageByPost(String postData) throws IOException {
        InputStream iStream = null;
        InputStreamReader iStreamReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr_);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(attributes_.getConnectionTimeout());
            connection.setReadTimeout(attributes_.getSocketTimeout());
            for (Map.Entry<String, String> entry : attributes_.getRequestProperties().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.setDoOutput(true);
            OutputStreamWriter wr = attributes_.getCharset() == null ? new OutputStreamWriter(connection.getOutputStream()) : new OutputStreamWriter(connection.getOutputStream(), attributes_.getCharset().displayName());
            wr.write(postData);
            wr.flush();

            if (attributes_.getCharset() == null) {
//                iStream = new SmartEncodingInputStream(connection, getInputStream(connection));
//                Charset streamCharset = ((SmartEncodingInputStream) iStream).getIdentifiedCharset();
//                iStreamReader = new InputStreamReader(iStream, streamCharset);
            } else {
                iStream = getInputStream(connection);
                iStreamReader = new InputStreamReader(iStream, attributes_.getCharset());
            }
            reader = new BufferedReader(iStreamReader);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            if (attributes_.isCalculateRedirectUrl()) {
                redirectedURL_ = connection.getURL().toString();
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            } else if (iStreamReader != null) {
                iStreamReader.close();
            } else if (iStream != null) {
                iStream.close();
            }
        }
    }

    /**
     * Determine whether the given response is a GZIP response.
     * <p>Default implementation checks whether the HTTP "Content-Encoding"
     * header contains "gzip" (in any casing).
     *
     * @param connection the HttpURLConnection to check
     */
    private InputStream getInputStream(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        String encodingHeader = connection.getContentEncoding();
        if (encodingHeader != null) {
            if (encodingHeader.equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
                    LOGGER.debug("GzipStream url: " + connection.getURL().toString());
            } else if (encodingHeader.equalsIgnoreCase("deflate")) {
//                inputStream = new SmartInflateInputStream(connection, inputStream);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("DeflateStream url: " + connection.getURL().toString());
                }
            }
        }
        return inputStream;
    }

    public String getRedirectedURL() {
        return redirectedURL_;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Usage: URLFetch <URL>");
        }
        URLFetchAttributes attributes = new URLFetchAttributes(1800, 1800);
        UrlFetch urlFetch = new UrlFetch(args[0], attributes);
        //urlFetch.fetchResponseCode();
        System.out.println(urlFetch.fetchPage());
    }
}

