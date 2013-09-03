package com.hk.api.edge.resource;

import java.security.InvalidParameterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.api.edge.pact.service.HKCatalogUserService;
import com.hk.api.edge.response.user.UserApiResponse;
import com.hk.util.json.JSONResponseBuilder;

@Component
@Path("/user/")
public class HKCatalogUserResource {

    @Autowired
    private HKCatalogUserService hkCatalogUserService;

  /*  @GET
    @Path("/loggedInUser")
    @Produces("application/json")
    public String getLoggedInUser() {
        UserApiBaseResponse userApiBaseResponse = getHkapiUserService().getLoggedInUser();
        if (userApiBaseResponse == null) {
            userApiBaseResponse = new UserApiBaseResponse();
            userApiBaseResponse.setException(true);
            userApiBaseResponse.setMessage("There came an Error, please try again");
        }
        return new JSONResponseBuilder().addField("userResponse", userApiBaseResponse).build();
    }*/

    @GET
    @Path("/name/{login}")
    @Produces("application/json")
    public String getUserByLogin(@PathParam("login")
    String login) {
        UserApiResponse userApiResponse = getHkCatalogUserService().getUserResponseByLogin(login);
        return new JSONResponseBuilder().addField("userResponse", userApiResponse).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public String getUserById(@PathParam("id")
    Long userId) {
        UserApiResponse userApiResponse = getHkCatalogUserService().getUserResponseById(userId);
        return new JSONResponseBuilder().addField("userResponse", userApiResponse).build();
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
            return new JSONResponseBuilder().addField("isTempUser", bool).addField("exception", ipe.getMessage()).build();
        }
        return new JSONResponseBuilder().addField("isTempUser", bool).addField("message", "UserExist").build();
    }

    public HKCatalogUserService getHkCatalogUserService() {
        return hkCatalogUserService;
    }

}
