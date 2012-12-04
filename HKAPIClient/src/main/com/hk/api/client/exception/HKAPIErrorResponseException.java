package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 9:46 PM
 */
public class HKAPIErrorResponseException extends HKAPIBaseException {
    private static final long serialVersionUID = 1L;

    public HKAPIErrorResponseException(String message) {
        super(message);
    }
}
