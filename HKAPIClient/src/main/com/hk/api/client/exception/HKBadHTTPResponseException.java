package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 8:10 PM
 */
public class HKBadHTTPResponseException extends HKAPIBaseException {
    private static final long serialVersionUID = 1L;

    public HKBadHTTPResponseException(){
        super();
    }
    public HKBadHTTPResponseException(String message) {
        super(message);
    }
}
