package com.hk.security;

import java.util.Collection;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HKAuthentication {
    
    
    public Collection<GrantedOperation> getAuthorities();
    
    
    public Object getCredentials();
    
    
    boolean isAuthenticated();
    
    
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

}
