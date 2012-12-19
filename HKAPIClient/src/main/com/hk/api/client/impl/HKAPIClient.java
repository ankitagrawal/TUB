package com.hk.api.client.impl;

import com.hk.api.client.constants.HKAPITokenTypes;
import com.hk.api.client.dto.HKAPIBaseDto;
import com.hk.api.client.dto.HKUserDetailDto;
import com.hk.api.client.dto.order.HKAPIOrderDTO;
import com.hk.api.client.exception.HKEmptyCredentialException;
import com.hk.api.client.pact.IHKAPIClient;
import com.hk.api.client.utils.HKAPIAuthenticationUtils;
import com.hk.api.client.utils.HKAPIBaseUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 1:59 PM
 */
public class HKAPIClient extends RestClient implements IHKAPIClient {

    private String appKey="";
    private String appSecretKey="";
    private String healthkartRestUrl="";

    public HKAPIClient(String appKey, String appSecretKey, String healthkartRestUrl) {
        this.appKey = appKey;
        this.appSecretKey = appSecretKey;
        this.healthkartRestUrl = healthkartRestUrl;
    }

    public HKUserDetailDto getUserDetails(String userEmail){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(userEmail, appKey, appSecretKey);
        String hkResponse=doGet("/user/details", getUserAccessTokenHeaders(userEmail), healthkartRestUrl);
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto;
    }

    public HKUserDetailDto createUserInHK(HKUserDetailDto userDetails){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(userDetails.getEmail(), appKey, appSecretKey);
        String hkResponse=doPost("/user/sso/create",HKAPIBaseUtils.toJSON(userDetails), getAppTokenHeaders(), healthkartRestUrl);
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto;
    }

    public Double getRewardPoints(String userEmail){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(userEmail, appKey, appSecretKey);
        String hkResponse=doGet("/user/rewardpoints", getUserAccessTokenHeaders(userEmail), healthkartRestUrl);
        HKUserDetailDto userDetailDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(userDetailDto);
        return userDetailDto.getRewardPoints();
    }

    public HKAPIBaseDto awardRewardPoints(String userEmail, Double rewardPoints){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(userEmail, appKey, appSecretKey);
        String url="/user/reward/"+rewardPoints.toString();
        String hkResponse=doPost(url, null, getUserAccessTokenHeaders(userEmail), healthkartRestUrl);
        HKAPIBaseDto baseDto= HKAPIBaseUtils.fromJSON(HKAPIBaseDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(baseDto);
        return baseDto;
    }

    public HKAPIBaseDto placeOrderInHK(HKAPIOrderDTO hkapiOrderDTO){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(hkapiOrderDTO.getHkapiUserDTO().getEmail(), appKey, appSecretKey);
        String hkResponse=doPost("/order/create",HKAPIBaseUtils.toJSON(hkapiOrderDTO),getAppTokenHeaders(), healthkartRestUrl);
        HKAPIBaseDto responseDto= HKAPIBaseUtils.fromJSON(HKUserDetailDto.class, hkResponse);
        HKAPIBaseUtils.checkForErrors(responseDto);
        return responseDto;
    }

    private void checkForEmptyCredentials(){
        if(HKAPIBaseUtils.isEmpty(appKey)){
            throw new HKEmptyCredentialException("HK app key is empty - please set it");
        }
        if(HKAPIBaseUtils.isEmpty(appSecretKey)){
            throw new HKEmptyCredentialException("HK appSecret Key is empty - please set it");
        }
    }

    private Header[] getUserAccessTokenHeaders(String userEmail){
        checkForEmptyCredentials();
        String uaToken= HKAPIAuthenticationUtils.generateUserAccessToken(userEmail, appKey, appSecretKey);
        BasicHeader header=new BasicHeader(HKAPITokenTypes.USER_ACCESS_TOKEN,uaToken);
        BasicHeader[] headers={header};
        return headers;
    }

    private Header[] getAppTokenHeaders(){
        checkForEmptyCredentials();
        String appToken= HKAPIAuthenticationUtils.generateAppToken(appKey, appSecretKey);
        BasicHeader header=new BasicHeader(HKAPITokenTypes.APP_TOKEN,appToken);
        BasicHeader[] headers={header};
        return headers;
    }

    private Header[] getAppAndUserAccessTokenHeaders(String userEmail){
        BasicHeader userAccessHeader=(BasicHeader)getUserAccessTokenHeaders(userEmail)[0];
        BasicHeader appTokenHeader=(BasicHeader)getAppTokenHeaders()[0];
        BasicHeader[] combinedHeaders={userAccessHeader,appTokenHeader};
        return combinedHeaders;
    }

    public  String getAppKey() {
        return appKey;
    }

    public  void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public  String getAppSecretKey() {
        return appSecretKey;
    }

    public  void setAppSecretKey(String appSecretKey) {
        this.appSecretKey = appSecretKey;
    }

}
