package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductVariantInfo;
import com.hk.web.HealthkartResponse;

public class CartAction extends AbstractLoyaltyAction {

	private String productVariantId;
	private long qty;
	
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	private List<LoyaltyProduct> loyaltyProductList = new ArrayList<LoyaltyProduct>();
	
	@JsonHandler
	public Resolution addToCart() {
		
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", new HashMap<Object, Object>());
		
		Long orderId = getProcessor().createOrder(getPrincipal().getId());
		
		ProductVariantInfo info = new ProductVariantInfo();
		info.setProductVariantId(productVariantId);
		info.setQuantity(qty);
		List<ProductVariantInfo> infos = new ArrayList<ProductVariantInfo>();
		infos.add(info);
		try {
			getProcessor().addToCart(orderId, infos);
		} catch (InvalidOrderException e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), new HashMap<Object, Object>());
		}
        noCache();
		return new JsonResolution(healthkartResponse);
	}
	
	@DefaultHandler
	public Resolution viewKart() {
		Order order = getProcessor().getOrder(getPrincipal().getId());
		if(order != null) {
			Set<CartLineItem> cartLineItems = order.getCartLineItems();
			for (CartLineItem cartLineItem : cartLineItems) {
				LoyaltyProduct loyaltyProduct  = loyaltyProgramService.getProductByVariantId(cartLineItem.getProductVariant().getId());
				loyaltyProduct.setQty(cartLineItem.getQty());
				loyaltyProductList.add(loyaltyProduct);
			}
		}
		return new ForwardResolution("/pages/loyalty/cart.jsp");
	}
	
    public Resolution checkout() {
        return new RedirectResolution(AddressSelectionAction.class);
    }

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public List<LoyaltyProduct> getLoyaltyProductList() {
		return loyaltyProductList;
	}
	
	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}
}
