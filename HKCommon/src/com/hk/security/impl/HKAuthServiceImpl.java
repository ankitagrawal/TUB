package com.hk.security.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.hk.security.GrantedOperation;
import com.hk.security.GrantedOperationUtil;
import com.hk.security.HkAuthService;
import com.hk.security.HkAuthentication;
import com.hk.security.HkUsernamePasswordAuthenticationToken;
import com.hk.security.exception.HKAuthTokenExpiredException;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.security.exception.HkInvalidAuthTokenException;
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
            String apiKey = (String) authentication.getApiKey();

            if (userName.equals("abc") && password.equals("abc") && apiKey.equals("abc")) {
                Collection<GrantedOperation> allowedOperations = GrantedOperationUtil.ALL_OPERATIONS;
                authentication = new HkUsernamePasswordAuthenticationToken(userName, password, allowedOperations);
            } else {
                throw new HkAuthenticationException((String) authentication.getCredentials());
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
        String apiKey = (String) authentication.getApiKey();
        /**
         * base64( emailid:expiredon:apiKey b=token->md5(emailid+passwd+expiration) a :b
         */

        Date expiryTime = HKDateUtil.addToDate(new Date(), Calendar.HOUR_OF_DAY, DEFAULT_EXPIRY_MIN);

        // byte[] data = base64.encode();

        String tokenA = userName.concat(":").concat(Long.valueOf(expiryTime.getTime()).toString()).concat(":").concat(apiKey);
        String tokenB = userName.concat(":").concat(password).concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());

        String md5 = DigestUtils.md5Hex(tokenB);
        String baseToken = tokenA.concat(":").concat(md5);

        byte[] base64encoding = Base64.encodeBase64(baseToken.getBytes());
        

        return new String(base64encoding);
    }

    @Override
    public boolean validateToken(String authToken, String apiKey, boolean validatePwd) {
        boolean valid = true;
        String[] decodedTokenArr = decodeAuthToken(authToken);

        Date expiredOn = new Date(Long.parseLong(decodedTokenArr[1]));
        System.out.println(expiredOn);

        if (expiredOn.compareTo(new Date()) == -1) {
            throw new HKAuthTokenExpiredException();
        }

        String apiKeyInToken = decodedTokenArr[2];

        if (!apiKeyInToken.equals(apiKey)) {
            throw new HkInvalidAuthTokenException();
        }

        if (validatePwd) {

            String password = "abc";

            String reConstructedToken = decodedTokenArr[0].concat(":").concat(password).concat(":").concat(decodedTokenArr[1]);
            String reConTokeMd5 = DigestUtils.md5Hex(reConstructedToken);

            if (!reConTokeMd5.equals(decodedTokenArr[3])) {
                throw new HkInvalidAuthTokenException();
            }
        }

        return valid;

    }

    @Override
    public String refershAuthToken(String authToken, String apiKey, String authScheme) {
        //TODO: handle auth scheme here
        return refershUserNamePasswordAuthToken(authToken, apiKey);
    }

    private String refershUserNamePasswordAuthToken(String authToken, String apiKey) {
        String[] decodedTokenArr = decodeAuthToken(authToken);
        String password = "abc";
        HkUsernamePasswordAuthenticationToken authentication = new HkUsernamePasswordAuthenticationToken(decodedTokenArr[0], password, apiKey);
        return generateAuthToken(authentication);
    }

    private String[] decodeAuthToken(String authToken) {
        byte[] base64DecodedArr = Base64.decodeBase64(authToken);
        String base64Decoded = new String(base64DecodedArr);
        System.out.println(base64Decoded);

        String[] decodedTokenArr = base64Decoded.split(":");
        return decodedTokenArr;
    }

    public static void main(String[] args) {

        String password = "abc";
        String userName = "uname";
        String apiKey = "appId";
        
        HkUsernamePasswordAuthenticationToken authentication = new HkUsernamePasswordAuthenticationToken(userName,password , apiKey);
        HkAuthServiceImpl authService = new HkAuthServiceImpl();
        String authToken = authService.generateAuthToken(authentication);
        
        System.out.println(authToken);
        
        System.out.println(authService.validateToken(authToken, apiKey, true));
        
        System.out.println(authService.validateToken(authToken, apiKey, false));
        /**
         * base64( emailid:expiredon: b=token->md5(emailid+passwd+expiration) a :b
         */

        /*Date expiryTime = HKDateUtil.addToDate(new Date(), Calendar.MINUTE, DEFAULT_EXPIRY_MIN);
        System.out.println(expiryTime);
        // byte[] data = base64.encode();

        String tokenA = userName.concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());
        String tokenB = userName.concat(":").concat(password).concat(":").concat(Long.valueOf(expiryTime.getTime()).toString());

        String md5 = DigestUtils.md5Hex(tokenB);
        String baseToken = tokenA.concat(":").concat(md5);

        byte[] base64encoding = Base64.encodeBase64(baseToken.getBytes());
        String authToken = new String(base64encoding);
        System.out.println(authToken);

        byte[] base64DecodedArr = Base64.decodeBase64(authToken);
        String base64Decoded = new String(base64DecodedArr);
        System.out.println(base64Decoded);

        String[] decodedTokenArr = base64Decoded.split(":");
        Date expiredOn = new Date(Long.parseLong(decodedTokenArr[1]));
        System.out.println(expiredOn);

        if (expiredOn.compareTo(new Date()) == -1) {
            System.out.println("Expired");
        }

        String reConstructedToken = decodedTokenArr[0].concat(":").concat(password).concat(":").concat(decodedTokenArr[1]);
        String reConTokeMd5 = DigestUtils.md5Hex(reConstructedToken);

        if (reConTokeMd5.equals(decodedTokenArr[2])) {
            System.out.println("equal");
        }*/

    }
}
