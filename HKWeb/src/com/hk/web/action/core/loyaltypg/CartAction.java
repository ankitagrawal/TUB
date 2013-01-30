package com.hk.web.action.core.loyaltypg;

import java.util.*;

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
	private Double totalShoppingPoints = 0d;
	private CartLineItem cartLineItem;
	
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	private List<LoyaltyProduct> loyaltyProductList;
	
	@JsonHandler
	public Resolution addToCart() {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		HealthkartResponse healthkartResponse = null;
		Long orderId = getProcessor().createOrder(getPrincipal().getId());
		
		ProductVariantInfo info = new ProductVariantInfo();
		info.setProductVariantId(productVariantId);
		info.setQuantity(qty);
		List<ProductVariantInfo> infos = new ArrayList<ProductVariantInfo>();
		infos.add(info);
		try {
			getProcessor().addToCart(orderId, infos);
			init();
		} catch (InvalidOrderException e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), new HashMap<Object, Object>());
		}
		responseMap.put("totalShoppingPoints", totalShoppingPoints);
		healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", responseMap);
        noCache();
		return new JsonResolution(healthkartResponse);
	}
	
	@DefaultHandler
	public Resolution viewKart() {
		return new ForwardResolution("/pages/loyalty/cart.jsp");
	}
	
	private void init() {
		Order order = getProcessor().getCart(getPrincipal().getId());
		if(order != null) {
			loyaltyProductList = new ArrayList<LoyaltyProduct>();
			Set<CartLineItem> cartLineItems = order.getCartLineItems();
			for (CartLineItem cartLineItem : cartLineItems) {
				LoyaltyProduct loyaltyProduct  = loyaltyProgramService.getProductByVariantId(cartLineItem.getProductVariant().getId());
				loyaltyProduct.setQty(cartLineItem.getQty());
				loyaltyProductList.add(loyaltyProduct);
			}
			totalShoppingPoints = loyaltyProgramService.aggregatePoints(order.getId());
		}
	}

	@JsonHandler
	public Resolution updateQuantity() {
		return addToCart();
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
		init();
		return loyaltyProductList;
	}
	
	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}
	
	public Double getTotalShoppingPoints() {
		return totalShoppingPoints;
	}
	
	public void setTotalShoppingPoints(Double totalShoppingPoints) {
		this.totalShoppingPoints = totalShoppingPoints;
	}

	public CartLineItem getCartLineItem() {
		return cartLineItem;
	}

	public void setCartLineItem(CartLineItem cartLineItem) {
		this.cartLineItem = cartLineItem;
	}
}
