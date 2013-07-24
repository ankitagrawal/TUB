package com.hk.admin.impl.service.pos;

import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	@Autowired
	private SkuGroupService skuGroupService;
	@Autowired
	private SkuItemDao skuItemDao;

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
			cartLineItemSet.add(cartLineItem);
		}
		order.setCartLineItems(cartLineItemSet);
		order = orderService.save(order);
		return order;
	}

	public void applyOrderLevelDiscountOnCartLineItems(Order order, Double orderLevelDiscount) {
		for (CartLineItem cartLineItem : order.getCartLineItems()) {
			Double discountOnHKPrice = ((cartLineItem.getHkPrice() * cartLineItem.getQty()) / order.getAmount()) * orderLevelDiscount;
			cartLineItem.setDiscountOnHkPrice(discountOnHKPrice);
			cartLineItemService.save(cartLineItem);
		}
	}

	public ShippingOrder createSOForStore(Order order, Warehouse warehouse) {
		ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);

		for (CartLineItem cartLineItem : order.getCartLineItems()) {
			if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.asCartLineItemType().getId())) {
				ProductVariant productVariant = cartLineItem.getProductVariant();
				Sku sku = skuService.getSKU(productVariant, warehouse);
				if (sku != null) {
					LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
					shippingOrder.getLineItems().add(shippingOrderLineItem);
				} else {
					throw new NoSkuException(productVariant, warehouse);
				}
			}
		}
		shippingOrder.setAmount(order.getAmount());
		shippingOrder = shippingOrderService.save(shippingOrder);

		shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
		shippingOrder = shippingOrderService.save(shippingOrder);
		Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
		shippingOrder.setShippingOrderCategories(categories);
		shippingOrder.setBasketCategory(orderService.getBasketCategory(categories).getName());
		shippingOrder = shippingOrderService.save(shippingOrder);
		return shippingOrder;
	}

	public POSLineItemDto getPosLineItemWithNonAvailableInventory(List<POSLineItemDto> posLineItemDtoList) {
		List<SkuItem> bookedSkuItems = new ArrayList<SkuItem>(0);
		SkuItem skuItem = null;
		List<SkuItem> inStockSkuItemList = new ArrayList<SkuItem>(0);
		for (POSLineItemDto posLineItemDto : posLineItemDtoList) {
			SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser().getId(), EnumSkuItemStatus.Checked_IN.getId());
			if (skuItemBarcode != null) {
				skuItem = skuItemBarcode;
				inStockSkuItemList.removeAll(bookedSkuItems);
			} else {
				inStockSkuItemList = adminInventoryService.getInStockSkuItems(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser());
				if (inStockSkuItemList != null) {
					inStockSkuItemList.removeAll(bookedSkuItems);
				}

				if (inStockSkuItemList == null || inStockSkuItemList.size() == 0) {
					return posLineItemDto;
				}

				skuItem = inStockSkuItemList.get(0);
			}
			bookedSkuItems.add(skuItem);
		}

		return null;
	}

	public void checkoutAndUpdateInventory(List<POSLineItemDto> posLineItems, ShippingOrder shippingOrder) {
		SkuItem skuItem = null;
		for (POSLineItemDto posLineItemDto : posLineItems) {
			Sku posLineItemSku = posLineItemDto.getSkuItem().getSkuGroup().getSku();
			int counter = 0;
			while (counter < posLineItemDto.getQty()) {
				SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser().getId(), EnumSkuItemStatus.Checked_IN.getId());
				if (skuItemBarcode != null) {
					skuItem = skuItemBarcode;
				} else {
					List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(posLineItemDto.getProductVariantBarcode(), userService.getWarehouseForLoggedInUser());
					// deliberately not handling null pointer exception, to rollback everything if exception is thrown(i.e. no in stock sku item found)
					skuItem = inStockSkuItemList.get(0);
				}
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

	public Address createDefaultAddressForUser(User customer, String phone, Warehouse warehouse) {
		Address address = new Address();
		address.setLine1("NA");
		address.setLine2("NA");
		address.setCity("NA");
		address.setState(warehouse.getState());
		address.setPincode(pincodeService.getByPincode(warehouse.getPincode()));

		address.setName(customer.getName());
		address.setPhone(phone);
		address.setUser(customer);
		address.setCountry(addressDao.getCountry(80L));
		address = addressDao.save(address);
		return address;
	}

	public Address createAddressForUser(String line1, String line2, String city, String state, String pincode, String phone, User customer) {
		Address address = new Address();
		address.setLine1(line1);
		address.setLine2(line2);
		address.setCity(city);
		address.setState(state);
		address.setPincode(pincodeService.getByPincode(pincode));
		address.setName(customer.getName());
		address.setPhone(phone);
		address.setUser(customer);
		address.setCountry(addressDao.getCountry(80L));
		address = addressDao.save(address);
		return address;
	}

	public List<PosProductSearchDto> searchProductInStore(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId) {
		return skuItemDao.getCheckedInSkuItems(productVariantId, primaryCategory, productName, brand, flavor, size, color, form, warehouseId);
	}

	public List<PosSkuGroupSearchDto> searchBatchesInStore(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId) {
		return skuItemDao.getCheckedInSkuItemsByGroup(productVariantId, primaryCategory, productName, brand, flavor, size, color, form, userService.getWarehouseForLoggedInUser().getId());
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
