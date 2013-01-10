package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.AuthAPI;

@Path("/auth")
@Component
public class AuthResource {

    private AuthAPI authAPI;

    @GET
    @Path("/{apiKey}/refreshToken/")
    @Produces("application/json")
    public String refreshToken(@QueryParam("authToken")
    String authToken, @PathParam("apiKey")
    String apiKey, @QueryParam("authScheme")
    String authScheme) {

        return getAuthAPI().validateAndRefreshAuthToken(authToken, apiKey, authScheme);

    }

    public AuthAPI getAuthAPI() {
        if(authAPI == null){
            authAPI = APIRegistry.getAuthAPI();
        }
        return authAPI;
    }

}
