package com.hk.rest.resource.order;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.rest.models.order.APIOrder;
import com.hk.rest.pact.service.APIOrderService;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:26:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Path ("/order")
public class OrderResource {

  @Autowired
  private APIOrderService apiOrderService;


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


  public APIOrderService getApiOrderService() {
    return apiOrderService;
  }

  public void setApiOrderService(APIOrderService apiOrderService) {
    this.apiOrderService = apiOrderService;
  }
}
