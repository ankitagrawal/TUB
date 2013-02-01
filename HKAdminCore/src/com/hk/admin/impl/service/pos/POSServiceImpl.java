package com.hk.admin.impl.service.pos;

import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Country;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	AddressDao addressDao;
	@Autowired
	PincodeService pincodeService;

	public Order createOrderForStore(User user, Address address, Store store) {
		Order order = new Order();
		order.setUser(user);
		order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InCart));
		order.setAmount(0D);
		order.setSubscriptionOrder(false);
		order.setAddress(address);
		order.setStore(store);
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

	public Order createCartLineItems(List<POSLineItemDto> posLineItems, Order order) {
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
		Set<CartLineItem> cartLineItemSet = new HashSet<CartLineItem>(0);
		for (ProductVariant productVariant : productVariantQtyMap.keySet()) {
			productVariant.setQty(productVariantQtyMap.get(productVariant));
			CartLineItem cartLineItem = cartLineItemService.createCartLineItemWithBasicDetails(productVariant, order);
			//cartLineItem = cartLineItemService.save(cartLineItem);
			cartLineItemSet.add(cartLineItem);
		}
		order.setCartLineItems(cartLineItemSet);
		order = orderService.save(order);
		return order;
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
		shippingOrder.setAmount(order.getAmount());
		//shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.));
		shippingOrder = shippingOrderService.save(shippingOrder);

		shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
		shippingOrder = shippingOrderService.save(shippingOrder);
		return shippingOrder;
	}

	public POSLineItemDto getPosLineItemWithNonAvailableInventory(List<POSLineItemDto> posLineItemDtoList) {
		List<SkuItem> bookedSkuItems = new ArrayList<SkuItem>(0);
		for (POSLineItemDto posLineItemDto : posLineItemDtoList) {
			List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser());
			if(inStockSkuItemList != null) {
				inStockSkuItemList.removeAll(bookedSkuItems);
			}

			if(inStockSkuItemList == null || inStockSkuItemList.size() == 0) {
				return posLineItemDto;
			}

			SkuItem skuItem = inStockSkuItemList.get(0);
			bookedSkuItems.add(skuItem);
		}

		return null;
	}

	public void checkoutAndUpdateInventory(List<POSLineItemDto> posLineItems, ShippingOrder shippingOrder) {

		for (POSLineItemDto posLineItemDto : posLineItems) {
			Sku posLineItemSku = posLineItemDto.getSkuItem().getSkuGroup().getSku();
			int counter = 0;
			while (counter < posLineItemDto.getQty()) {
				List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser());
				// deliberately not handling null pointer exception, to rollback everything if exception is thrown(i.e. no in stock sku item found)
				SkuItem skuItem = inStockSkuItemList.get(0);
				skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
				baseDao.save(skuItem);
				LineItem lineItemToBeInsertedInPVI = null;
				for (LineItem lineItem : shippingOrder.getLineItems()) {
					if (lineItem.getSku().getId().equals(posLineItemSku.getId())) {
						lineItemToBeInsertedInPVI = lineItem;
						break;
					}
				}

				/*if (lineItemToBeInsertedInPVI == null) {
					addRedirectAlertMessage(new SimpleMessage("Some error occurred, order could not be processed"));
					logger.error("Line item not found for following Sku: " + posLineItemSku + " for POS checkout");
					return new ForwardResolution("/pages/pos/pos.jsp");
				}*/

				adminInventoryService.inventoryCheckinCheckout(posLineItemSku, skuItem, lineItemToBeInsertedInPVI, shippingOrder, null,
						null, null, inventoryService.getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1L, userService.getLoggedInUser());

				counter++;
			}
		}

	}

	public Address createOrUpdateAddressForUser(Address address, User customer, String phone, Warehouse warehouse) {
		if(address == null) {
			address = new Address();
		}
		if(StringUtils.isBlank(address.getLine1())) {
			address.setLine1(warehouse.getLine1());
			address.setLine2(warehouse.getLine2());
			address.setCity(warehouse.getCity());
			address.setState(warehouse.getState());
			address.setPincode(pincodeService.getByPincode(warehouse.getPincode()));
		}

		address.setName(customer.getName());
		address.setPhone(phone);
		address.setUser(customer);
		address.setCountry(addressDao.getCountry(80L));
		address = addressDao.save(address);
		return address;
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
