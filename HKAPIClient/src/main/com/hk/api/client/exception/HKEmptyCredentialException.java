package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 7:44 PM
 */
public class HKEmptyCredentialException extends HKAPIBaseException {
    private static final long serialVersionUID = 1L;

    public HKEmptyCredentialException(String message) {
        super(message);
    }
}
