package com.hk.api.client.impl;

import com.hk.api.client.constants.HKAPITokenTypes;
import com.hk.api.client.dto.HKAPIBaseDto;
import com.hk.api.client.dto.HKUserDetailDto;
import com.hk.api.client.exception.HKEmptyCredentialException;
import com.hk.api.client.pact.IHKAPIClient;
import com.hk.api.client.utils.APIAuthenticationUtils;
import com.hk.api.client.utils.HKAPIBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 1:59 PM
 */
public class HKAPIClient extends RestClient implements IHKAPIClient {

    private static String appKey="";
    private static String appSecretKey="";

    public HKUserDetailDto getUserDetails(String userEmail){
        checkForEmptyCredentials();
        String uaToken= APIAuthenticationUtils.generateUserAccessToken(userEmail, appKey, appSecretKey);
        String hkResponse=doGet("/user/details", getUserAccessTokenHeaders(userEmail));
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto;
    }

    public HKUserDetailDto createUserInHK(HKUserDetailDto userDetails){
        checkForEmptyCredentials();
        String uaToken= APIAuthenticationUtils.generateUserAccessToken(userDetails.getEmail(),appKey,appSecretKey);
        String hkResponse=doPost("/user/sso/create",HKAPIBaseUtils.toJSON(userDetails), getAppTokenHeaders());
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto;
    }

    public Double getRewardPoints(String userEmail){
        checkForEmptyCredentials();
        String uaToken= APIAuthenticationUtils.generateUserAccessToken(userEmail,appKey,appSecretKey);
        String hkResponse=doGet("/user/rewardpoints", getUserAccessTokenHeaders(userEmail));
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto.getRewardPoints();
    }

    public HKAPIBaseDto awardRewardPoints(String userEmail, Double rewardPoints){
        checkForEmptyCredentials();
        String uaToken= APIAuthenticationUtils.generateUserAccessToken(userEmail,appKey,appSecretKey);
        String url="/user/reward/"+rewardPoints.toString();
        String hkResponse=doPost(url, null, getUserAccessTokenHeaders(userEmail));
        HKAPIBaseDto baseDto= HKAPIBaseUtils.fromJSON(HKAPIBaseDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(baseDto);
        return baseDto;
    }

    private static void checkForEmptyCredentials(){
        if(StringUtils.isEmpty(appKey)){
            throw new HKEmptyCredentialException("HK app key is empty - please set it");
        }
        if(StringUtils.isEmpty(appSecretKey)){
            throw new HKEmptyCredentialException("HK appSecret Key is empty - please set it");
        }
    }

    private static Header[] getUserAccessTokenHeaders(String userEmail){
        checkForEmptyCredentials();
        String uaToken=APIAuthenticationUtils.generateUserAccessToken(userEmail,appKey,appSecretKey);
        BasicHeader header=new BasicHeader(HKAPITokenTypes.USER_ACCESS_TOKEN,uaToken);
        BasicHeader[] headers={header};
        return headers;
    }

    private static Header[] getAppTokenHeaders(){
        checkForEmptyCredentials();
        String appToken=APIAuthenticationUtils.generateAppToken(appKey,appSecretKey);
        BasicHeader header=new BasicHeader(HKAPITokenTypes.APP_TOKEN,appToken);
        BasicHeader[] headers={header};
        return headers;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        HKAPIClient.appKey = appKey;
    }

    public static String getAppSecretKey() {
        return appSecretKey;
    }

    public static void setAppSecretKey(String appSecretKey) {
        HKAPIClient.appSecretKey = appSecretKey;
    }

}
