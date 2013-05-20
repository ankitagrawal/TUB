package com.hk.store;

import java.util.List;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;

public interface StoreProcessor {
	
	List<ProductAdapter> searchProducts(Long userId, SearchCriteria criteria);
	
	List<CategoryDto> listCategories();
	
	int countProducts(Long userId, SearchCriteria criteria);
	
	Long createOrder(Long userId);

	void addToCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException;
	
	Order getCart(Long userId);
	
	Order getOrderById(Long orderId);
	
	Double calculateDebitAmount(Long orderId);
	
	List<Address> getUserAddresses(Long userId);
	
	Payment makePayment(Long orderId, String remoteId) throws InvalidOrderException;
	
	OrderStatus getOrderStatus(Long orderId);
	
	void setShipmentAddress(Long orderId, Address address);

}
