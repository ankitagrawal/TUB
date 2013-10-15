package com.hk.util.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.hk.domain.core.JSONObject;

public class HkHttpClient {

    private static final ObjectMapper mapper;
    private static final JsonFactory jsonFactory;
    //  URLConnection connection;

    public static final String BROWSER_IE6 = "IE6";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String GZIP = "gzip";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected static final int MAX_CONN_PER_HOST = 3;
    protected static final int DEFAULT_TIMEOUT = 60;

    //DataOutputStream os;
    private static Random random = new Random();

    protected static String randomString() {
      return Long.toString(random.nextLong(), 36);
    }

    static String boundary = "---------------------------" + randomString() + randomString() + randomString();

    static {
      mapper = new ObjectMapper();
      mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      jsonFactory = mapper.getJsonFactory();
    }

    public static Object executeGet(String webServiceUrl) {
      return executeGet(webServiceUrl, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object executeGet(String webServiceUrl, Class responseClass) {
      Object response = null;
      try {
        URL url = new URL(webServiceUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection == null) {
          // TODO: log
        }

        //urlConnection.connect();

        System.setProperty("http.keepAlive", "false");

        int responseCode = urlConnection.getResponseCode();

        System.out.println(webServiceUrl + "  RC: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
          InputStream in = null;
          JsonParser jsonParser = null;
          try {
            long httpStartTime = System.currentTimeMillis();
            in = urlConnection.getInputStream();

            long httpEndTime = System.currentTimeMillis();
            System.out.println(" http T:" + (httpEndTime - httpStartTime) + " ms ");


            long startTime = System.currentTimeMillis();
            if (responseClass != null) {
              jsonParser = jsonFactory.createJsonParser(in);
              response = jsonParser.readValueAs(responseClass);
            } else {
              BufferedReader reader = new BufferedReader(new InputStreamReader(in));
              StringBuilder resp = new StringBuilder();
              String line = null;

              while ((line = reader.readLine()) != null) {
                resp.append(line);
              }

              response = resp.toString();
            }
            long endTime = System.currentTimeMillis();

            System.out.println(" parsing T:" + (endTime - startTime) + " ms ");
          } finally {
            if (in != null) {
              in.close();
            }
            if (jsonParser != null) {
              jsonParser.close();
            }
          }
        }
      } catch (MalformedURLException e) {
        System.err.println(e);
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
        System.err.println(e);
      } catch (Throwable t) {
        System.err.println(t);
      }

      return response;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static InputStream executeGetForStreamRespone(String webServiceUrl) {
      InputStream in = null;
      try {
        URL url = new URL(webServiceUrl);
        long startTime = System.currentTimeMillis();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection == null) {
          //TODO : log
          return null;
        }

        System.setProperty("http.keepAlive", "false");

        int responseCode = urlConnection.getResponseCode();

        System.out.println(webServiceUrl + "  RC: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
          try {

            in = urlConnection.getInputStream();
            long endTime = System.currentTimeMillis();

            System.out.println(" http T:" + (endTime - startTime) / 1000 + " sec ");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } catch (MalformedURLException e) {
        System.err.println(e);
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
        System.err.println(e);
      } catch (Throwable t) {
        System.err.println(t);
      }

      return in;
    }

    public static Object executePostForObject(String url, Map<String, String> params, Class responseClass)  {
      return executePostForObject(url, params, null, responseClass);
    }

    public static Object executePostForObject(String url, Map<String, String> params, Map<String, String> headers, Class responseClass) {
      PostMethod method = new PostMethod(url);
      if (headers != null) {
        for (String key : headers.keySet()) {
          method.addRequestHeader(key, headers.get(key));
        }
      }
      if (params != null) {
        method.addParameters(mapToNameValuePair(method, params));
        StringBuilder sb = new StringBuilder();
        JSONObject.appendJSONMap(params, sb);

        try {
          StringRequestEntity requestEntity = new StringRequestEntity(
              sb.toString(),
              "application/json",
              "UTF-8");
          method.setRequestEntity(requestEntity);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace(); //TODO:llogger
        }

      }
      return executePostForObject(method, BROWSER_IE6, responseClass);
    }

    @SuppressWarnings("unchecked")
    private static Object executePostForObject(PostMethod method, String browser, Class responseClass) {

      try {
        StringWriter sw = new StringWriter();
        Object response = new StringBuffer();
        emulateBrowser(method, browser);


        // execute the method
        HttpClient httpClient = createHttpClient(null);
        httpClient.executeMethod(method);


        int statuscode = method.getStatusCode();

        BufferedReader br = null;

        if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
            || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {

          //TODO: handle redirects
          return null;
        } else {

          if (responseClass != null) {
            JsonParser jsonParser = jsonFactory.createJsonParser(method.getResponseBodyAsStream());
            response = jsonParser.readValueAs(responseClass);
            return response;
          } else {
            Header hce = method.getResponseHeader(CONTENT_ENCODING);
            if (null != hce) {
              if (hce.getValue().equals(GZIP)) {
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(method.getResponseBodyAsStream())));
              }
            } else {
              br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));
            }
          }

          IOUtils.copy(br, sw);
          return sw.toString();
        }
//              while ((line = br.readLine()) != null) {
//                  response.append(line).append(LINE_SEPARATOR);
//              }

        // TODO improve exception handling
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        // always release the connection after we're done
        method.releaseConnection();
      }
      return null;
    }


    public static String executePost(String url, Map<String, String> params) throws UnsupportedEncodingException {
      return executePost(url, params, null);
    }


    public static String executePost(String url, Map<String, String> params, Map<String, String> headers) throws UnsupportedEncodingException {
      PostMethod method = new PostMethod(url);
      if (headers != null) {
        for (String key : headers.keySet()) {
          method.addRequestHeader(key, headers.get(key));
        }
      }
      if (params != null) {
        method.addParameters(mapToNameValuePair(method, params));
        StringBuilder sb = new StringBuilder();
        JSONObject.appendJSONMap(params, sb);

        StringRequestEntity requestEntity = new StringRequestEntity(
            sb.toString(),
            "application/json",
            "UTF-8");
        method.setRequestEntity(requestEntity);

      }
      return executePost(method, BROWSER_IE6);
    }

    public static NameValuePair[] mapToNameValuePair(PostMethod method, Map<String, String> map) {
      NameValuePair[] params = new NameValuePair[map.size()];
      int i = 0;
      for (Map.Entry<String, String> entry : map.entrySet()) {
        params[i++] = new NameValuePair(entry.getKey(), entry.getValue());
      }
      return params;
    }

    private static void emulateBrowser(HttpMethod method, String browser) {
      if (BROWSER_IE6.equals(browser)) {
        method.addRequestHeader(new Header("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0; Windows NT 5.1)"));
        method.addRequestHeader(new Header("accept", "*/*"));
        method.addRequestHeader(new Header("accept-language", "en-gb"));
        method.addRequestHeader(new Header("accept-encoding", "gzip"));
        method.addRequestHeader(new Header("connection", "Keep-Alive"));
        method.addRequestHeader(new Header("cache-control", "no-cache"));
      }
    }

    private static HttpClient createHttpClient(InetAddress iNetAdd) {
      MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
      HttpClient httpClient = new HttpClient(connectionManager);
      HttpMethodRetryHandler myretryhandler = new HttpMethodRetryHandler() {
        public boolean retryMethod(final HttpMethod method, final IOException exception, int executionCount) {
          return false;
        }
      };
      httpClient.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
      httpClient.setState(new HttpState());
      httpClient.getParams().setSoTimeout(DEFAULT_TIMEOUT * 1000);
      HostConfiguration hConfig = new HostConfiguration();
      if (iNetAdd != null) {
        hConfig.setLocalAddress(iNetAdd);
        httpClient.setHostConfiguration(hConfig);
      }
      httpClient.getHttpConnectionManager().getParams().setMaxConnectionsPerHost(hConfig, MAX_CONN_PER_HOST);
      httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
      return httpClient;
    }

    private static String executePost(PostMethod method, String browser) {

      try {
        StringWriter sw = new StringWriter();
        StringBuffer response = new StringBuffer();
        emulateBrowser(method, browser);


        // execute the method
        HttpClient httpClient = createHttpClient(null);
        httpClient.executeMethod(method);


        int statuscode = method.getStatusCode();

        BufferedReader br = null;

        if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
            || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {

          //TODO: handle redirects
          return null;
        } else {
          Header hce = method.getResponseHeader(CONTENT_ENCODING);
          if (null != hce) {
            if (hce.getValue().equals(GZIP)) {
              br = new BufferedReader(new InputStreamReader(new GZIPInputStream(method.getResponseBodyAsStream())));
            }
          } else {
            br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), method.getResponseCharSet()));
          }
        }

        IOUtils.copy(br, sw);
//              while ((line = br.readLine()) != null) {
//                  response.append(line).append(LINE_SEPARATOR);
//              }
        return sw.toString();
        // TODO improve exception handling
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        // always release the connection after we're done
        method.releaseConnection();
      }
      return null;
    }
}
