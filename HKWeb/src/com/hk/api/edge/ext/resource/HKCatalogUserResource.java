package com.hk.api.edge.ext.resource;

import java.security.InvalidParameterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.ext.pact.service.HKCatalogUserService;
import com.hk.api.edge.ext.response.user.UserApiResponse;
import com.hk.util.json.JSONResponseBuilder;

@Component
@Path("/user/")
public class HKCatalogUserResource {

    @Autowired
    private HKCatalogUserService hkCatalogUserService;

    @GET
    @Path("/name/{login}")
    @Produces("application/json")
    public String getUserByLogin(@PathParam("login")
    String login) {
        UserApiResponse userApiResponse = getHkCatalogUserService().getUserResponseByLogin(login);
        return new JSONResponseBuilder().addField("results", userApiResponse).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public String getUserById(@PathParam("id")
    Long userId) {
        UserApiResponse userApiResponse = getHkCatalogUserService().getUserResponseById(userId);
        return new JSONResponseBuilder().addField("results", userApiResponse).build();
    }

    @GET
    @Path("/isTempUser/{id}")
    @Produces("application/json")
    public String isTempUser(@PathParam("id")
    Long userId) {
        boolean bool = false;
        try {
            bool = getHkCatalogUserService().isTempUser(userId);
        } catch (InvalidParameterException ipe) {
            return new JSONResponseBuilder().addField("isTempUser", bool).addField("msgs", ipe.getMessage()).build();
        }
        return new JSONResponseBuilder().addField("isTempUser", bool).addField("msgs", "UserExist").build();
    }

    public HKCatalogUserService getHkCatalogUserService() {
        return hkCatalogUserService;
    }

}
