package com.hk.store.loyalty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.AbstractStoreProcessor;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductVariantInfo;
import com.hk.store.SearchCriteria;

@Component("loyaltyStoreProcessor")
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

	@Override
	protected void validateCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException {
		/*
		 * accumulate the input order id points and variant price
		 * and validate with user karma points. 
		 * if order points are less then ok otherwise throw InvalidOrderException
		 */
	}
}
