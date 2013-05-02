package com.hk.web.action.core.loyaltypg;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.stripesstuff.plugin.security.Secure;

import com.hk.constants.core.RoleConstants;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.store.InvalidOrderException;

@Secure(hasAnyRoles = {RoleConstants.HK_LOYALTY_USER}, authActionBean=JoinLoyaltyProgramAction.class)
public class PlaceOrderAction extends AbstractLoyaltyAction {
	
	private Address selectedAddress;
	private Order order;
	private String errorMessage;
	
	@DefaultHandler
	public Resolution summary() {
		order = getProcessor().getCart(getPrincipal().getId());
		selectedAddress = order.getAddress();
		return new ForwardResolution("/pages/loyalty/orderSummary.jsp"); 
	}
	
	public Resolution confirm() {
		order = getProcessor().getCart(getPrincipal().getId());
		try {
			getProcessor().makePayment(order.getId(), getRemoteHostAddr());
			getProcessor().escalateOrder(order.getId());
		} catch (InvalidOrderException e) {
			errorMessage = e.getMessage();
		}
		order = getProcessor().getOrderById(order.getId());
		return new ForwardResolution("/pages/loyalty/orderStatus.jsp");
	}
	
	public Address getSelectedAddress() {
		return selectedAddress;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
