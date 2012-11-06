package com.hk.security.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.hk.security.GrantedOperation;
import com.hk.security.GrantedOperationUtil;
import com.hk.security.HkAuthService;
import com.hk.security.HkAuthentication;
import com.hk.security.HkUsernamePasswordAuthenticationToken;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.util.HKDateUtil;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HkAuthServiceImpl implements HkAuthService {

    @Override
    public HkAuthentication authenticate(HkAuthentication authentication) throws HkAuthenticationException {

        // TODO use some factory here to get authenticator
        if (authentication instanceof HkUsernamePasswordAuthenticationToken) {

            String userName = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();
            String appId = (String) authentication.getAppId();

            if (userName.equals("abc") && password.equals("abc") && appId.equals("abc")) {
                Collection<GrantedOperation> allowedOperations = GrantedOperationUtil.ALL_OPERATIONS;
                authentication = new HkUsernamePasswordAuthenticationToken(userName, password, allowedOperations);
            } else {
                // throw some authentication exception
            }
            return authentication;
        } else {
            // throw some authentication exception here.
        }

        return null;

    }

    @Override
    public String generateAuthToken(HkAuthentication authentication) {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        /**
         * base64( emailid:expiredon: b=token->md5(emailid+passwd+expiration) a :b
         */

        Date expiryTime = HKDateUtil.addToDate(new Date(), Calendar.HOUR_OF_DAY, DEFAULT_EXPIRY);

        // byte[] data = base64.encode();

        String tokenA = userName.concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());
        String tokenB = userName.concat(":").concat(password).concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());

        String md5 = DigestUtils.md5Hex( tokenB );
        String baseToken = tokenA.concat(md5);

        byte[] base64encoding = Base64.encodeBase64(baseToken.getBytes());
        System.out.println( base64encoding );
        
        return new String(base64encoding);
    }

    
    public static void main(String[] args) {
        
        String password = "abc";
        String userName = "abc";
        /**
         * base64( emailid:expiredon: b=token->md5(emailid+passwd+expiration) a :b
         */

        Date expiryTime = HKDateUtil.addToDate(new Date(), Calendar.HOUR_OF_DAY, DEFAULT_EXPIRY);

        // byte[] data = base64.encode();

        String tokenA = userName.concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());
        String tokenB = userName.concat(":").concat(password).concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());
        
        
        
        String md5 = DigestUtils.md5Hex( tokenB );
        String baseToken = tokenA.concat(md5);

        byte[] base64encoding = Base64.encodeBase64(baseToken.getBytes());
        System.out.println( new String(base64encoding) );

}
}
