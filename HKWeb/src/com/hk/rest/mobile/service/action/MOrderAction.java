package com.hk.rest.mobile.service.action;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.OrderService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 18, 2012
 * Time: 3:06:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mOrder")
@Component
public class MOrderAction extends MBaseAction{
    @Autowired
    OrderService orderService;

    @GET
    @Path("/{orderId}/track/")
    @Produces("application/json")
    public String trackOrder(@PathParam("orderId") String orderId,
                             @Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        String orderStatus=MHKConstants.NO_RESULTS;
        Map<String,Object> statusMap = new HashMap<String,Object>();
        try{

        //orderStatus =  getApiOrderService().trackOrder(orderId);
            Order order  = orderService.findByGatewayOrderId(orderId);
            orderStatus = order.getOrderStatus().getName();
            Format orderDate = new SimpleDateFormat("dd/MM/yy");
            
            statusMap.put("orderstatus",orderStatus);
            statusMap.put("orderId",order.getOrderStatus().getId());
            statusMap.put("name",order.getAddress().getName());
            statusMap.put("date",orderDate.format(order.getCreateDate()));
            
        }catch(Exception e){
            message = MHKConstants.NO_ORDER_EXIST;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, statusMap);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }

}