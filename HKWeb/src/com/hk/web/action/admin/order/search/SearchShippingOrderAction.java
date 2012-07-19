package com.hk.web.action.admin.order.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.constants.courier.EnumAwbStatus;

@Component
public class SearchShippingOrderAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(SearchShippingOrderAction.class);                  

  private String shippingOrderGatewayId;
  private Long shippingOrderId;
  private String trackingId;
  private Courier courier=null;
  private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();

  @Autowired
  ShippingOrderService shippingOrderService;
    @Autowired
    AwbService awbService;

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
      //yet to verify  if either of  shippingid and gateweyid  is present search on ids.if only tracking id present then wet awb
      //multiple awb can be there. so we should return as a list.change shippingordersearchcriteria attribut awb to awb list.

      if (shippingOrderId != null || (!(StringUtils.isBlank(shippingOrderGatewayId)))) {
          shippingOrderSearchCriteria.setAwbList(null);
      } else {
          List<Awb> awbList = awbService.getAwbInShipment(null, trackingId, null, null, EnumAwbStatus.Attach.getAsAwbStatus());
          awbList.addAll(awbService.getAwbInShipment(null, trackingId, null, null, EnumAwbStatus.Used.getAsAwbStatus()));
          if (awbList == null || awbList.size() == 0) {
              addRedirectAlertMessage(new SimpleMessage("Wrong Tracking Id"));
          } else {
              shippingOrderSearchCriteria.setAwbList(awbList);
          }

      }

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

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
