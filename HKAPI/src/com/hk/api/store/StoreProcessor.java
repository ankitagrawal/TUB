package com.hk.api.store;

import java.util.List;

import com.hk.api.loyaltypg.domain.LoyaltyProduct;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;

public interface StoreProcessor {
	
	List<LoyaltyProduct> searchProducts(Long userId, SearchCriteria criteria);
	
	Long createOrder(Long userId);

	void addToCart(Long orderId, List<ProductVariantInfo> productVariants);
	
	void removeFromCart(Long orderId, List<ProductVariantInfo> productVariants);
	
	Order getOrder(Long orderId);
	
	Payment makePayment(Long orderId, String remoteId);
	
	void shipOrder(Long orderId);
	
	OrderStatus getOrderStatus(Long orderId);
	
	void shipmentAddress(Long orderId, Address address);

}
