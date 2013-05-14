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
import com.hk.domain.store.EnumStore;
import com.hk.domain.store.Store;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.service.UserService;
import com.hk.store.AbstractStoreProcessor;
import com.hk.store.CategoryDto;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductAdapter;
import com.hk.store.SearchCriteria;

@Service("loyaltyStoreProcessor")
public class LoyaltyStoreProcessor extends AbstractStoreProcessor {

	@Autowired LoyaltyProgramService loyaltyProgramService;
	@Autowired UserService userService;
	
	@Override
	public List<ProductAdapter> searchProducts(Long userId, SearchCriteria criteria) {
		List<LoyaltyProduct> list = this.loyaltyProgramService.listProucts(criteria);
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
		return EnumStore.LOYALTYPG.asStore();
	}

	@Override
	@Transactional
	protected Payment doPayment(Long orderId, String remoteIp) {
		Order order = this.orderService.find(orderId);
		Payment payment = this.paymentManager.createNewPayment(order, EnumPaymentMode.FREE_CHECKOUT.asPaymenMode(), remoteIp, null, null, null);
		this.loyaltyProgramService.debitKarmaPoints(order);
		payment.setPaymentStatus(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
		this.baseDao.saveOrUpdate(payment);
		return payment;
	}

	@Override
	protected void validateCart(Long userId, Collection<CartLineItem> cartLineItems) throws InvalidOrderException {
		double shoppingPoints = this.loyaltyProgramService.calculateLoyaltyPoints(cartLineItems);
		double userKarmaPoints = this.loyaltyProgramService.calculateLoyaltyPoints(this.userService.getUserById(userId));
		if(shoppingPoints > userKarmaPoints ) {
			throw new InvalidOrderException("You do not have sufficient loyalty points to purchase the product.");
		}
	}

	@Override
	public Double calculateDebitAmount(Long orderId) {
		Order order = this.orderService.find(orderId);
		return this.loyaltyProgramService.calculateLoyaltyPoints(order);
	}

	@Override
	protected void validatePayment(Long orderId) throws InvalidOrderException {
		Order order = this.orderService.find(orderId);
		if(!order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.asPaymenStatus().getId())) {
			throw new InvalidOrderException("Loyalty points are not debited yet. Unsuccessfull Payment.");
		}
	}

	@Override
	public int countProducts(Long userId, SearchCriteria criteria) {
		return this.loyaltyProgramService.countProucts(criteria);
	}

	@Override
	public List<CategoryDto> listCategories() {
		return this.loyaltyProgramService.listCategories();
	}
}
