package com.hk.api.resource.order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.hk.api.constants.HKAPITokenTypes;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.order.HKAPIOrderDTO;
import com.hk.api.pact.service.HKAPIOrderService;
import com.hk.api.security.annotation.SecureResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.domain.order.Order;
import com.hk.manager.StoreOrderService;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:26:51 PM
 */
@Path ("/order")
@Component
public class OrderResource {

    @Autowired
    private HKAPIOrderService hkapiOrderService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderStatusService orderStatusService;


    @POST
    @Path ("/create")
    @Consumes ("application/json")
    @Produces ("application/json")
    @SecureResource(hasAllTokens = {HKAPITokenTypes.APP_TOKEN})
    public HKAPIBaseDTO createOrderInHK(@HeaderParam(HKAPITokenTypes.APP_TOKEN) String appToken,HKAPIOrderDTO hkapiOrderDTO) {

        return getHkapiOrderService().createOrderInHK(appToken, hkapiOrderDTO);

    }

    @GET
    @Path ("{orderId}/track")
    @Consumes ("application/json")
    @Produces ("application/json")
    public String trackOrder(@PathParam ("orderId") String orderId) {
        return getHkapiOrderService().trackOrder(orderId);
    }

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createOrderInHKForStore(String orderInfo){
        Gson gson= JsonUtils.getGsonDefault();
        Order order=gson.fromJson(orderInfo, Order.class);
        if(order!=null){
            return hkapiOrderService.createOrderInHk(order);
        }else{
            return "invalid json";
        }
    }


    public HKAPIOrderService getHkapiOrderService() {
        return hkapiOrderService;
    }

    public void setHkapiOrderService(HKAPIOrderService hkapiOrderService) {
        this.hkapiOrderService = hkapiOrderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public StoreOrderService getStoreOrderService() {
        return storeOrderService;
    }

    public void setStoreOrderService(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }
}
