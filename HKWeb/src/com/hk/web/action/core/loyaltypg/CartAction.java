package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.order.Order;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductVariantInfo;
import com.hk.store.StoreProcessor;
import com.hk.web.HealthkartResponse;

public class CartAction extends BaseAction {

	private String productVariantId;
	private long qty;
	
	private Order order;
	
	@Qualifier("loyaltyStoreProcessor")
	@Autowired
	StoreProcessor processor;

	@JsonHandler
	public Resolution addToCart() {
		
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", new HashMap<Object, Object>());
		
		Long orderId = processor.createOrder(getPrincipal().getId());
		
		ProductVariantInfo info = new ProductVariantInfo();
		info.setProductVariantId(productVariantId);
		info.setQuantity(qty);
		List<ProductVariantInfo> infos = new ArrayList<ProductVariantInfo>();
		try {
			processor.addToCart(orderId, infos);
		} catch (InvalidOrderException e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), new HashMap<Object, Object>());
		}
        noCache();
		return new JsonResolution(healthkartResponse);
	}
	
	@DefaultHandler
	public Resolution viewKart() {
		order = processor.getOrder(getPrincipal().getId());
		return new ForwardResolution("/pages/loyaltypg/cart.jsp");
	}
	
    public Resolution placeOrder() {
        return new RedirectResolution(AddressSelectionAction.class);
    }

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
