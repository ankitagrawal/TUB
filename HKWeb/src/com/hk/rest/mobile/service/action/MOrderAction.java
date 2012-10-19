package com.hk.rest.mobile.service.action;

import com.hk.rest.models.order.APIOrder;
import com.hk.rest.pact.service.APIOrderService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import com.hk.pact.service.order.OrderService;
import com.hk.domain.order.Order;
import com.akube.framework.gson.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.Format;

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
    private APIOrderService apiOrderService;
    @Autowired
    OrderService orderService;


    @POST
    @Path("/create")
    @Consumes("application/json")
    @Produces("application/json")
    public String createOrderInHK(APIOrder apiOrder) {
        if (apiOrder != null) {
            String response = getApiOrderService().createOrderInHK(apiOrder);
            return response;
        } else {
            return "invalid json";
        }
    }

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
        Map statusMap = new HashMap<String,String>();
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
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, statusMap);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }


    public APIOrderService getApiOrderService() {
        return apiOrderService;
    }

    public void setApiOrderService(APIOrderService apiOrderService) {
        this.apiOrderService = apiOrderService;
    }
}