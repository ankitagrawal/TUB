package com.hk.rest.resource;

import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.domain.user.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 12/8/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user/email")
@Component
public class UserEmailResource {

    private static Logger logger = LoggerFactory.getLogger(UserEmailResource.class);

    @Autowired
    AdminEmailService adminEmailService;

    @GET
    @Path("/store/{storeId}/category/{category}")
    @Produces("application/json")
    public Response getEmailsByCategory(@PathParam("category")String category, @PathParam("storeId")int storeId){

        Response response = null;
        try{
            List<String> emailRecepients = adminEmailService.getMailingListByCategory(category, storeId);
            final GenericEntity<List<String>> entity = new GenericEntity<List<String>>(emailRecepients){};
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }
}
