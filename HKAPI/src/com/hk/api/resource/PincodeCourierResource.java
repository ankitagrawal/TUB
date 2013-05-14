package com.hk.api.resource;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
 * User: Pratham
 * Date: 13/05/13  Time: 15:33
*/
@Path("/pincode")
@Component
public class PincodeCourierResource {

    @Autowired
    private PincodeCourierService pincodeCourierService;

    @GET
    @Path ("/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    public String product(@PathParam("pin") String pin) {
        boolean isCodAllowed = getPincodeCourierService().isCodAllowed(pin);
        Gson gson = JsonUtils.getGsonDefault();
        return gson.toJson(isCodAllowed);
    }

    public PincodeCourierService getPincodeCourierService() {
        return pincodeCourierService;
    }

}
