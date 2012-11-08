package com.hk.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.hk.api.APIRegistry;
import com.hk.api.AuthAPI;

@Path("/secure/user")
@Component
public class UserResource {

    @GET
    @Path("/all")
    @Produces("application/json")
    public String test() {

        AuthAPI authAPI = APIRegistry.getAuthAPI();
        return new String("a" + ":" + "b");
    }
}
