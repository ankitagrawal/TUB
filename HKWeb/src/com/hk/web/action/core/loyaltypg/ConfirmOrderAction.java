package com.hk.web.action.core.loyaltypg;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.Order;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.StoreProcessor;

public class ConfirmOrderAction extends BaseAction {
	
	@Qualifier("loyaltyStoreProcessor")
	@Autowired
	StoreProcessor processor;
	
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	private Order order;
	private int orderPoints;
	
	@DefaultHandler
	public Resolution viewOrder() {
		order = processor.getOrder(getPrincipal().getId());
		orderPoints = loyaltyProgramService.calculateLoyaltyPoints(order.getId());
		return new ForwardResolution("/pages/loyalty/orderSummary.jsp");
	}
	
	public Resolution confirm() {
		processor.makePayment(order.getId(), getRemoteHostAddr());
		processor.shipOrder(order.getId());
		return new ForwardResolution("/pages/loyalty/success.jsp");
	}
	
	public Order getOrder() {
		return order;
	}
	
	public int getOrderPoints() {
		return orderPoints;
	}
}
