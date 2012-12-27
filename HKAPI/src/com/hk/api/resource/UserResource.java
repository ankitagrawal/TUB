package com.hk.api.resource;

import javax.ws.rs.*;

import com.hk.api.UserAPI;
import com.hk.api.constants.HKAPITokenTypes;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.user.HKAPIUserDTO;
import com.hk.api.pact.service.HKAPIUserService;
import com.hk.api.request.AddRewardPointRequest;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.util.json.JSONResponseBuilder;
import org.apache.commons.lang.StringUtils;
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
    private UserAPI userAPI;

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

 /*   @GET
    @Path("/{login}/details")
    @Produces("application/json")
    public String getUserDetails(@PathParam("login")
                                 String login) {
        UserDTO userDTO = getUserAPI().getUserDetails(login);
        return new JSONResponseBuilder().addField("login", login).addField("userDetails", userDTO).build();
    }*/

    @GET
    @Path("/{login}/rewardPoints")
    @Produces("application/json")
    public String getEligibleRewardPoints(@PathParam("login")
                                          String login) {
        Double eligibleRewardPoints = getUserAPI().getEligibleRewardPointsForUser(login);

        return new JSONResponseBuilder().addField("login", login).addField("rewardPoints", eligibleRewardPoints).build();
    }

    @POST
    @Path("/addRewardPoints")
    @Produces("application/json")
    public String addHKPlusRewardPointsForUser(AddRewardPointRequest addRewardPointRequest) {
        if (StringUtils.isBlank(addRewardPointRequest.getLogin())) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "Login cannot be blank").build();
        }
        if (StringUtils.isBlank(addRewardPointRequest.getApiKey())) {
            return new JSONResponseBuilder().addField("exception", true).addField("message", "api key cannot be blank").build();
        }

        String login = addRewardPointRequest.getLogin();
        //TODO: use api key to get reward point mode
        boolean rewardPointsAdded = getUserAPI().addRewardPointsForUser(login, addRewardPointRequest.getRewardPoints(), addRewardPointRequest.getComment(),
                EnumRewardPointMode.HKPLUS_POINTS);

        Double eligibleRewardPoints = getUserAPI().getEligibleRewardPointsForUser(login);

        return new JSONResponseBuilder().addField("login", login).addField("rewardPoints", eligibleRewardPoints).addField("rewardPointsAdded", rewardPointsAdded).build();
    }

    public UserAPI getUserAPI() {
        if (userAPI == null) {
            userAPI = APIRegistry.getUserAPI();
        }
        return userAPI;
    }

}
