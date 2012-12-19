package com.hk.api.resource;

import javax.ws.rs.*;

import com.hk.api.constants.APITokenTypes;
import com.hk.api.dto.HkAPIBaseDto;
import com.hk.api.dto.UserDetailDto;
import com.hk.api.pact.service.APIUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.AuthAPI;
import com.hk.api.security.annotation.SecureResource;

@Path("/user")
@Component
@SecureResource(hasAllTokens = {APITokenTypes.USER_ACCESS_TOKEN})
public class UserResource {

    private static Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    APIUserService apiUserService;

    @GET
    @Path("/all")
    @Produces("application/json")
    @SecureResource(hasAllTokens = {})
    public String test() {

        AuthAPI authAPI = APIRegistry.getAuthAPI();
        return new String("a" + ":" + "b");
    }

    @GET
    @Path("/details")
    @Produces("application/json")
    public HkAPIBaseDto getUserDetails(@HeaderParam(APITokenTypes.USER_ACCESS_TOKEN) String userAccessToken){
        return apiUserService.getUserDetails(userAccessToken);
    }

    @POST
    @Path("/reward/{points}")
    @Produces("application/json")
    public HkAPIBaseDto awardRewardPoints(@HeaderParam(APITokenTypes.USER_ACCESS_TOKEN) String userAccessToken,
                                         @PathParam("points") Double rewardPoints){
        return apiUserService.awardRewardPoints(userAccessToken,rewardPoints);
    }

    @GET
    @Path("/rewardpoints")
    @Produces("application/json")
    public HkAPIBaseDto getUserRewardPoints(@HeaderParam(APITokenTypes.USER_ACCESS_TOKEN) String userAccessToken){
        return apiUserService.getUserRewardPointDetails(userAccessToken);
    }

    @POST
    @Path("/sso/create")
    @Produces("application/json")
    @SecureResource(hasAllTokens = {APITokenTypes.APP_TOKEN})
    public HkAPIBaseDto createUserInHK(UserDetailDto userDetailDto){
       return apiUserService.createSSOUser(userDetailDto);
    }

}
