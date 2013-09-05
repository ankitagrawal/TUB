package com.hk.api.edge.integration.resource.user;

import java.security.InvalidParameterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.integration.pact.service.user.HybridUserService;
import com.hk.api.edge.integration.response.user.UserResponseFromHKR;
import com.hk.util.json.JSONResponseBuilder;

@Component
@Path("/user/")
public class HybridUserResource {

    @Autowired
    private HybridUserService hybridUserService;

    @GET
    @Path("/name/{login}")
    @Produces("application/json")
    public String getHkrUserByLogin(@PathParam("login")
    String login) {
        UserResponseFromHKR userApiResponse = getHybridUserService().getUserResponseByLogin(login);
        return new JSONResponseBuilder().addField("results", userApiResponse).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public String getHkrUserById(@PathParam("id")
    Long userId) {
        UserResponseFromHKR userApiResponse = getHybridUserService().getUserResponseById(userId);
        return new JSONResponseBuilder().addField("results", userApiResponse).build();
    }

    @GET
    @Path("/isTempUser/{id}")
    @Produces("application/json")
    public String isTempUser(@PathParam("id")
    Long userId) {
        boolean bool = false;
        try {
            bool = getHybridUserService().isTempUser(userId);
        } catch (InvalidParameterException ipe) {
            return new JSONResponseBuilder().addField("isTempUser", bool).addField("msgs", ipe.getMessage()).build();
        }
        return new JSONResponseBuilder().addField("isTempUser", bool).addField("msgs", "UserExist").build();
    }

    public HybridUserService getHybridUserService() {
        return hybridUserService;
    }

}
