package com.hk.api.client.utils;

import com.hk.api.client.constants.AuthConstants;
import com.hk.api.client.exception.HKInvalidTokenSignatureException;
import com.hk.api.client.exception.HKInvalidUserAccessTokenException;
import com.hk.api.client.exception.HKTokenExpiredException;
import org.apache.commons.codec.binary.Base64;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:11 PM
 */
public class APIAuthenticationUtils {

    public static String generateUserAccessToken(String userEmail, String appKey, String appSecret){
        StringBuilder uaToken=new StringBuilder("");
        uaToken.append(appKey);
        uaToken.append(":"+getExpiryTimeStamp(AuthConstants.TOKEN_EXPIRY_TIME_MIN));
        uaToken.append(":"+userEmail);
        String md5Hash=HKAPIBaseUtils.md5Hash(uaToken.toString()+":"+appSecret,AuthConstants.md5Salt,AuthConstants.md5HashIterations);
        uaToken.append(":"+md5Hash);

        byte[] base64encoding = Base64.encodeBase64(uaToken.toString().getBytes());

        return new String(base64encoding);
    }

    public static String generateAppToken(String appKey, String appSecretKey){
        StringBuilder appToken=new StringBuilder("");
        appToken.append(appKey);
        appToken.append(":"+getExpiryTimeStamp(AuthConstants.TOKEN_EXPIRY_TIME_MIN));
        String md5Hash=HKAPIBaseUtils.md5Hash(appToken.toString()+":"+appSecretKey,AuthConstants.md5Salt,AuthConstants.md5HashIterations);
        appToken.append(":"+md5Hash);

        byte[] base64encoding = Base64.encodeBase64(appToken.toString().getBytes());

        return new String(base64encoding);
    }

    public static boolean isValidUserAccessToken(String userAccessToken){
        String[] decodedTokenArr=decodeToken(userAccessToken);

        if(decodedTokenArr.length!=4){
            throw new HKInvalidUserAccessTokenException(userAccessToken);
        }
        String apiKey= decodedTokenArr[0];
        String expiryTimeStamp = decodedTokenArr[1];
        String userEmail = decodedTokenArr[2];
        if(!HKAPIBaseUtils.md5Hash(apiKey+":"+expiryTimeStamp+":"+userEmail, AuthConstants.md5Salt,AuthConstants.md5HashIterations).equals(decodedTokenArr[2])){
            throw new HKInvalidTokenSignatureException(userAccessToken);
        }
        Date expiryDate=new Date(Long.parseLong(expiryTimeStamp));
        if(expiryDate.compareTo(new Date())==-1){
            throw new HKTokenExpiredException(userAccessToken);
        }

        return true;
    }

    private static String[] decodeToken(String token) {
        byte[] base64DecodedArr = Base64.decodeBase64(token);
        String base64Decoded = new String(base64DecodedArr);

        String[] decodedTokenArr = base64Decoded.split(":");
        return decodedTokenArr;
    }

    private static Long getExpiryTimeStamp(int min){
        long t=HKAPIBaseUtils.getCurrentTimestamp().getTime();
        long ms=min*60*1000 ;
        return (t+ms);
    }
}
