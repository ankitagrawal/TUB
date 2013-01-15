package com.hk.api.store.loyalty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.api.loyaltypg.domain.LoyaltyProduct;
import com.hk.api.loyaltypg.service.LoyaltyProgramService;
import com.hk.api.store.AbstractStoreProcessor;
import com.hk.api.store.SearchCriteria;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;

public class LoyaltyStoreProcessor extends AbstractStoreProcessor {

	private static final Long storeId = 2l;
	
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	@Override
	public List<LoyaltyProduct> searchProducts(Long userId, SearchCriteria criteria) {
		return loyaltyProgramService.listProucts(userId, criteria.getStartRow(), criteria.getMaxRows());
	}

	@Override
	protected Store getStore() {
		return storeService.getStoreById(storeId);
	}

	@Override
	protected Payment doPayment(Long orderId, String remoteIp) {
		Order order = orderService.find(orderId);
		loyaltyProgramService.debitKarmaPoints(orderId);
		Payment payment = paymentManager.createNewPayment(order, EnumPaymentMode.FREE_CHECKOUT.asPaymenMode(), remoteIp, null, null);
		return payment;
	}
}
