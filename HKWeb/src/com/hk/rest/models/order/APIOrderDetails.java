package com.hk.rest.models.order;

import org.codehaus.jackson.annotate.JsonProperty;

public class APIOrderDetails {

  private String orderId;
  
   @JsonProperty("details")
    private APIProductDetail[] apiProductDetails;


  public APIProductDetail[] getApiProductDetails() {
    return apiProductDetails;
  }


  public void setApiProductDetails(APIProductDetail[] apiProductDetails) {
    this.apiProductDetails = apiProductDetails;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderid) {
    this.orderId = orderid;
  }


}
