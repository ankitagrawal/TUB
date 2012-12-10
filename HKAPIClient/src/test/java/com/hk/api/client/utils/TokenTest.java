package com.hk.api.client.utils;

import com.hk.api.client.exception.HKInvalidTokenSignatureException;
import com.hk.api.client.exception.HKInvalidUserAccessTokenException;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/5/12
 * Time: 10:08 AM
 */
public class TokenTest {
    @Test
    public void uaTokenValidationTest(){
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken("test@test.com", "test", "secret");
        HKAPIAuthenticationUtils.isValidUserAccessToken(uaToken, "secret");
     }

    @Test(expectedExceptions = HKInvalidTokenSignatureException.class)
    public void invalidTokenSignatureTest(){
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken("test@test.com", "test", "secret");
        HKAPIAuthenticationUtils.isValidUserAccessToken(uaToken, "falsesecret");
    }

    @Test(expectedExceptions = {HKInvalidUserAccessTokenException.class, HKInvalidTokenSignatureException.class})
    public void invalidUserAccessTokenTest(){
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken("test@test.com", "test", "secret");
        uaToken=uaToken.concat(":"+"test");
        HKAPIAuthenticationUtils.isValidUserAccessToken(uaToken, "secret");
    }
}
