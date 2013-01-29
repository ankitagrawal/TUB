package com.hk.web.action.core.loyaltypg;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.store.InvalidOrderException;

public class PlaceOrderAction extends AbstractLoyaltyAction {
	
	private Address selectedAddress;
	private Order order;
	
	@DefaultHandler
	public Resolution summary() {
		order = getProcessor().getOrder(getPrincipal().getId());
		selectedAddress = order.getAddress();
		return new ForwardResolution("/pages/loyalty/orderSummary.jsp"); 
	}
	
	public Resolution confirm() {
		order = getProcessor().getOrder(getPrincipal().getId());
		try {
			getProcessor().makePayment(order.getId(), getRemoteHostAddr());
			getProcessor().escalateOrder(order.getId());
		} catch (InvalidOrderException e) {
			//Log the message
		}
		return new RedirectResolution("/pages/loyalty/orderStatus.jsp");
	}
	
	public Address getSelectedAddress() {
		return selectedAddress;
	}
	
	public Order getOrder() {
		return order;
	}
}
