package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.AuthAPI;
import com.hk.api.UserAPI;
import com.hk.security.annotation.SecureResource;

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

    public UserAPI getUserAPI() {
        if (userAPI == null) {
            userAPI = APIRegistry.getUserAPI();
        }
        return userAPI;
    }

}
