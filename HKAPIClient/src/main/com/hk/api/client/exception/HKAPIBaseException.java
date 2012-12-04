package com.hk.api.client.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:22 PM
 */

public abstract class HKAPIBaseException extends RuntimeException {
    protected HKAPIBaseException() {
    }

    protected HKAPIBaseException(String message) {
        super(message);
    }

    public HKAPIBaseException(Throwable e) {
        super(e);
    }
}