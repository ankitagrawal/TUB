package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.admin.order.search.SearchShippingOrderAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShippingOrderAction extends BaseAction {

	private ShippingOrder shippingOrder;
	private Warehouse warehouseToUpdate;
	@Autowired
	WarehouseService warehouseService;

	@Autowired
	private ShippingOrderService shippingOrderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private AdminShippingOrderService adminShippingOrderService;
	@Autowired
	SkuService skuService;

	private ReplacementOrderReason rtoReason;

  private String customerSatisfyReason;

  @DefaultHandler
  public Resolution pre(){
     return new ForwardResolution("/pages/admin/order/flipShippingOrder.jsp");
  }

  public Resolution flipWarehouse() {
    boolean isWarehouseUpdated = adminShippingOrderService.updateWarehouseForShippingOrder(shippingOrder, warehouseToUpdate);

    String responseMsg = "";
    if (isWarehouseUpdated) {
      responseMsg = "Warehouse Flipped";
    } else {
      responseMsg = " Either All products not avaialable in other warehouse or sku for all products are nor available , so cannot be updated.";
    }
    addRedirectAlertMessage(new SimpleMessage(responseMsg));
    return new ForwardResolution("/pages/admin/order/flipShippingOrder.jsp");
  }

	@JsonHandler
	public Resolution initiateRTO() {
		adminShippingOrderService.initiateRTOForShippingOrder(shippingOrder, rtoReason);

		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
		return new JsonResolution(healthkartResponse);
	}

  	public Resolution markOrderCustomerReturn() {
    	getBaseDao().save(shippingOrder);
    	shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Customer_Return);
		return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderId", shippingOrder.getId());
	}

	public Resolution markOrderCustomerSatisfy(){
		getBaseDao().save(shippingOrder);
    	shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Customer_Satisfaction, null, customerSatisfyReason);
		return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderId", shippingOrder.getId());
	}


	@JsonHandler
	public Resolution markRTO() {
		adminShippingOrderService.markShippingOrderAsRTO(shippingOrder);

		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
		return new JsonResolution(healthkartResponse);
	}

	@JsonHandler
	public Resolution cancelShippingOrder() {
		adminShippingOrderService.cancelShippingOrder(shippingOrder);

		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "shipping order canceled", data);
		return new JsonResolution(healthkartResponse);
	}

	@JsonHandler
	public Resolution manualEscalateShippingOrder() {
		boolean isManualEscalable = shippingOrderService.isShippingOrderManuallyEscalable(shippingOrder);
		String message = "";
		if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(shippingOrder.getBaseOrder().getPayment().getPaymentStatus().getId())) {
			if (isManualEscalable) {
				message = "shipping order manually escalated";
				shippingOrderService.escalateShippingOrderFromActionQueue(shippingOrder, false);

			} else {
				message = "Shipping order cant be escalated";
			}
		}
		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, message, data);
		return new JsonResolution(healthkartResponse);
	}


	@JsonHandler
	public Resolution delieverDropShippingOrder() {
		String message = "";
		boolean orderHasOnlyDropShipProduct = true;

		for (LineItem lineItem : shippingOrder.getLineItems()) {
			CartLineItem cartLineItem = lineItem.getCartLineItem();

			if (EnumCartLineItemType.Product.getId().equals(cartLineItem.getLineItemType().getId())) {
				orderHasOnlyDropShipProduct &= cartLineItem.getProductVariant().getProduct().isDropShipping();
			}
			if (!orderHasOnlyDropShipProduct) {
				break;
			}
		}

		if (orderHasOnlyDropShipProduct) {
			adminShippingOrderService.markShippingOrderAsDelivered(shippingOrder);
			message = "shipping order marked as delieverd";
		} else {
			message = "shipping order cannot be marked delievered, since it has non drop ship products";
		}

		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, message, data);
		return new JsonResolution(healthkartResponse);
	}

    public Resolution bulkEscalateShippingOrder() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria =  new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setShippingOrderStatusList(Arrays.asList(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting)));
        shippingOrderSearchCriteria.setPaymentStatuses(EnumPaymentStatus.getEscalablePaymentStatuses());
        shippingOrderSearchCriteria.setDropShipping(false);
        List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria,false);
        for (ShippingOrder toBeEscalateShippingOrder : shippingOrders) {
            if(shippingOrderService.isShippingOrderAutomaticallyManuallyEscalable(toBeEscalateShippingOrder)){
                shippingOrderService.escalateShippingOrderFromActionQueue(toBeEscalateShippingOrder, true);
            }
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

        public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public ReplacementOrderReason getRtoReason() {
		return rtoReason;
	}

	public void setRtoReason(ReplacementOrderReason rtoReason) {
		this.rtoReason = rtoReason;
	}

  	public void setCustomerSatisfyReason(String customerSatisfyReason) {
    	this.customerSatisfyReason = customerSatisfyReason;
  	}

	public String getCustomerSatisfyReason() {
		return customerSatisfyReason;
	}

  public Warehouse getWarehouseToUpdate() {
    return warehouseToUpdate;
  }

  public void setWarehouseToUpdate(Warehouse warehouseToUpdate) {
    this.warehouseToUpdate = warehouseToUpdate;
  }
}
