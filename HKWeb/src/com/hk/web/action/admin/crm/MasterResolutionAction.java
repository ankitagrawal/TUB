package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.analytics.EnumReasonType;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.taglibs.Functions;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 9/18/13
 * Time: 6:58 PM
 */
public class MasterResolutionAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
  }
/*  Long shippingOrderId;

  Integer actionType;

  Reason reason;

  String comments;

  Set<Long> actionSet;

  @Autowired
  ShippingOrderService shippingOrderService;

  List<ShippingOrder> shippingOrderList;

  @DefaultHandler
  public Resolution pre () {

    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId);
    shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);

    actionSet = new HashSet<Long>();
    actionSet.add(1l);actionSet.add(2l);actionSet.add(3l);actionSet.add(4l);actionSet.add(5l);

    return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
  }

  public Long getShippingOrderId() {
    return shippingOrderId;
  }

  public void setShippingOrderId(Long shippingOrderId) {
    this.shippingOrderId = shippingOrderId;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public Reason getReason() {
    return reason;
  }

  public void setReason(Reason reason) {
    this.reason = reason;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Set<Long> getActionSet() {
    return actionSet;
  }

  public void setActionSet(Set<Long> actionSet) {
    this.actionSet = actionSet;
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  public List<Reason> getRefundReasons() {
    return  Functions.getReasonsByType(EnumReasonType.REFUND.getName());
  }*/
}
