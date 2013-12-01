package com.hk.web.action.admin.order.split;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.courier.CourierConstants;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.core.Pincode;
import com.hk.exception.NoSkuException;
import com.hk.exception.OrderSplitException;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SplitBaseOrderAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(SplitBaseOrderAction.class);

    private Order baseOrder;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	PincodeService pincodeService;
	@Autowired
	AdminEmailManager adminEmailManager;

    Map<CartLineItem, Warehouse> cartLineItemWarehouseMap = new HashMap<CartLineItem, Warehouse>();

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyRoles = {RoleConstants.ADMIN, RoleConstants.GOD, RoleConstants.CATEGORY_MANAGER}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/splitBaseOrder.jsp");
    }

	@Transactional
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

			Pincode pincode = baseOrder.getAddress().getPincode();
			if (pincode != null) {
				for (Map.Entry<Warehouse, Set<CartLineItem>> warehouseSetEntry : warehouseCartLineItemsMap.entrySet()) {

					try {
						ShippingOrder shippingOrder = adminShippingOrderService.createSOforManualSplit(warehouseSetEntry.getValue(), warehouseSetEntry.getKey());
						if (shippingOrder != null) {
							orderLoggingService.logOrderActivity(baseOrder, userService.getLoggedInUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderManualSplit), null);
                            getOrderService().splitBOCreateShipmentEscalateSOAndRelatedTasks(shippingOrder.getBaseOrder());
						}
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
				Set<CartLineItem> serviceCartLineItems = new CartLineItemFilter(baseOrder.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).hasOnlyServiceLineItems(
						true).filter();
				if (serviceCartLineItems != null && serviceCartLineItems.size() > 0) {
					for (CartLineItem serviceCartLineItem : serviceCartLineItems) {
						try {
							orderService.createSOForService(serviceCartLineItem);
						} catch (NoSkuException e) {
							logger.error("No sku found", e);
							addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
							return new RedirectResolution(ActionAwaitingQueueAction.class);
						}
					}
				}

				baseOrder.setOrderStatus(orderStatusService.find(EnumOrderStatus.InProcess));
				baseOrder = orderService.save(baseOrder);
				//getOrderLoggingService().logOrderActivity(baseOrder, EnumOrderLifecycleActivity.OrderSplit);

				addRedirectAlertMessage(new SimpleMessage("Order : " + baseOrder.getGatewayOrderId() + " was split manually."));
				return new RedirectResolution(ActionAwaitingQueueAction.class);
			} else {
				addRedirectAlertMessage(new SimpleMessage("Order cannot be split as Pincode is not found in System "));
				adminEmailManager.sendNoShipmentEmail(CourierConstants.PINCODE_INVALID, null, baseOrder);
				return new RedirectResolution(ActionAwaitingQueueAction.class);
			}
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

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }


}
