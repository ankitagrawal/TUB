package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.UserAPI;
import com.hk.api.dto.user.UserDTO;
import com.hk.api.request.AddRewardPointRequest;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.util.json.JSONResponseBuilder;

@Path("/user")
@Component
// @SecureResource
public class UserResource {

    private UserAPI userAPI;

    @GET
    @Path("/{login}/details")
    @Produces("application/json")
    public String getUserDetails(@PathParam("login")
    String login) {
        UserDTO userDTO = getUserAPI().getUserDetails(login);
        return new JSONResponseBuilder().addField("login", login).addField("userDetails", userDTO).build();
    }

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
        String login = addRewardPointRequest.getLogin();
        boolean rewardPointsAdded = getUserAPI().addRewardPointsForUser(login, addRewardPointRequest.getRewardPoints(), addRewardPointRequest.getComment(), EnumRewardPointMode.HKPLUS_POINTS);

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
