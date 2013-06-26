package com.hk.store.loyalty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.RewardPointConstants;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.EnumStore;
import com.hk.domain.store.Store;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pricing.PricingEngine;
import com.hk.store.AbstractStoreProcessor;
import com.hk.store.CategoryDto;
import com.hk.store.InvalidOrderException;
import com.hk.store.ProductAdapter;
import com.hk.store.SearchCriteria;

@Service("loyaltyStoreProcessor")
public class LoyaltyStoreProcessor extends AbstractStoreProcessor {

	@Autowired LoyaltyProgramService loyaltyProgramService;
	@Autowired UserService userService;
	@Autowired PricingEngine pricingEngine;
	@Autowired RewardPointService rewardPointService;
	
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
	protected Payment doPayment(Long orderId, String remoteIp) {
		Order order = this.orderService.find(orderId);
		
		double rewardPoints = 0d;
		for (CartLineItem cartLineItem : order.getCartLineItems()) {
			rewardPoints += (cartLineItem.getHkPrice() * cartLineItem.getQty());
		}
		
		order.setAmount(rewardPoints);
		order.setRewardPointsUsed(rewardPoints);
		this.orderService.save(order);

		// add reward points before making payment
		String comment = "Reward points added for redemption only at loyalty store.";
		RewardPoint loyaltyRewardPoints = this.rewardPointService.addRewardPoints(order.getUser(), null, order,
				rewardPoints, comment, EnumRewardPointStatus.APPROVED,
				EnumRewardPointMode.HKLOYALTY_POINTS.asRewardPointMode());

		this.rewardPointService.approveRewardPoints(Arrays.asList(loyaltyRewardPoints), new DateTime().plusDays(180).toDate());

		order = this.orderManager.recalAndUpdateAmount(order);
		Payment payment = this.paymentManager.createNewPayment(order, EnumPaymentMode.FREE_CHECKOUT.asPaymenMode(), remoteIp, null, null, null);
		this.loyaltyProgramService.debitKarmaPoints(order);
		order = this.paymentManager.success(payment.getGatewayOrderId());
		
		return order.getPayment();
	}

	@Override
	protected void validateCart(Long userId, Collection<CartLineItem> cartLineItems, double hkPrice) throws InvalidOrderException {
		double shoppingPoints = this.loyaltyProgramService.calculateLoyaltyPoints(cartLineItems);
		double userKarmaPoints = this.loyaltyProgramService.calculateLoyaltyPoints(this.userService.getUserById(userId));
		if (RewardPointConstants.MAX_REWARD_POINTS <= hkPrice) {
			throw new InvalidOrderException("Sorry! You can not put any more product in your cart right now. Confirm this order and place another order to add more products.");
		} else if(shoppingPoints > userKarmaPoints ) {
			throw new InvalidOrderException("You do not have sufficient loyalty points to add this product.");
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
