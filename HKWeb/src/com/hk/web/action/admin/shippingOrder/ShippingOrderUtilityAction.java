package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure(hasAnyRoles = {RoleConstants.GOD}, authActionBean = AdminPermissionAction.class)
public class ShippingOrderUtilityAction extends BasePaginatedAction {

  private static Logger logger = LoggerFactory.getLogger(ShippingOrderUtilityAction.class);

  @Autowired
  ShippingOrderService shippingOrderService;

  private String gatewayOrderIds;
  private String shippingOrderIds;
  private Date startDate;
  private Date endDate;
  private List<ShippingOrder> shippingOrders;
  private List<ShippingOrder> shippingOrderMarked;

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  private List<ShippingOrder> shippingOrderList;
  private Integer defaultPerPage = 1000;
  private Page shippingOrderPage;
  private List<ShippingOrderStatus> shippingOrderStatusList;
  private List<ShippingOrderStatus> shippingOrderStatus;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/shippingOrder/soValidationAndOtherUtility.jsp");
  }

  public Resolution searchSO() {
    shippingOrders = new ArrayList<ShippingOrder>();
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderStatus = new ArrayList<ShippingOrderStatus>();
    if (shippingOrderStatusList != null && shippingOrderStatusList.size() > 0 && !shippingOrderStatusList.isEmpty()) {
      for (ShippingOrderStatus status : shippingOrderStatusList) {
        if (status != null) {
          shippingOrderStatus.add(status);
        }
      }
    } else {
      shippingOrderStatus.addAll(Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus(),
          EnumShippingOrderStatus.SO_Ready_For_Validation.asShippingOrderStatus(),
          EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus(),
          EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus(),
          EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus(),
          EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus()));
    }
    if (startDate != null && endDate != null) {
      shippingOrderSearchCriteria.setPaymentStartDate(startDate).setPaymentEndDate(endDate);
    }
    if (StringUtils.isNotBlank(shippingOrderIds)) {
      String[] orderArray = shippingOrderIds.split(",");
      for (String soId : orderArray) {
        ShippingOrder shippingOrder = shippingOrderService.find(Long.valueOf(StringUtils.deleteWhitespace(soId)));
        if (shippingOrder != null) {
          shippingOrders.add(shippingOrder);
        }
      }
    } else {
      shippingOrderSearchCriteria.setSortByDispatchDate(false);
      shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatus);
      shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
      List<ShippingOrder> shippingOrdersList = shippingOrderPage.getList();
      for (ShippingOrder shippingOrder : shippingOrdersList) {
        shippingOrders.add(shippingOrder);
      }
    }
    return new ForwardResolution("/pages/admin/shippingOrder/soValidationAndOtherUtility.jsp");
  }

  @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
  public Resolution validate() {
    if (shippingOrderList != null && !shippingOrderList.isEmpty()) {
      for (ShippingOrder shippingOrder : shippingOrderList) {
        shippingOrderService.validateShippingOrderAB(shippingOrder);
      }
    } else {
      addRedirectAlertMessage(new SimpleMessage("Please select a SO for validation"));
    }
    addRedirectAlertMessage(new SimpleMessage("Selected Shipping Orders have been validated"));
    return new RedirectResolution(ShippingOrderUtilityAction.class);
  }

  public String getGatewayOrderIds() {
    return gatewayOrderIds;
  }

  public void setGatewayOrderIds(String gatewayOrderIds) {
    this.gatewayOrderIds = gatewayOrderIds;
  }

  public String getShippingOrderIds() {
    return shippingOrderIds;
  }

  public void setShippingOrderIds(String shippingOrderIds) {
    this.shippingOrderIds = shippingOrderIds;
  }

  public Date getStartDate() {
    return startDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  @Validate(converter = CustomDateTypeConvertor.class)
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public List<ShippingOrder> getShippingOrders() {
    return shippingOrders;
  }

  public void setShippingOrders(List<ShippingOrder> shippingOrders) {
    this.shippingOrders = shippingOrders;
  }

  public List<ShippingOrder> getShippingOrderMarked() {
    return shippingOrderMarked;
  }

  public void setShippingOrderMarked(List<ShippingOrder> shippingOrderMarked) {
    this.shippingOrderMarked = shippingOrderMarked;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
  }

  public int getResultCount() {
    return shippingOrderPage == null ? (shippingOrders == null ? 0 : shippingOrders.size()) : shippingOrderPage.getTotalResults();
  }

  public Integer getDefaultPerPage() {
    return defaultPerPage;
  }

  public void setDefaultPerPage(Integer defaultPerPage) {
    this.defaultPerPage = defaultPerPage;
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("startDate");
    params.add("endDate");
    int ctr = 0;
    if (shippingOrderStatusList != null && shippingOrderStatusList.size() > 0) {
      for (ShippingOrderStatus orderStatus : shippingOrderStatusList) {
        if (orderStatus != null) {
          params.add("shippingOrderStatusList[" + ctr + "]");
        }
        ++ctr;
      }
    }
    return params;
  }

  public List<ShippingOrderStatus> getShippingOrderStatusList() {
    return shippingOrderStatusList;
  }

  public void setShippingOrderStatusList(List<ShippingOrderStatus> shippingOrderStatusList) {
    this.shippingOrderStatusList = shippingOrderStatusList;
  }

  public List<ShippingOrderStatus> getShippingOrderStatus() {
    return shippingOrderStatus;
  }

  public void setShippingOrderStatus(List<ShippingOrderStatus> shippingOrderStatus) {
    this.shippingOrderStatus = shippingOrderStatus;
  }

}
