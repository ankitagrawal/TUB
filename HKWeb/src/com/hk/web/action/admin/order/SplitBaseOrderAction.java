package com.hk.web.action.admin.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.exception.OrderSplitException;
import com.hk.filter.CartLineItemFilter;
import com.hk.service.OrderStatusService;
import com.hk.service.WarehouseService;
import com.hk.service.order.OrderService;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SplitBaseOrderAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(SplitBaseOrderAction.class);

  private Order baseOrder;
  private ShippingOrderService shippingOrderService;
  private AdminShippingOrderService adminShippingOrderService;
  private WarehouseService warehouseService;
  private OrderService orderService;
  private OrderStatusService orderStatusService;
  private ShippingOrderStatusService shippingOrderStatusService;


  Map<CartLineItem, Warehouse> cartLineItemWarehouseMap = new HashMap<CartLineItem, Warehouse>();


  @DontValidate
  @DefaultHandler
  @Secure(hasAnyRoles = {RoleConstants.ADMIN, RoleConstants.GOD, RoleConstants.CATEGORY_MANAGER}, authActionBean = AdminPermissionAction.class)
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/order/splitBaseOrder.jsp");
  }

  public Resolution splitBaseOrder() {

    if (baseOrder != null && EnumOrderStatus.Placed.getId().equals(baseOrder.getOrderStatus().getId())) {
      Map<Warehouse, Set<CartLineItem>> warehouseCartLineItemsMap = new HashMap<Warehouse, Set<CartLineItem>>();
      for (Map.Entry<CartLineItem, Warehouse> cartLineItemWarehouseEntry : cartLineItemWarehouseMap.entrySet()) {
        if (warehouseCartLineItemsMap.get(cartLineItemWarehouseEntry.getValue()) != null) {
          warehouseCartLineItemsMap.get(cartLineItemWarehouseEntry.getValue()).add(cartLineItemWarehouseEntry.getKey());
        } else {
          Set<CartLineItem> cartLineItemsInWH = new HashSet<CartLineItem>();
          cartLineItemsInWH.add(cartLineItemWarehouseEntry.getKey());
          warehouseCartLineItemsMap.put(cartLineItemWarehouseEntry.getValue(), cartLineItemsInWH);
        }
      }

      for (Map.Entry<Warehouse, Set<CartLineItem>> warehouseSetEntry : warehouseCartLineItemsMap.entrySet()) {

        try {
          adminShippingOrderService.createSOforManualSplit(warehouseSetEntry.getValue(), warehouseSetEntry.getKey());
        } catch (NoSkuException e) {
          logger.error("No sku found", e);
          addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
          return new RedirectResolution(ActionAwaitingQueueAction.class);
        } catch (OrderSplitException e) {
          logger.error("Could not split order", e);
          addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
          return new RedirectResolution(ActionAwaitingQueueAction.class);
        }
      }

      /**
       * if order has any services products create a shipping order and send it to service queue
       */
      if (baseOrder.getContainsServices()) {
        Set<CartLineItem> serviceCartLineItems = new CartLineItemFilter(baseOrder.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).hasOnlyServiceLineItems(true).filter();
        for (CartLineItem serviceCartLineItem : serviceCartLineItems) {
          try {
            adminShippingOrderService.createSOForService(serviceCartLineItem);
          }
          catch (NoSkuException e) {
            logger.error("No sku found", e);
            addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
            return new RedirectResolution(ActionAwaitingQueueAction.class);
          }
        }
      }

      baseOrder.setOrderStatus(orderStatusService.find(EnumOrderStatus.InProcess));
      baseOrder = orderService.save(baseOrder);
      orderService.logOrderActivity(baseOrder, EnumOrderLifecycleActivity.OrderSplit);

      addRedirectAlertMessage(new SimpleMessage("Order : " + baseOrder.getGatewayOrderId() + " was split manually."));
      return new RedirectResolution(ActionAwaitingQueueAction.class);
    } else {
      addRedirectAlertMessage(new SimpleMessage("Order : " + baseOrder.getGatewayOrderId() + " is in incorrect status cannot be split."));
      return new RedirectResolution(ActionAwaitingQueueAction.class);
    }
  }


  public Order getBaseOrder() {
    return baseOrder;
  }

  public void setBaseOrder(Order baseOrder) {
    this.baseOrder = baseOrder;
  }

  public Map<CartLineItem, Warehouse> getCartLineItemWarehouseMap() {
    return cartLineItemWarehouseMap;
  }

  public void setCartLineItemWarehouseMap(Map<CartLineItem, Warehouse> cartLineItemWarehouseMap) {
    this.cartLineItemWarehouseMap = cartLineItemWarehouseMap;
  }

  
  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  
  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  
  public void setOrderStatusService(OrderStatusService orderStatusService) {
    this.orderStatusService = orderStatusService;
  }

  
  public void setWarehouseService(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  
  public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
    this.shippingOrderStatusService = shippingOrderStatusService;
  }
}
