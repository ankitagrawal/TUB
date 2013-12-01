package com.hk.exception;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * User: kani Time: 6 Nov, 2009 2:12:03 PM
 */
@SuppressWarnings("serial")
public class HealthkartDefaultWebException extends HealthkartWebException {
    public HealthkartDefaultWebException(String message) {
        super(message);
    }

    public HealthkartDefaultWebException(Throwable e) {
        super(e);
    }

    public Long getErrorCode() {
        return 1L;
    }

    public String getErrorMessage() {
        return "Default error: possibly IO error";
    }

    public Resolution getResolution() {
        return new RedirectResolution("GeneralErrorAction.class");
    }

}
