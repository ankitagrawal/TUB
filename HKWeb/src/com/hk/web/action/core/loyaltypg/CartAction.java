package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.store.EnumStore;
import com.hk.domain.user.User;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductVariantInfo;
import com.hk.web.HealthkartResponse;

@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class CartAction extends AbstractLoyaltyAction {

	private String productVariantId;
	private long qty;
	private Double totalShoppingPoints = 0d;
	private CartLineItem cartLineItem;
	private int itemsInCart ;
	@Autowired LoyaltyProgramService loyaltyProgramService;   
	
	private List<LoyaltyProduct> loyaltyProductList;
	
	@JsonHandler
	public Resolution addToCart() {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		HealthkartResponse healthkartResponse = null;
		Long orderId = this.getProcessor().createOrder(this.getPrincipal().getId());
		
		ProductVariantInfo info = new ProductVariantInfo();
		info.setProductVariantId(this.productVariantId);
		info.setQuantity(this.qty);
		List<ProductVariantInfo> infos = new ArrayList<ProductVariantInfo>();
		infos.add(info);
		try {
			this.getProcessor().addToCart(orderId, infos);
		} catch (InvalidOrderException e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(), new HashMap<Object, Object>());
			this.noCache();
			return new JsonResolution(healthkartResponse);
		}
		this.init();
		responseMap.put("totalShoppingPoints", this.totalShoppingPoints);
		responseMap.put("itemsInCart", this.itemsInCart + 1);
		healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", responseMap);
        this.noCache();
		return new JsonResolution(healthkartResponse);
	}
	
	@DefaultHandler
	public Resolution viewCart() {
		return new ForwardResolution("/pages/loyalty/cart.jsp");
	}
	
	public Resolution emptyCart() {
		this.getProcessor().getCart(this.getPrincipal().getId()).setCartLineItems(null);
		return new ForwardResolution("/pages/loyalty/cart.jsp");
	}

	private void init() {
		Order order = this.getProcessor().getCart(this.getPrincipal().getId());
		if(order != null) {
			this.loyaltyProductList = new ArrayList<LoyaltyProduct>();
			List<CartLineItem> cartLineItems = order.getProductCartLineItems();
			for (CartLineItem cartLineItem : cartLineItems) {
				LoyaltyProduct loyaltyProduct  = this.loyaltyProgramService.getProductByVariantId(cartLineItem.getProductVariant().getId());
				loyaltyProduct.setQty(cartLineItem.getQty());
				this.loyaltyProductList.add(loyaltyProduct);
			}
			this.totalShoppingPoints = this.loyaltyProgramService.calculateLoyaltyPoints(order);
			this.itemsInCart = this.loyaltyProductList.size();
		}
	}

    public Resolution getCartItems() {
    	init();
        return new ForwardResolution("/pages/loyalty/cart.jsp");
    }
	@JsonHandler
	public Resolution updateQuantity() {
		return this.addToCart();
	}
	
    public Resolution checkout() {
        return new RedirectResolution(AddressSelectionAction.class);
    }

	public String getProductVariantId() {
		return this.productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public List<LoyaltyProduct> getLoyaltyProductList() {
		this.init();
		return this.loyaltyProductList;
	}
	
	public long getQty() {
		return this.qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}
	
	public Double getTotalShoppingPoints() {
		return this.totalShoppingPoints;
	}
	
	public void setTotalShoppingPoints(Double totalShoppingPoints) {
		this.totalShoppingPoints = totalShoppingPoints;
	}

	public CartLineItem getCartLineItem() {
		return this.cartLineItem;
	}

	public void setCartLineItem(CartLineItem cartLineItem) {
		this.cartLineItem = cartLineItem;
	}

	/**
	 * @return the itemsInCart
	 */
	public int getItemsInCart() {
		return itemsInCart;
	}

	/**
	 * @param itemsInCart the itemsInCart to set
	 */
	public void setItemsInCart(int itemsInCart) {
		this.itemsInCart = itemsInCart;
	}
}
