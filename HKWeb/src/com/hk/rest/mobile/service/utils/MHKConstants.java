package com.hk.rest.mobile.service.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 1, 2012
 * Time: 5:14:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MHKConstants {

    public static final String STATUS_OK = "ok";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_ACCESS_DENIED = "denied";
    public static final String STATUS_RELOAD = "reload";
    public static final String STATUS_REDIRECT = "redirect";
    public static final String STATUS_DONE = "Done";
    public static final String NO_RESULTS = "No results found.";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS ="Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_METHODS ="Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_LIST ="POST,GET,OPTIONS,DELETE,PUT";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN ="Access-Control-Allow-Origin";
    public static final String STAR = "*";
    public static final String TRUE = "true";
    public static final String SERVICES = "services";
    public static final String IMAGEURL = "/images/ProductImages/ProductImagesThumb/";
    public static final String IMAGETYPE = ".jpg";
    public static final String NOLOGIN_NOADDRESS = "1";
    public static final String LOGIN_NOADDRESS = "2";
    public static final String LOGIN_ADDRESS = "3";
    public static final String NO_ADDRESS = "No address selected";
    public static final String EMPTY_CART = "No Items in Cart";
    public static String getStringNullDefault(String data){
    	if(data==null)
    		return "";
    	return data;
    }
    public static Double getDoubleNullDefault(Double data){
    	if(data==null)
    		return 0.0;
    	return data;
    }
    public static Long getLongNullDefault(Long data){
    	if(data==null)
    		return 0l;
    	return data;
    }
   
}
