package com.hk.security.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.hk.security.GrantedOperation;
import com.hk.security.GrantedOperationUtil;
import com.hk.security.HKAuthService;
import com.hk.security.HKAuthentication;
import com.hk.security.HKUsernamePasswordAuthenticationToken;
import com.hk.security.exception.HKAuthenticationException;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HKAuthServiceImpl implements HKAuthService {

    @Override
    public HKAuthentication authenticate(HKAuthentication authentication) throws HKAuthenticationException {

        // TODO use some factory here to get authenticator
        if (authentication instanceof HKUsernamePasswordAuthenticationToken) {
                
            String userName = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();
            String appId = (String) authentication.getAppId();
            
            if(userName.equals("abc") && password.equals("abc") && appId.equals("abc")){
                Collection<GrantedOperation> allowedOperations = GrantedOperationUtil.ALL_OPERATIONS;
                authentication = new HKUsernamePasswordAuthenticationToken(userName, password, allowedOperations);
            }
        }

        return authentication;
    }

}
