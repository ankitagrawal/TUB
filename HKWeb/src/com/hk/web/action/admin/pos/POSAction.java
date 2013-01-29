package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.store.StoreService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.accounting.AccountingInvoiceAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/21/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class POSAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(POSAction.class);
	@Validate(on = "receivePaymentAndProcessOrder", required = true)
	private String phone;
	@Validate(on = "receivePaymentAndProcessOrder", required = true)
	private String email;
	@Validate(on = "receivePaymentAndProcessOrder", required = true)
	private String name;
	private String productVariantBarcode;
	private List<POSLineItemDto> posLineItems = new ArrayList<POSLineItemDto>(0);
	private User customer;
	private Double grandTotal;
	private Order order;
	private Address address;
	private List<SkuItem> skuItemListToBeCheckedOut = new ArrayList<SkuItem>(0);
	private ShippingOrder shippingOrderToPrint;

	@Autowired
	private UserService userService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private POSService posService;
	@Autowired
	private CartLineItemService cartLineItemService;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private SkuGroupService skuGroupService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private StoreService storeService;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/pos/pos.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution getProductDetailsByBarcode() {
		Map dataMap = new HashMap();
		if (productVariantBarcode != null) {
			List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(productVariantBarcode, userService.getWarehouseForLoggedInUser());
			//exclude those sku items which have already been selected for this order
			if (inStockSkuItemList != null) {
				inStockSkuItemList.removeAll(skuItemListToBeCheckedOut);
			}

			if (inStockSkuItemList == null || inStockSkuItemList.size() == 0) {
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "No item found for this Barcode", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}
			SkuItem skuItem = inStockSkuItemList.get(0);
			skuItemListToBeCheckedOut.add(skuItem);
			SkuGroup skuGroup = skuItem.getSkuGroup();
			ProductVariant productVariant = skuGroup.getSku().getProductVariant();
			//dataMap.put("skuGroupId", skuGroup.getId());
			dataMap.put("product", productVariant.getProduct().getName());
			dataMap.put("options", productVariant.getOptionsCommaSeparated());
			dataMap.put("mrp", skuGroup.getMrp());
			dataMap.put("offerPrice", productVariant.getHkPrice());
			//dataMap.put("skuItemHidden", skuItem.getId());
			dataMap.put("skuItemId", skuItem.getId());
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Barcode", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);

		} else {
			logger.error("null or empty barcode or warehouse Id passed to load pv details in getProductDetailsByBarcode method of POSAction");
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	@SuppressWarnings("unchecked")
	public Resolution getCustomerDetailsByLogin() {
		Map dataMap = new HashMap();

		if (!StringUtils.isBlank(email)) {
			User customer = userService.findByLogin(email);
			if (customer != null) {
				dataMap.put("customerName", customer.getName());
				dataMap.put("customer", customer);
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Barcode", dataMap);
				List<Address> addressList = addressService.getVisibleAddresses(customer);
				if (addressList != null && addressList.size() > 0) {
					//Get the last address of the user
					address = addressList.get(addressList.size() - 1);
					dataMap.put("address", address);
				}
				noCache();
				return new JsonResolution(healthkartResponse);
			}
		} else {
			logger.error("Blank email is passed in POSAction");
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Customer Not found", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);

	}

	public Resolution receivePaymentAndProcessOrder() {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		if (posLineItems.size() == 0) {
			addRedirectAlertMessage(new SimpleMessage("Please place order for atleast one item"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		if (customer == null) {
			//todo: is store id need to be set for new user
			customer = posService.createUserForStore(email, name, null, "HK_USER");
		}

		if(address != null && address.getId() != null) {
			if(StringUtils.isBlank(address.getLine1()) || StringUtils.isBlank(address.getCity()) || StringUtils.isBlank(address.getPin())) {
				addRedirectAlertMessage(new SimpleMessage("Please give the complete address, Order could not be processed"));
				return new ForwardResolution("/pages/pos/pos.jsp");
			}
		}

		if(address == null || address.getId() == null) {
			address = posService.createOrUpdateAddressForUser(address, customer, phone, warehouse);
		}

		POSLineItemDto posLineItemDtoWithNonAvailableInventory = posService.getPosLineItemWithNonAvailableInventory(posLineItems);
		if(posLineItemDtoWithNonAvailableInventory != null) {
			addRedirectAlertMessage(new SimpleMessage("Required Inventory is not available for barcode: " + posLineItemDtoWithNonAvailableInventory.getProductVariantBarcode() +
					" and Product: " + posLineItemDtoWithNonAvailableInventory.getProductName() + ". Please scan the order again"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		Store store = storeService.getStoreById(StoreService.PUNJABI_BAGH);

		order = posService.createOrderForStore(customer, address, store);
		if (order == null) {
			addRedirectAlertMessage(new SimpleMessage("Error occurred while creating Order"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		order.setAmount(grandTotal);
		order = posService.createCartLineItems(posLineItems, order);

		Payment payment = paymentManager.createNewPayment(order, paymentService.findPaymentMode(EnumPaymentMode.COUNTER_CASH), BaseUtils.getRemoteIpAddrForUser(getContext()),
				null, null);

		if (payment == null) {
			addRedirectAlertMessage(new SimpleMessage("Payment could not be processed, contact Application Support"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		payment.setPaymentMode(EnumPaymentMode.COUNTER_CASH.asPaymenMode());
		payment.setAmount(order.getAmount());
		payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
		payment.setGatewayReferenceId(null);
		payment.setPaymentStatus(paymentService.findPaymentStatus(EnumPaymentStatus.SUCCESS));
		baseDao.save(payment);

		order.setGatewayOrderId(payment.getGatewayOrderId());
		order.setPayment(payment);
		orderService.save(order);

		ShippingOrder shippingOrder = posService.createSOForStore(order, warehouse);

		posService.checkoutAndUpdateInventory(posLineItems, shippingOrder);

		//todo: discuss SO Lifecycle
		shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Delivered));
		shippingOrder = shippingOrderService.save(shippingOrder);
		order.setOrderStatus(EnumOrderStatus.Delivered.asOrderStatus());
		orderService.save(order);
		shippingOrderToPrint = shippingOrder;
		addRedirectAlertMessage(new SimpleMessage("Order processed successfully"));
		return new ForwardResolution("/pages/pos/pos.jsp");
	}

	public Resolution print() {
		if (order == null) {
			addRedirectAlertMessage(new SimpleMessage("Invalid Order Id"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		///get the first Shipping Order of the base Order
		ShippingOrder shippingOrder = null;
		for(ShippingOrder shippingOrderInBaseOrder : order.getShippingOrders()) {
			shippingOrder = shippingOrderInBaseOrder;
			break;
		}

		return new RedirectResolution(AccountingInvoiceAction.class).addParameter("shippingOrder", shippingOrder);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductVariantBarcode() {
		return productVariantBarcode;
	}

	public void setProductVariantBarcode(String productVariantBarcode) {
		this.productVariantBarcode = productVariantBarcode;
	}

	public List<POSLineItemDto> getPosLineItems() {
		return posLineItems;
	}

	public void setPosLineItems(List<POSLineItemDto> posLineItems) {
		this.posLineItems = posLineItems;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<SkuItem> getSkuItemListToBeCheckedOut() {
		return skuItemListToBeCheckedOut;
	}

	public void setSkuItemListToBeCheckedOut(List<SkuItem> skuItemListToBeCheckedOut) {
		this.skuItemListToBeCheckedOut = skuItemListToBeCheckedOut;
	}

	public ShippingOrder getShippingOrderToPrint() {
		return shippingOrderToPrint;
	}

	public void setShippingOrderToPrint(ShippingOrder shippingOrderToPrint) {
		this.shippingOrderToPrint = shippingOrderToPrint;
	}

}
