package com.hk.web.action.admin.booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@Component
public class AdminBookingAction extends BaseAction {

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	OrderService orderService;

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
				soSiLiMap.put(shippingOrder, boSoSiLi);
			}
		}
		return new ForwardResolution("/pages/admin/bookingStatusForSO.jsp");
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
