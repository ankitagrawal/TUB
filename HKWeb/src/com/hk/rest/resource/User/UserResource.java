package com.hk.rest.resource.User;

import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.service.UserService;
import com.hk.pact.service.user.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user")
@Component
public class UserResource {

    private static Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Autowired
    UserDetailService userDetailService;

    @Autowired
    UserService userService;

    @POST
    @Path ("email/{email}/phone/{phone}")
    @Produces("application/json")
    public Response updateUser(@PathParam("email") String email, @PathParam("phone") long phone) {
        email = email.toLowerCase().trim();
        User user = userService.findByLogin(email);
        Response response = null;
        try{
            if (user != null){
                response = Response.status(Response.Status.NOT_FOUND).build();
            }else{
                UserDetail userDetail = new UserDetail();
                userDetail.setPhone((int)phone);
                userDetail.setUser(user);
                userDetailService.save(userDetail);
                response = Response.status(Response.Status.OK).build();
            }
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to save User Details ", ex);
        }
        return response;
    }

    @GET
    @Path ("/priority/{priority}")
    @Produces("application/json")
    public Response getUserListByPriority(@PathParam ("priority") long priority) {

        Response response = null;
        try{
            List<UserDetail> userDetailList = userDetailService.getByPriority((int)priority);
            final GenericEntity<List<UserDetail>> entity = new GenericEntity<List<UserDetail>>(userDetailList) { };
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }
}
