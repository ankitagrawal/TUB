package com.hk.store.loyalty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.AbstractStoreProcessor;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductAdapter;
import com.hk.store.SearchCriteria;

@Service("loyaltyStoreProcessor")
public class LoyaltyStoreProcessor extends AbstractStoreProcessor {

	private static final Long storeId = 2l;
	
	@Autowired LoyaltyProgramService loyaltyProgramService;
	
	@Override
	public List<ProductAdapter> searchProducts(Long userId, SearchCriteria criteria) {
		List<LoyaltyProduct> list = loyaltyProgramService.listProucts(userId, criteria.getStartRow(), criteria.getMaxRows());
		List<ProductAdapter> productList = new ArrayList<ProductAdapter>(list.size());
		for (LoyaltyProduct loyaltyProduct : list) {
			ProductAdapter adapter = new ProductAdapter();
			adapter.setLoyaltyProduct(loyaltyProduct);
			productList.add(adapter);
		}
		return productList;
	}

	@Override
	protected Store getStore() {
		return storeService.getStoreById(storeId);
	}

	@Override
	@Transactional
	protected Payment doPayment(Long orderId, String remoteIp) {
		Order order = orderService.find(orderId);
		Payment payment = paymentManager.createNewPayment(order, EnumPaymentMode.FREE_CHECKOUT.asPaymenMode(), remoteIp, null, null, null);
		loyaltyProgramService.debitKarmaPoints(orderId);
		payment.setPaymentStatus(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
		baseDao.saveOrUpdate(payment);
		return payment;
	}

	@Override
	protected void validateCart(Long userId, Collection<CartLineItem> cartLineItems) throws InvalidOrderException {
		double shoppingPoints = loyaltyProgramService.aggregatePoints(cartLineItems);
		double userKarmaPoints = loyaltyProgramService.calculateKarmaPoints(userId);
		if(shoppingPoints > userKarmaPoints ) {
			throw new InvalidOrderException("Not sufficient karma points in account to purchase the product.");
		}
	}

	@Override
	public Double calculateDebitAmount(Long orderId) {
		return loyaltyProgramService.aggregatePoints(orderId);
	}

	@Override
	protected void validatePayment(Long orderId) throws InvalidOrderException {
		Order order = orderService.find(orderId);
		if(!order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.asPaymenStatus().getId())) {
			throw new InvalidOrderException("Loyalty points are not debited yet. Unsuccessfull Payment.");
		}
	}
}
