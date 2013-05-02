package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.InvalidOrderException;

@Secure(hasAnyRoles = {RoleConstants.HK_USER}, authActionBean=SignInAction.class)
public class PlaceOrderAction extends AbstractLoyaltyAction {
	
	private Address shipmentAddress;
	private Order order;
	private String errorMessage;
	private List<LoyaltyProduct> loyaltyProductList;
	private double totalShoppingPoints;
	
	@Autowired
	private LoyaltyProgramService loyaltyProgramService;
	
	@DefaultHandler
	public Resolution summary() {
		this.order = this.getProcessor().getCart(this.getPrincipal().getId());
		this.shipmentAddress = this.order.getAddress();
		return new ForwardResolution("/pages/loyalty/orderSummary.jsp"); 
	}
	
	public Resolution confirm() {
		this.order = this.getProcessor().getCart(this.getPrincipal().getId());
		this.shipmentAddress = this.order.getAddress();
		try {
			this.getProcessor().makePayment(this.order.getId(), this.getRemoteHostAddr());
			this.getProcessor().escalateOrder(this.order.getId());
		} catch (InvalidOrderException e) {
			this.errorMessage = e.getMessage();
		}
		if(this.order != null) {
			this.loyaltyProductList = new ArrayList<LoyaltyProduct>();
			Set<CartLineItem> cartLineItems = this.order.getCartLineItems();
			for (CartLineItem cartLineItem : cartLineItems) {
				LoyaltyProduct loyaltyProduct  = this.loyaltyProgramService.getProductByVariantId(cartLineItem.getProductVariant().getId());
				loyaltyProduct.setQty(cartLineItem.getQty());
				this.loyaltyProductList.add(loyaltyProduct);
			}
			this.totalShoppingPoints = this.loyaltyProgramService.calculateLoyaltyPoints(this.order);
		}
		return new ForwardResolution("/pages/loyalty/orderStatus.jsp");
	}
		
	public Order getOrder() {
		return this.order;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the loyaltyProductList
	 */
	public List<LoyaltyProduct> getLoyaltyProductList() {
		return this.loyaltyProductList;
	}

	/**
	 * @param loyaltyProductList the loyaltyProductList to set
	 */
	public void setLoyaltyProductList(List<LoyaltyProduct> loyaltyProductList) {
		this.loyaltyProductList = loyaltyProductList;
	}

	/**
	 * @return the totalShoppingPoints
	 */
	public double getTotalShoppingPoints() {
		return this.totalShoppingPoints;
	}

	/**
	 * @param totalShoppingPoints the totalShoppingPoints to set
	 */
	public void setTotalShoppingPoints(double totalShoppingPoints) {
		this.totalShoppingPoints = totalShoppingPoints;
	}

	/**
	 * @return the loyaltyProgramService
	 */
	public LoyaltyProgramService getLoyaltyProgramService() {
		return this.loyaltyProgramService;
	}

	/**
	 * @param loyaltyProgramService the loyaltyProgramService to set
	 */
	public void setLoyaltyProgramService(LoyaltyProgramService loyaltyProgramService) {
		this.loyaltyProgramService = loyaltyProgramService;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @return the shipmentAddress
	 */
	public Address getShipmentAddress() {
		return this.shipmentAddress;
	}

	/**
	 * @param shipmentAddress the shipmentAddress to set
	 */
	public void setShipmentAddress(Address shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}
}
