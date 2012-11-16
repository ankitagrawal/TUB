package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.UserAPI;
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
        return getUserAPI().getUserDetails(login);
    }
    
    @GET
    @Path("/{login}/rewardPoints")
    @Produces("application/json")
    public String getEligibleRewardPoints(@PathParam("login")
    String login) {
        double  eligibleRewardPoints = getUserAPI().getEligibleRewardPointsForUser(login);
        
        return new JSONResponseBuilder().addField("login", login).addField("rewardPoints", eligibleRewardPoints).build();
    }
    
    @GET
    @Path("/addRewardPoints")
    @Produces("application/json")
    public void addHKPlusRewardPointsForUser(String login, double rewardPoints, String apiKey, String comment){
        boolean rewardPointsAdded = getUserAPI().addRewardPointsForUser(login, rewardPoints, comment, rewardPointMode);
        
        
        
        
    }
    

    public UserAPI getUserAPI() {
        if (userAPI == null) {
            userAPI = APIRegistry.getUserAPI();
        }
        return userAPI;
    }

}
