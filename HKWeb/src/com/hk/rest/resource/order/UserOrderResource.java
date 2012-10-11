package com.hk.rest.resource.order;

import com.hk.domain.core.JSONObject;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.UserOrderService;
import com.hk.pact.service.user.UserDetailService;
import com.hk.rest.models.order.APIOrder;
import com.hk.rest.pact.service.APIOrderService;
import com.hk.util.json.JSONResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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

    private static Logger logger = LoggerFactory.getLogger(UserOrderResource.class);
    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private UserDetailService userDetailService;

    class OrderDetail{
        public Long id;
        public String status;
        public String gatewayOrderId;
    }

    @GET
    @Path ("/phone/{phone}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam ("phone") long phone) {

        Response response = null;
        try{
            if (userDetailService.findByPhone((int)phone) == null){
                response = Response.status(Response.Status.NOT_FOUND).build();
            }else{
                List<Order> orders = userOrderService.getUserOrders((int)phone);
                final GenericEntity<List<OrderDetail>> entity = new GenericEntity<List<OrderDetail>>(getOrderDetailList(orders)) { };
                response = Response.status(Response.Status.OK).entity(entity).build();
            }

        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unabel to get User Orders ", ex);
        }
        return response;
    }

    @GET
    @Path ("/email/{email}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam ("email") String email) {

        Response response = null;
        try{
            List<Order> orders = userOrderService.getUserOrders(email);
            final GenericEntity<List<OrderDetail>> entity = new GenericEntity<List<OrderDetail>>(getOrderDetailList(orders)) { };
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private  List<OrderDetail> getOrderDetailList(List<Order> orders ){
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (Order order : orders){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.id = order.getId();
            orderDetail.status = order.getOrderStatus().getName();
            orderDetail.gatewayOrderId = order.getGatewayOrderId();
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
