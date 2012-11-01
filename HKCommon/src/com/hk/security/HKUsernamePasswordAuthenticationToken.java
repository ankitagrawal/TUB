package com.hk.security;

import java.util.Collection;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class HKUsernamePasswordAuthenticationToken extends HKAbstractAuthenticationToken{

    
    private final Object credentials;
    private final Object principal;
    private final Object appId;
    
    public HKUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object appId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.appId = appId;
        setAuthenticated(false);
    }
    
    
    public HKUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object appId, Collection<GrantedOperation> operations) {
        super(operations);
        this.principal = principal;
        this.credentials = credentials;
        this.appId = appId;
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
    public Object getAppId() {
        return this.appId;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedOperation list instead");
        }

        super.setAuthenticated(false);
    }


    


    
}
