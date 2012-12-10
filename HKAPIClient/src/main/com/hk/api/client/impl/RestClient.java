package com.hk.api.client.impl;

import com.hk.api.client.constants.AuthConstants;
import com.hk.api.client.exception.HKBadHTTPResponseException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 1:07 PM
 */


class RestClient {
    public static final int HTTP_OK=200;
    public static final String DEFAULT_SERVER_URL = AuthConstants.hkRestUrl;

    public static String doGet(final String url, String serverUrl) {
        return doGet(url,null);
    }

    public static String doGet(final String url, Header[] headers, String serverUrl) {
        try{
            final HttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            HttpGet httpget = new HttpGet(serverUrl + url);
            if(headers!=null){
                httpget.setHeaders(headers);
            }
            HttpResponse response = httpClient.execute(httpget);

            checkResponseStatus(response);
            authenticateResponse(response);

            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            return read(instream);
        }catch (IOException e){

        }
        return null;
    }

    public static String doPost(final String url, final String POSTText, String serverUrl){
        return doPost(url,POSTText,null);
    }

    public static String doPost(final String url, final String POSTText, Header[] headers, String serverUrl){
        try{
            final HttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(httpClient.getParams(), 10000);
            HttpPost httpPost = new HttpPost(serverUrl + url);
            StringEntity entity = new StringEntity(POSTText, "UTF-8");
            BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json");
            httpPost.getParams().setBooleanParameter(
                    "http.protocol.expect-continue", false);
            entity.setContentType(basicHeader);
            httpPost.setEntity(entity);
            if(headers!=null){
                httpPost.setHeaders(headers);
            }
            HttpResponse response = httpClient.execute(httpPost);

            checkResponseStatus(response);
            authenticateResponse(response);

            InputStream instream = response.getEntity().getContent();
            return read(instream);
        }catch (IOException e){

        }
        return null;
    }

    public static boolean doPut(final String url, final String PUTText)
            throws URISyntaxException, HttpException, IOException {
        final HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams
                .setConnectionTimeout(httpClient.getParams(), 10000);
        HttpPut httpPut = new HttpPut(DEFAULT_SERVER_URL + url);
        httpPut.addHeader("Accept", "application/json");
        httpPut.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(PUTText, "UTF-8");
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPut);

        checkResponseStatus(response);
        authenticateResponse(response);

        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == HTTP_OK ? true : false;
    }

    public static boolean doDelete(final String url) throws HttpException,
            IOException, URISyntaxException {
        final HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams
                .setConnectionTimeout(httpClient.getParams(), 10000);
        HttpDelete httpDelete = new HttpDelete(DEFAULT_SERVER_URL + url);
        httpDelete.addHeader("Accept",
                "text/html, image/jpeg, *; q=.2, */*; q=.2");
        HttpResponse response = httpClient.execute(httpDelete);

        checkResponseStatus(response);
        authenticateResponse(response);

        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode == HTTP_OK ? true : false;
    }


    private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    private static void checkResponseStatus(HttpResponse response){
        if(response.getStatusLine().getStatusCode()!=HTTP_OK){
            throw new HKBadHTTPResponseException(response.getStatusLine().getStatusCode()+response.getStatusLine().getReasonPhrase());
        }
    }

    private static String authenticateResponse(HttpResponse response){
        //to-do improve logic once we start validating every response from HK
        /*Header[] tokenHeaders=response.getHeaders(HKAPITokenTypes.USER_ACCESS_TOKEN);
        if(tokenHeaders!=null){
            HKAPIAuthenticationUtils.isValidUserAccessToken(tokenHeaders[0].getValue());
        }else{

        }*/
        return "";
    }


}
