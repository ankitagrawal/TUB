package com.hk.admin.impl.service.pos;

import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/22/13
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class POSServiceImpl implements POSService {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private CartLineItemService cartLineItemService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private ShippingOrderService shippingOrderService;

	public Order createOrderForStore(User user, Address address) {
		Order order = new Order();
		order.setUser(user);
		order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InCart));
		order.setAmount(0D);
		order.setSubscriptionOrder(false);
		order.setAddress(address);
		order = getOrderService().save(order);

		return order;
	}

	public User createUserForStore(String email, String name, String password, String roleName) {

		User user = new User();

		user.setName(name);
		user.setLogin(email);
		user.setEmail(email);
		if (StringUtils.isBlank(password)) {
			password = generatePasswordForStoreUser();
		}
		user.setPasswordChecksum(BaseUtils.passwordEncrypt(password));
		user = getUserService().save(user);
		if (StringUtils.isBlank(roleName)) {
			Role role = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
			roleName = role.getName();
			user.getRoles().add(role);
		} else {
			user.getRoles().add(getRoleService().getRoleByName(roleName));
		}
		user = getUserService().save(user);

		/* // generate user activation link
					if (RoleConstants.HK_UNVERIFIED.equals(roleName)) {
						String userActivationLink = getUserActivationLink(user);
						getEmailManager().sendWelcomeEmail(user, userActivationLink);
					}*/

		return user;
	}

	public void createCartLineItems(List<POSLineItemDto> posLineItems, Order order) {
		Map<ProductVariant, Long> productVariantQtyMap = new HashMap<ProductVariant, Long>(0);

		for (POSLineItemDto posLineItemDto : posLineItems) {
			ProductVariant productVariant = posLineItemDto.getSkuItem().getSkuGroup().getSku().getProductVariant();
			Long previousQtyOfSameProductVariant = productVariantQtyMap.get(productVariant);
			if (previousQtyOfSameProductVariant != null) {
				productVariantQtyMap.put(productVariant, previousQtyOfSameProductVariant + posLineItemDto.getQty());
			} else {
				productVariantQtyMap.put(productVariant, posLineItemDto.getQty());
			}
			productVariant.setQty(posLineItemDto.getQty());
		}
		for (ProductVariant productVariant : productVariantQtyMap.keySet()) {
			productVariant.setQty(productVariantQtyMap.get(productVariant));
			CartLineItem cartLineItem = cartLineItemService.createCartLineItemWithBasicDetails(productVariant, order);
			cartLineItemService.save(cartLineItem);
		}
	}

	public ShippingOrder createSOForStore(Order order, Warehouse warehouse) {
		ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);

		for (CartLineItem cartLineItem : order.getCartLineItems()) {
			ProductVariant productVariant = cartLineItem.getProductVariant();
			Sku sku = skuService.getSKU(productVariant, warehouse);
			if (sku != null) {
				LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
				shippingOrder.getLineItems().add(shippingOrderLineItem);
			} else {
				throw new NoSkuException(productVariant, warehouse);
			}
		}
		shippingOrder.setBasketCategory(orderService.getBasketCategory(shippingOrder).getName());
		//todo: Ask for below call
		ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
		shippingOrder.setAmount(order.getAmount());
		//shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.));
		shippingOrder = shippingOrderService.save(shippingOrder);

		shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
		shippingOrder = shippingOrderService.save(shippingOrder);
		return shippingOrder;
	}

	private String generatePasswordForStoreUser() {
		return BaseUtils.getRandomString(6) + BaseUtils.getCurrentTimestamp().getTime();
	}


	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public OrderStatusService getOrderStatusService() {
		return orderStatusService;
	}

	public void setOrderStatusService(OrderStatusService orderStatusService) {
		this.orderStatusService = orderStatusService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
}
