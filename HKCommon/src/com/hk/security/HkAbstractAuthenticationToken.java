package com.hk.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public abstract class HkAbstractAuthenticationToken implements HkAuthentication {

    
    private final Collection<GrantedOperation> operations;
    private boolean                            authenticated = false;

    public HkAbstractAuthenticationToken(Collection<GrantedOperation> operations) {
        if (operations == null) {
            this.operations = GrantedOperationUtil.NO_OPERATIONS;
            return;
        }

        for (GrantedOperation o : operations) {
            if (o == null) {
                throw new IllegalArgumentException("Operations collection cannot contain any null elements");
            }
        }
        ArrayList<GrantedOperation> temp = new ArrayList<GrantedOperation>(operations.size());
        temp.addAll(operations);
        this.operations = Collections.unmodifiableList(temp);
    }

    // ~ Methods
    // ========================================================================================================

    public boolean equals(Object obj) {
        if (!(obj instanceof HkAbstractAuthenticationToken)) {
            return false;
        }

        HkAbstractAuthenticationToken test = (HkAbstractAuthenticationToken) obj;

        if (!operations.equals(test.operations)) {
            return false;
        }

        if ((this.getCredentials() == null) && (test.getCredentials() != null)) {
            return false;
        }

        if ((this.getCredentials() != null) && !this.getCredentials().equals(test.getCredentials())) {
            return false;
        }

        if (this.getPrincipal() == null && test.getPrincipal() != null) {
            return false;
        }

        if (this.getPrincipal() != null && !this.getPrincipal().equals(test.getPrincipal())) {
            return false;
        }
        
        if(this.getApiKey() !=null && test.getApiKey() == null){
            return false;
        }
        
        if(this.getApiKey() == null && test.getApiKey() !=null){
            return false;
        }
        
        if(this.getApiKey() !=null && !this.getApiKey().equals(test.getApiKey())){
            return false;
        }

        return this.isAuthenticated() == test.isAuthenticated();
    }

    public Collection<GrantedOperation> getOperations() {
        return operations;
    }

    public int hashCode() {
        int code = 31;

        for (GrantedOperation authority : operations) {
            code ^= authority.hashCode();
        }

        if (this.getPrincipal() != null) {
            code ^= this.getPrincipal().hashCode();
        }

        if (this.getCredentials() != null) {
            code ^= this.getCredentials().hashCode();
        }

        if (this.isAuthenticated()) {
            code ^= -37;
        }

        return code;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Principal: ").append(this.getPrincipal()).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Authenticated: ").append(this.isAuthenticated()).append("; ");

        if (!operations.isEmpty()) {
            sb.append("Granted Authorities: ");

            int i = 0;
            for (GrantedOperation operation : operations) {
                if (i++ > 0) {
                    sb.append(", ");
                }

                sb.append(operation);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
    
    
}
