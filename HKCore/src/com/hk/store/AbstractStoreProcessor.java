package com.hk.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;


import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;

public abstract class AbstractStoreProcessor implements StoreProcessor {

	@Autowired protected UserService userService;
	@Autowired protected BaseDao baseDao;
	@Autowired protected OrderService orderService;
	@Autowired protected StoreService storeService;
	@Autowired protected ProductVariantService productVariantService;
	@Autowired protected PaymentManager paymentManager;
	@Autowired protected AddressService addressService;
	@Autowired protected CartLineItemService cartLineItemService;
  @Autowired protected OrderManager orderManager;

	@Override
	public Long createOrder(Long userId) {
		Order order = getCart(userId);
		if(order != null) {
			return order.getId();
		}
		order = new Order();
		order.setStore(getStore());
		order.setUser(userService.getUserById(userId));
		order.setOrderStatus(EnumOrderStatus.InCart.asOrderStatus());
		order.setAmount(0D);
		order.setSubscriptionOrder(false);
		orderService.save(order);
		order = getCart(userId);
		return order.getId();
	}

	@Override
	public void addToCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException {
		Map<String, CartLineItem> cartItemMap = new HashMap<String, CartLineItem>();

		Order order = orderService.find(orderId);
		Set<CartLineItem> cartItems = order.getCartLineItems();
		
		List<ProductVariantInfo> processedVariants = new ArrayList<ProductVariantInfo>();
		if (cartItems != null && cartItems.size() > 0) {
			for (CartLineItem cartLineItem : cartItems) {
				for (ProductVariantInfo productVariantInfo : productVariants) {
					if (cartLineItem.getProductVariant().getId().equals(productVariantInfo.getProductVariantId())) {
						cartLineItem.setQty(productVariantInfo.getQuantity());
						processedVariants.add(productVariantInfo);
					}
					cartItemMap.put(cartLineItem.getProductVariant().getId(), cartLineItem);
				}
			}
			productVariants.removeAll(processedVariants);
		}

		for (ProductVariantInfo productVariantInfo : productVariants) {
			ProductVariant productVariant = productVariantService.getVariantById(productVariantInfo.getProductVariantId());
			CartLineItem cartLineItem = new CartLineItem();
			cartLineItem.setProductVariant(productVariant);
			cartLineItem.setQty(productVariantInfo.getQuantity());
			cartLineItem.setOrder(order);
			cartLineItem.setMarkedPrice(0d);
			cartLineItem.setHkPrice(0d);
			cartLineItem.setDiscountOnHkPrice(0d);
			cartLineItem.setLineItemType(EnumCartLineItemType.Product.asCartLineItemType());
			cartItemMap.put(cartLineItem.getProductVariant().getId(), cartLineItem);
		}
		
		validateCart(order.getUser().getId(), cartItemMap.values());
		
		for (CartLineItem cartLineItem : cartItemMap.values()) {
			cartLineItemService.save(cartLineItem);
		}
	}

	@Override
	public Order getCart(Long userId) {
		Store store = getStore();

		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("store.id", store.getId()));
		
		criteria.createAlias("orderStatus", "os");
		criteria.add(Restrictions.eq("os.name", EnumOrderStatus.InCart.getName()));

		@SuppressWarnings("unchecked")
		List<Order> orders = baseDao.findByCriteria(criteria);
		if (orders != null && orders.size() > 0) {
      Order order = orders.iterator().next();
      order = orderManager.trimEmptyLineItems(order);
      return order;
		}
		return null;
	}
	
	@Override
	public Order getOrderById(Long orderId) {
		return orderService.find(orderId);
	}

	@Override
	public Payment makePayment(Long orderId, String remoteIp) throws InvalidOrderException {
		Order order = orderService.find(orderId);
		Set<CartLineItem> cartLineItems = order.getCartLineItems();
		validateCart(order.getUser().getId(), cartLineItems);
		List<CartLineItem> lineItems = new ArrayList<CartLineItem>(cartLineItems);
		for (CartLineItem cartLineItem : lineItems) {
			if(cartLineItem.getQty() == 0l) {
				cartLineItemService.remove(cartLineItem.getId());
			}
		}
		Payment payment = doPayment(orderId, remoteIp);
		order.setPayment(payment);
		order.setGatewayOrderId(payment.getGatewayOrderId());
		orderService.save(order);
		return payment;
	}

	@Override
	public void setShipmentAddress(Long orderId, Address address) {
		Order order = orderService.find(orderId);
		User user = order.getUser();
		address.setUser(user);
		address = addressService.save(address);
		order.setAddress(address);
		orderService.save(order);
	}

	@Override
	public void escalateOrder(Long orderId) throws InvalidOrderException {
		validatePayment(orderId);
		Order order = orderService.find(orderId);
	    Set<OrderCategory> categories = orderService.getCategoriesForBaseOrder(order);
	    order.setCategories(categories);
		order.setOrderStatus(EnumOrderStatus.Placed.asOrderStatus());
		order = orderService.save(order);
		orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
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

	protected abstract void validateCart(Long userId, Collection<CartLineItem> cartLineItems) throws InvalidOrderException;
	
	protected abstract void validatePayment(Long orderId) throws InvalidOrderException;
}
