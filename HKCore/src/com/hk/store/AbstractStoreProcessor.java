package com.hk.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.OrderManager;
import com.hk.manager.SMSManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
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

	@Autowired protected PaymentService paymentService;
	@Autowired protected EmailManager emailManager;

	@Autowired SMSManager smsManager;

	@Override
	public Long createOrder(Long userId) {
		Order order = this.getCart(userId);
		if (order != null) {
			return order.getId();
		}
		order = new Order();
		order.setStore(this.getStore());
		order.setUser(this.userService.getUserById(userId));
		order.setOrderStatus(EnumOrderStatus.InCart.asOrderStatus());
		order.setAmount(0D);
		order.setSubscriptionOrder(false);
		this.orderService.save(order);
		order = this.getCart(userId);
		return order.getId();
	}

	@Override
	public void addToCart(Long orderId, List<ProductVariantInfo> productVariants) throws InvalidOrderException {
		Map<String, CartLineItem> cartItemMap = new HashMap<String, CartLineItem>();

		double totalHKPrice = 0;
		Order order = this.orderService.find(orderId);
		List<CartLineItem> cartItems = order.getProductCartLineItems();

		List<ProductVariantInfo> processedVariants = new ArrayList<ProductVariantInfo>();
		if (cartItems != null && cartItems.size() > 0) {
			for (CartLineItem cartLineItem : cartItems) {
				for (ProductVariantInfo productVariantInfo : productVariants) {
					if (cartLineItem.getProductVariant().getId().equals(productVariantInfo.getProductVariantId())) {
						cartLineItem.setQty(productVariantInfo.getQuantity());
						processedVariants.add(productVariantInfo);
					}
					totalHKPrice += cartLineItem.getHkPrice() * cartLineItem.getQty();
					cartItemMap.put(cartLineItem.getProductVariant().getId(), cartLineItem);
				}
			}
			productVariants.removeAll(processedVariants);
		}

		for (ProductVariantInfo productVariantInfo : productVariants) {
			ProductVariant productVariant = this.productVariantService.getVariantById(productVariantInfo
					.getProductVariantId());
			CartLineItem cartLineItem = new CartLineItem();
			cartLineItem.setProductVariant(productVariant);
			cartLineItem.setQty(productVariantInfo.getQuantity());
			cartLineItem.setOrder(order);
			cartLineItem.setMarkedPrice(productVariant.getMarkedPrice());
			cartLineItem.setDiscountOnHkPrice(0d);
			cartLineItem.setHkPrice(productVariant.getHkPrice());
			totalHKPrice += (cartLineItem.getHkPrice() * cartLineItem.getQty());
			cartLineItem.setLineItemType(EnumCartLineItemType.Product.asCartLineItemType());
			cartItemMap.put(cartLineItem.getProductVariant().getId(), cartLineItem);
		}

		this.validateCart(order.getUser().getId(), cartItemMap.values(), totalHKPrice);

		for (CartLineItem cartLineItem : cartItemMap.values()) {
			this.cartLineItemService.save(cartLineItem);
		}
		this.orderManager.trimEmptyLineItems(order);
	}

	@Override
	public Order getCart(Long userId) {
		Store store = this.getStore();

		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("store.id", store.getId()));

		criteria.createAlias("orderStatus", "os");
		criteria.add(Restrictions.eq("os.name", EnumOrderStatus.InCart.getName()));

		@SuppressWarnings("unchecked")
		List<Order> orders = this.baseDao.findByCriteria(criteria);
		if (orders != null && orders.size() > 0) {
			Order order = orders.iterator().next();
			this.orderManager.trimEmptyLineItems(order);
			return this.orderService.find(order.getId());
		}
		return null;
	}

	@Override
	public Order getOrderById(Long orderId) {
		return this.orderService.find(orderId);
	}

	@Override
	public Payment makePayment(Long orderId, String remoteIp) throws InvalidOrderException {
		Order order = this.orderService.find(orderId);
		List<CartLineItem> cartLineItems = order.getProductCartLineItems();
		this.validateCart(order.getUser().getId(), cartLineItems, order.getAmount());
		List<CartLineItem> lineItems = new ArrayList<CartLineItem>(cartLineItems);
		for (CartLineItem cartLineItem : lineItems) {
			if (cartLineItem.getQty() == 0l) {
				this.cartLineItemService.remove(cartLineItem.getId());
			}
		}
		return this.doPayment(orderId, remoteIp);
	}

	@Override
	public void setShipmentAddress(Long orderId, Address address) {
		Order order = this.orderService.find(orderId);
		User user = order.getUser();
		address.setUser(user);
		address = this.addressService.save(address);
		order.setAddress(address);
		this.orderService.save(order);
	}

	@Override
	public OrderStatus getOrderStatus(Long orderId) {
		Order order = this.orderService.find(orderId);
		return order.getOrderStatus();
	}

	@Override
	public List<Address> getUserAddresses(Long userId) {
		return this.addressService.getVisibleAddresses(this.userService.getUserById(userId));
	}

	protected abstract com.hk.domain.store.Store getStore();

	protected abstract Payment doPayment(Long orderId, String remoteIp);

	protected abstract void validateCart(Long userId, Collection<CartLineItem> cartLineItems, double hkPrice)
			throws InvalidOrderException;

	protected abstract void validatePayment(Long orderId) throws InvalidOrderException;
}
