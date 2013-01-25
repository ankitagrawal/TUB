package com.hk.store.loyalty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.AbstractStoreProcessor;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductAdapter;
import com.hk.store.ProductVariantInfo;
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
		Payment payment = paymentManager.createNewPayment(order, EnumPaymentMode.FREE_CHECKOUT.asPaymenMode(), remoteIp, null, null);
		loyaltyProgramService.debitKarmaPoints(orderId);
		payment.setPaymentStatus(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		baseDao.saveOrUpdate(payment);
		return payment;
	}

	@Override
	protected void validateCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException {
		/*
		 * TODO
		 * accumulate the input order id points and variant price
		 * and validate with user karma points. 
		 * if order points are less then ok otherwise throw InvalidOrderException
		 */
	}

	@Override
	public Double calculateDebitAmount(Long orderId) {
		return Double.valueOf(loyaltyProgramService.calculateDebitPoints(orderId));
	}

	@Override
	protected void validatePayment(Long orderId) throws InvalidOrderException {
		// TODO Check whether points are debited or not.
	}
}
