package com.hk.security.exception;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HkInvalidApiKeyException extends HkAuthenticationException {

    private static final long serialVersionUID = 1L;
    private String            apiKey;

    public HkInvalidApiKeyException(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("no user registed with " + apiKey);
        return message.toString();
    }
}
