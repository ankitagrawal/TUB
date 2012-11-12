package com.hk.security;

import java.util.Collection;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HkUsernamePasswordAuthenticationToken extends HkAbstractAuthenticationToken{

    
    private final Object credentials;
    private final Object principal;
    private final Object apiKey;
    
    public HkUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object apiKey) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.apiKey = apiKey;
        setAuthenticated(false);
    }
    
    
    public HkUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object apiKey, Collection<GrantedOperation> operations) {
        super(operations);
        this.principal = principal;
        this.credentials = credentials;
        this.apiKey = apiKey;
        super.setAuthenticated(true); // must use super, as we override
    }


    //~ Methods ========================================================================================================

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }
    
    @Override
    public Object getApiKey() {
        return this.apiKey;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedOperation list instead");
        }

        super.setAuthenticated(false);
    }
    
}
