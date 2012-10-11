package com.hk.rest.resource.order;

import com.hk.domain.order.Order;
import com.hk.pact.service.order.UserOrderService;
import com.hk.rest.models.order.APIOrder;
import com.hk.rest.pact.service.APIOrderService;
import com.hk.util.json.JSONResponseBuilder;
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
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */

@Path("/userOrders")
@Component
public class UserOrderResource {

    @Autowired
    private UserOrderService userOrderService;

    @GET
    @Path ("/phone/{phone}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam ("phone") long phone) {

        Response response = null;
        try{
            List<Order> orders = userOrderService.getUserOrders((int)phone);
            final GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) { };
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GET
    @Path ("/email/{email}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam ("email") String email) {

        List<Order> orders = userOrderService.getUserOrders(email);

        final GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) { };
        return Response.status(Response.Status.OK).entity(entity).build();
    }
}
