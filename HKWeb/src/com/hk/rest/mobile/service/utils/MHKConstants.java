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
    public static final String USER_DOES_NOT_EXIST = "User with the mentioned email is not in the record";
    public static final String COD_NOT_IN_PINCODE = "Cash on Delivery is not available for pincode: ";
    public static final String PLEASE_LOGIN = "Please log in";
    public static final String NO_ADDRESS_AVAILABLE = "No address available";
    public static final String INVALID_ADDRESS = "Invalid address";
    public static final String OUT_OF_STOCK = "Out of stock";
    public static final String COMBO_VARIANT_QUANTITY_ERROR = "Combo product variant quantity is not in accordance to offer";
    public static final String MIN_QTY_PROD_VARIANT = "Minimum Quantity should be one";
    public static final String PROD_ADDED_CART = "Product has been added to cart";
    public static final String PROD_NOT_ADDED_CART = "Product could not be added to cart";
    public static final String COD_CONTCT_NAME_BLANK = "Cod Contact Name cannot be blank";
    public static final String COD_CONTACT_PHN_BLANK = "Cod Contact Phone cannot be blank";
    public static final String COD_CONTACT_NAME_LENGTH = "Cod Contact Name cannot be longer than 80 characters";
    public static final String COD_CONTACT_PHN_LENGTH = "Cod Contact Phone cannot be longer than 25 characters";
    public static final String COD_CONTACT_SUBSCR_CART = "Cod is not allowed as you have subscriptions in your cart";
    public static final String COD_NOT_FOR_SERVICES = "Currently Cod is not available for services";
    public static final String COD_NOT_FOR_SERVICES_PYMT= "Currently Cod is not available for services, Please make payments separately for products and services";
    public static final String PWD_RESET_MSG = "Password reset mail has been sent to your email id. Please check your Inbox/Spam/Junk folders.";
    public static final String INVALID_LOGIN_CRDNTLS = "Invalid login credentials.";
    public static final String NO_SUCH_PRDCT ="No such product found";
    public static final String EMAIL_ID_ALRDY_EXIST = "email id already exists";
    public static final String EYEGLASSES = "eyeglasses";
    public static final String LENSES = "lenses";
    public static final String NO_STEP_UP="Maximum quantity reached";
    public static final String NO_ORDER_EXIST = "No such order exist. Please check again.";
    public static final String COUPON_CODE_RQD = "Coupon code required";
    public static final String INVALID_COUPON = "Invalid Coupon code";
    public static final String USED_OFFER = "This offer has already been used";
    public static final String APPLIED_COUPON = "This coupon has already been applied";
    public static final String EXPIRED_COUPON = "This coupon has expired";
    public static final String OFFER_APPLIED = "Offer applied";
    public static final String OFFER_REMOVED = "Offer removed";
    public static final String ERROR_ALREADYUSED="You have already availed the discount using this coupon.";
    public static final String ERROR_ALREADYAPPLIED= "The offer associated with this coupon has now been selected.";
    public static final String ERROR_ALREADYREFERRER = "You have already used someone else's referral coupon. You cannot use referral coupons from two different users.";
    public static final String ERROR_REFERRALNOTALLOWED = "You have already signed up without your friend's referral. Sorry :)";
    public static final String NO_COUPON_CODE = "No coupon code";
    public static final String NO_COUPON_OFFER_SELECTED = "No coupon offer";
    
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
