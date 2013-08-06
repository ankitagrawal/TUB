package com.hk.web.action.admin.booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@Component
public class AdminBookingAction extends BaseAction {

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SkuItemLineItemService skuItemLineItemService;

	private Long shippingOrderId;
	private Long baseOrderId;

	private List<SkuItemCLI> skuCLIList;
	private List<SkuItemLineItem> skuLiList;
	private HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLiMap;

	public Resolution getSkuItemLineItems() {
		if (shippingOrderId != null) {
			ShippingOrder so = shippingOrderService.find(shippingOrderId);
			skuLiList = new ArrayList<SkuItemLineItem>();
			for (LineItem li : so.getLineItems()) {
				if (li.getSkuItemLineItems() != null && li.getSkuItemLineItems().size() > 0) {
					skuLiList.addAll(li.getSkuItemLineItems());
				}
			}
		}
		return new ForwardResolution("/pages/admin/bookingStatusForSO.jsp");
	}

	public Resolution getSkuCartItemLineItems() {
		if (baseOrderId != null) {
			soSiLiMap = new HashMap<ShippingOrder, List<SkuItemLineItem>>();
			Order bo = orderService.find(baseOrderId);
			skuCLIList = new ArrayList<SkuItemCLI>();
			for (CartLineItem cli : bo.getCartLineItems()) {
				if (cli.getSkuItemCLIs() != null && cli.getSkuItemCLIs().size() > 0)
					skuCLIList.addAll(cli.getSkuItemCLIs());
			}

			for (ShippingOrder shippingOrder : bo.getShippingOrders()) {
				List<SkuItemLineItem> boSoSiLi = new ArrayList<SkuItemLineItem>();
				for (LineItem li : shippingOrder.getLineItems()) {
					if (li.getSkuItemLineItems() != null && li.getSkuItemLineItems().size() > 0) {
						boSoSiLi.addAll(li.getSkuItemLineItems());
					}
				}
				if (boSoSiLi != null && boSoSiLi.size() > 0) {
					soSiLiMap.put(shippingOrder, boSoSiLi);
				}
			}
		}
		return new ForwardResolution("/pages/admin/bookingStatusForSO.jsp");
	}

	public Resolution freeBookingTable() {
		if (shippingOrderId != null) {
			ShippingOrder so = shippingOrderService.find(shippingOrderId);
			skuItemLineItemService.freeBookingTable(so);
			addRedirectAlertMessage(new SimpleMessage("Freed Booking Table For Shipping Order: " + so.getId()));
			return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuItemLineItems").addParameter("shippingOrderId", shippingOrderId);
		} else if (baseOrderId != null) {
			Order bo = orderService.find(baseOrderId);
			Set<ShippingOrder> soSet = bo.getShippingOrders();
			if (soSet != null && soSet.size() > 0) {
				for (ShippingOrder so : soSet) {
					skuItemLineItemService.freeBookingTable(so);
				}
			}
			addRedirectAlertMessage(new SimpleMessage("Freed Booking Table For Base Order: " + bo.getId()));
			return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuCartItemLineItems").addParameter("baseOrderId", baseOrderId);
		}
		return null;
	}

	public Resolution closeWindow() {
		return new ForwardResolution("/pages/close.jsp");
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public Long getBaseOrderId() {
		return baseOrderId;
	}

	public void setBaseOrderId(Long baseOrderId) {
		this.baseOrderId = baseOrderId;
	}

	public List<SkuItemCLI> getSkuCLIList() {
		return skuCLIList;
	}

	public void setSkuCLIList(List<SkuItemCLI> skuCLIList) {
		this.skuCLIList = skuCLIList;
	}

	public List<SkuItemLineItem> getSkuLiList() {
		return skuLiList;
	}

	public void setSkuLiList(List<SkuItemLineItem> skuLiList) {
		this.skuLiList = skuLiList;
	}

	public HashMap<ShippingOrder, List<SkuItemLineItem>> getSoSiLi() {
		return soSiLiMap;
	}

	public void setSoSiLi(HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLi) {
		this.soSiLiMap = soSiLi;
	}

	public HashMap<ShippingOrder, List<SkuItemLineItem>> getSoSiLiMap() {
		return soSiLiMap;
	}

	public void setSoSiLiMap(HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLiMap) {
		this.soSiLiMap = soSiLiMap;
	}

}
