package com.hk.rest.resource.order;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.store.StoreOrder;
import com.hk.manager.StoreOrderService;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.rest.models.order.APIOrder;
import com.hk.rest.pact.service.APIOrderService;

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
    private APIOrderService apiOrderService;

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
    public String createOrderInHK(APIOrder apiOrder) {
        if (apiOrder != null) {
            String response = getApiOrderService().createOrderInHK(apiOrder);
            return response;
        } else {
            return "invalid json";
        }
    }

    @GET
    @Path ("{orderId}/track")
    @Consumes ("application/json")
    @Produces ("application/json")
    public String trackOrder(@PathParam ("orderId") String orderId) {
        return getApiOrderService().trackOrder(orderId);
    }

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createOrderInHKForStore(String orderInfo){
        Gson gson= JsonUtils.getGsonDefault();
        Order order=gson.fromJson(orderInfo, Order.class);
        if(order!=null){
            return apiOrderService.createOrderInHk(order);
        }else{
            return "invalid json";
        }
    }

    @GET
    @Path("/script/place/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String placeOrderInHK(@PathParam ("orderId") String orderId){
        Order order=orderService.find(new Long(orderId));
        if(order!=null){
            if(!order.getStore().getId().equals(storeService.getDefaultStore().getId())){
                if(order.getStoreOrder()==null){
                    order=storeOrderService.placeOrderInHK(order);
                    return "order placed in hk with id: "+order.getStoreOrder().getHkOrderId() ;
                } else
                {
                    return "invalid";
                }
            }else {
                return "invalid";
            }
        }else{
            return "invalid";
        }
    }

    @GET
    @Path("/update/{hkOrderId}/status/{orderStatusId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateOrderInStore(@PathParam ("hkOrderId") String hkOrderId,@PathParam ("orderStatusId") String orderStatusId){
          StoreOrder storeOrder=storeService.getStoreOrderByHkOrderId(new Long(hkOrderId.trim()));
          if(storeOrder!=null){
              Order storeBaseOrder=storeOrder.getBaseOrder();
              OrderStatus orderStatusToUpdate=   orderStatusService.find(new Long(orderStatusId.trim()));
              if(orderStatusToUpdate!=null){
                 storeBaseOrder.setOrderStatus(orderStatusToUpdate);
                 storeBaseOrder=orderService.save(storeBaseOrder);
                 return "order status updated";
              }else{
                  return "invalid";
              }
          }else{
              return "invalid";
          }
    }


    public APIOrderService getApiOrderService() {
        return apiOrderService;
    }

    public void setApiOrderService(APIOrderService apiOrderService) {
        this.apiOrderService = apiOrderService;
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
