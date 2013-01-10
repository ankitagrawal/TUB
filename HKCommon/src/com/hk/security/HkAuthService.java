package com.hk.security;

import com.hk.domain.api.HkApiUser;
import com.hk.domain.user.User;
import com.hk.security.exception.HKAuthTokenExpiredException;
import com.hk.security.exception.HkAuthenticationException;
import com.hk.security.exception.HkInvalidAuthTokenException;

/**
 * @author vaibhav.adlakha
 */
public interface HkAuthService {

    public static final int    DEFAULT_EXPIRY_MIN = 1;
    public static final String TOKEN_DELIMITER   = ":";

    public HkAuthentication authenticate(HkAuthentication authentication) throws HkAuthenticationException;

    public String generateAuthToken(String userName, String passwordCheckSum, String apiKey);

    public boolean validateToken(String authToken, String apiKey,boolean validatePwd)throws HkInvalidAuthTokenException, HKAuthTokenExpiredException ; 

    public String refershAuthToken(String authToken, String apiKey, String authScheme);

    /*
    appToken format=[base64(appkey:expirytime:md5(appkey:expirytime:appSecretKey))]
    used to validate self encoded apptokens during delegated login process
     */
    public boolean isValidAppToken(String appToken);

    public boolean isValidAppKey(String appKey);

    /*
    userAccessToken format=[base64(appkey:expirytime:user_email:md5(appkey:expirytime:user_email:appSecretKey))]
    used to validate self encoded apptokens meant for user detail access. We don't need token state management on each side if we use this
    */
    public boolean isValidUserAccessToken(String userAccessToken);

    public String generateUserAccessToken(String userEmail, String appKey);

    public User getUserFromAccessToken(String userAccessToken);

    public HkApiUser getApiUserFromAppToken(String appToken);

    public HkApiUser getApiUserFromUserAccessToken(String userAccessToken);

}
