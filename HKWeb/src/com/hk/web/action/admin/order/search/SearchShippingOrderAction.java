package com.hk.web.action.admin.order.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.order.ShippingOrder;
import com.hk.search.ShippingOrderSearchCriteria;
import com.hk.service.shippingOrder.ShippingOrderService;

@Component
public class SearchShippingOrderAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(SearchShippingOrderAction.class);

  private String shippingOrderGatewayId;
  private Long shippingOrderId;
  private String trackingId;
  private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

  
  ShippingOrderService shippingOrderService;

  @ValidationMethod(on = "searchShippingOrder")
  public void validateSearch() {
    if (StringUtils.isBlank(shippingOrderGatewayId) && StringUtils.isBlank(trackingId) && shippingOrderId == null) {
      getContext().getValidationErrors().add("1", new SimpleError("Please Enter a Search Parameter"));
    }
  }

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/searchShippingOrder.jsp");
  }


  public Resolution searchShippingOrder() {

    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId).setGatewayOrderId(shippingOrderGatewayId);
    shippingOrderSearchCriteria.setTrackingId(trackingId);
    shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, false);

    return new ForwardResolution("/pages/admin/searchShippingOrder.jsp");

  }


  public String getShippingOrderGatewayId() {
    return shippingOrderGatewayId;
  }

  public void setShippingOrderGatewayId(String shippingOrderGatewayId) {
    this.shippingOrderGatewayId = shippingOrderGatewayId;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  public int getPageCount() {
    return 0;
  }

  public int getResultCount() {
    return 0;
  }


  public int getPerPageDefault() {
    return 20;
  }


  public Set<String> getParamSet() {
    Set<String> params = new HashSet<String>();
    params.add("orderStatus");

    return params;
  }

  public Long getShippingOrderId() {
    return shippingOrderId;
  }

  public void setShippingOrderId(Long shippingOrderId) {
    this.shippingOrderId = shippingOrderId;
  }
}
