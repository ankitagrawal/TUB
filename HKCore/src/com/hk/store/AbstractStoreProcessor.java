package com.hk.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;

public abstract class AbstractStoreProcessor implements StoreProcessor {

	@Autowired protected UserService userService;
	@Autowired protected OrderService orderService;
	@Autowired protected StoreService storeService;
	@Autowired protected ProductVariantService productVariantService;
	@Autowired protected OrderManager orderManager;
	@Autowired protected PaymentManager paymentManager;
	@Autowired protected AddressService addressService;
	@Autowired protected CartLineItemService cartLineItemService;

	@Override
	public Long createOrder(Long userId) {
		Order order =  orderManager.getOrCreateOrder(userService.getUserById(userId));
		order.setStore(getStore());
		orderService.save(order);
		return order.getId();
	}

	@Override
	public void addToCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException {
		validateCart(orderId, productVariants);
		Order order = orderService.find(orderId);
		List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
		for (ProductVariantInfo productVariantInfo : productVariants) {
			ProductVariant productVariant = productVariantService.getVariantById(productVariantInfo.getProductVariantId());
			productVariant.setQty(productVariantInfo.getQuantity());
			productVariantList.add(productVariantService.getVariantById(productVariantInfo.getProductVariantId()));
		}
		orderManager.createLineItems(productVariantList, order, null, null, null);
	}
	
	@Override
	public Order getOrder(Long userId) {
		return  orderManager.getOrCreateOrder(userService.getUserById(userId));
	}

	@Override
	public Payment makePayment(Long orderId, String remoteIp) {
		Order order = orderService.find(orderId);
		orderManager.trimEmptyLineItems(order);
		//Logic to calculate the charges for all variants
		//orderManager.recalAndUpdateAmount(order);
		return doPayment(orderId, remoteIp);
	}
	
	@Override
	public void shipmentAddress(Long orderId, Address address) {
		Order order = orderService.find(orderId);
		User user = order.getUser();
		address.setUser(user);
		addressService.save(address);
		order.setAddress(address);
		orderService.save(order);
	}

	@Override
	public void shipOrder(Long orderId) {
		Order order = orderService.find(orderId);
		orderService.splitBOEscalateSOCreateShipmentAndRelatedTasks(order);
	}

	@Override
	public OrderStatus getOrderStatus(Long orderId) {
		Order order = orderService.find(orderId);
		return order.getOrderStatus();
	}
	
	@Override
	public List<Address> getUserAddresses(Long userId) {
		return addressService.getVisibleAddresses(userService.getUserById(userId));
	}

	protected abstract com.hk.domain.store.Store getStore();
	
	protected abstract Payment doPayment(Long orderId, String remoteIp);
	
	protected abstract void validateCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException;

}
