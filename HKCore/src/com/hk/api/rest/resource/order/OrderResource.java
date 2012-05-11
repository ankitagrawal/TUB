package com.hk.api.rest.resource.order;

import com.hk.api.rest.models.order.APIOrder;
import com.hk.api.rest.pact.service.APIOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;

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
