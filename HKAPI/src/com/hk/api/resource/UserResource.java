package com.hk.api.resource;

import javax.ws.rs.*;

import com.hk.api.constants.HKAPITokenTypes;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.user.HKAPIUserDTO;
import com.hk.api.pact.service.HKAPIUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.AuthAPI;
import com.hk.api.security.annotation.SecureResource;

@Path("/user")
@Component
@SecureResource(hasAllTokens = {HKAPITokenTypes.USER_ACCESS_TOKEN})
public class UserResource {

    private static Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    HKAPIUserService hkapiUserService;

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
    public HKAPIBaseDTO getUserDetails(@HeaderParam(HKAPITokenTypes.USER_ACCESS_TOKEN) String userAccessToken){
        return hkapiUserService.getUserDetails(userAccessToken);
    }

    @POST
    @Path("/reward/{points}")
    @Produces("application/json")
    public HKAPIBaseDTO awardRewardPoints(@HeaderParam(HKAPITokenTypes.USER_ACCESS_TOKEN) String userAccessToken,
                                         @PathParam("points") Double rewardPoints){
        return hkapiUserService.awardRewardPoints(userAccessToken,rewardPoints);
    }

    @GET
    @Path("/rewardpoints")
    @Produces("application/json")
    public HKAPIBaseDTO getUserRewardPoints(@HeaderParam(HKAPITokenTypes.USER_ACCESS_TOKEN) String userAccessToken){
        return hkapiUserService.getUserRewardPointDetails(userAccessToken);
    }

    @POST
    @Path("/sso/create")
    @Produces("application/json")
    @SecureResource(hasAllTokens = {HKAPITokenTypes.APP_TOKEN})
    public HKAPIBaseDTO createUserInHK(HKAPIUserDTO userDetailDto){
       return hkapiUserService.createSSOUser(userDetailDto);
    }

}
