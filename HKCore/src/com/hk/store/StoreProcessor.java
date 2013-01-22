package com.hk.store;

import java.util.List;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;

public interface StoreProcessor {
	
	List<LoyaltyProduct> searchProducts(Long userId, SearchCriteria criteria);
	
	Long createOrder(Long userId);

	void addToCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException;
	
	Order getOrder(Long userId);
	
	Double calculateDebitAmount(Long orderId);
	
	List<Address> getUserAddresses(Long userId);
	
	Payment makePayment(Long orderId, String remoteId);
	
	void validateAndEscalateOrder(Long orderId) throws InvalidOrderException;
	
	OrderStatus getOrderStatus(Long orderId);
	
	void setShipmentAddress(Long orderId, Address address);

}
