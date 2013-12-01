package com.hk.web.action.admin.shippingOrder;

import java.util.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.dao.Page;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.FixedShippingOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.FixedShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;

@Component
@Secure(hasAnyPermissions={PermissionConstants.SO_FIX, PermissionConstants.SO_REVIEW})
public class FixedShippingOrderAction extends BasePaginatedAction {


  @Autowired
  FixedShippingOrderDao fixedShippingOrderDao;

  private Integer defaultPerPage = 20;
  private Page fixedSOPage;
  private Warehouse warehouse;
  private String status;
  private List<FixedShippingOrder> fixedShippingOrderList;

  public Resolution search() {
    fixedSOPage = fixedShippingOrderDao.getFixedShippingOrder(warehouse, status, getPageNo(), getPerPage());
    if (fixedSOPage != null) {
      fixedShippingOrderList = fixedSOPage.getList();
    }
    return new ForwardResolution("/pages/admin/shippingOrder/fixedSOList.jsp");
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<FixedShippingOrder> getFixedShippingOrderList() {
    return fixedShippingOrderList;
  }

  public void setFixedShippingOrderList(List<FixedShippingOrder> fixedShippingOrderList) {
    this.fixedShippingOrderList = fixedShippingOrderList;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return fixedSOPage == null ? 0 : fixedSOPage.getTotalPages();
  }

  public int getResultCount() {
    return fixedSOPage == null ? 0 : fixedSOPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("warehouse");
    params.add("status");
    return params;
  }


}