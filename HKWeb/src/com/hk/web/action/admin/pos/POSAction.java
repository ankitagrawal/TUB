package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.admin.courier.CreateReverseOrderAction;
import com.hk.web.action.admin.courier.ReversePickupCourierAction;
import com.hk.web.action.admin.order.search.SearchShippingOrderAction;
import com.hk.web.action.core.accounting.AccountingInvoiceAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/21/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.STORE_MANAGER}, authActionBean = AdminPermissionAction.class)
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
	private PaymentMode paymentMode;
	private String paymentReferenceNumber;
	private String paymentRemarks;
	private Long lastFourDigitCardNo;
	private Store store;
	private boolean newAddress = false;
	private String addressLine1;
	private String addressLine2;
	private String addressCity;
	private String addressState;
	private String addressPincode;
	private Double discount;
	private String shippingGatewayOrderId;
	private ShippingOrder shippingOrder;
	private String returnOrderReason;
	private Map<LineItem, Long> itemMap = new HashMap<LineItem, Long>();

	@Autowired
	private UserService userService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private POSService posService;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private PincodeService pincodeService;
	@Autowired
	private SeekInvoiceNumService seekInvoiceNumService;
	@Autowired
	private SkuGroupService skuGroupService;
	@Autowired
	private ReverseOrderService reverseOrderService;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/pos/pos.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution getProductDetailsByBarcode() {
		Map dataMap = new HashMap();
		List<SkuItem> inStockSkuItemList = new ArrayList<SkuItem>();
		SkuItem skuItem = null;
		if (productVariantBarcode != null) {
			SkuItem skuItemBarcode = skuGroupService.getSkuItemByBarcode(productVariantBarcode, userService.getWarehouseForLoggedInUser().getId(), EnumSkuItemStatus.Checked_IN.getId());
			if (skuItemBarcode != null) {
				skuItem = skuItemBarcode;
				inStockSkuItemList.add(skuItem);
				if (skuItemListToBeCheckedOut.contains(skuItem)) {
					HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Already Scanned Sku Item", dataMap);
					noCache();
					return new JsonResolution(healthkartResponse);
				}
			} else {
				inStockSkuItemList = adminInventoryService.getInStockSkuItems(productVariantBarcode, userService.getWarehouseForLoggedInUser());
				//exclude those sku items which have already been selected for this order
				if (inStockSkuItemList != null && inStockSkuItemList.size() > 0) {
					inStockSkuItemList.removeAll(skuItemListToBeCheckedOut);
					skuItem = inStockSkuItemList.get(0);
				}
			}

			if (inStockSkuItemList == null || inStockSkuItemList.size() == 0) {
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "No item found for this Barcode", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}

			skuItemListToBeCheckedOut.add(skuItem);
			SkuGroup skuGroup = skuItem.getSkuGroup();
			ProductVariant productVariant = skuGroup.getSku().getProductVariant();
			dataMap.put("product", productVariant.getProduct().getName());
			dataMap.put("options", productVariant.getOptionsCommaSeparated());
			dataMap.put("mrp", skuGroup.getMrp());
			dataMap.put("offerPrice", productVariant.getHkPrice());
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
					dataMap.put("pincode", address.getPincode().getPincode());
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
			customer = posService.createUserForStore(email, name, null, "HK_USER");
		}

		if (newAddress) {
			if (StringUtils.isBlank(addressLine1) || StringUtils.isBlank(addressCity) || StringUtils.isBlank(addressPincode)) {
				address = posService.createDefaultAddressForUser(customer, phone, warehouse);
			} else {
				if (pincodeService.getByPincode(addressPincode) == null) {
					addRedirectAlertMessage(new SimpleMessage("Given pincode is not defined in the system, Order could not be processed"));
					return new ForwardResolution("/pages/pos/pos.jsp");
				}
				address = posService.createAddressForUser(addressLine1, addressLine2, addressCity, addressState, addressPincode, phone, customer);
			}

		} else {
			if (address == null) {
				address = posService.createDefaultAddressForUser(customer, phone, warehouse);
			}
		}

		if (paymentMode == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select a payment mode"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}

		POSLineItemDto posLineItemDtoWithNonAvailableInventory = posService.getPosLineItemWithNonAvailableInventory(posLineItems);
		if (posLineItemDtoWithNonAvailableInventory != null) {
			addRedirectAlertMessage(new SimpleMessage("Required Inventory is not available for barcode: " + posLineItemDtoWithNonAvailableInventory.getProductVariantBarcode() +
					" and Product: " + posLineItemDtoWithNonAvailableInventory.getProductName() + ". Please scan the order again"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}

		Store store = userService.getWarehouseForLoggedInUser().getStore();

		order = posService.createOrderForStore(customer, address, store);
		if (order == null) {
			addRedirectAlertMessage(new SimpleMessage("Error occurred while creating Order"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		order.setAmount(grandTotal);
		order = posService.createCartLineItems(posLineItems, order);
		if (discount != null) {
			posService.applyOrderLevelDiscountOnCartLineItems(order, discount);
			order.setAmount(grandTotal - discount);
		}

		Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), null, null, null);

		if (payment == null) {
			addRedirectAlertMessage(new SimpleMessage("Payment could not be processed, contact Application Support"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		payment.setAmount(order.getAmount());
		payment.setPaymentDate(BaseUtils.getCurrentTimestamp());

		if (paymentMode.getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId())) {
			payment.setGatewayReferenceId(paymentReferenceNumber);
			payment.setBankName(paymentRemarks);
			payment.setLastFourDigitCardNo(lastFourDigitCardNo);
		}
		payment.setPaymentStatus(paymentService.findPaymentStatus(EnumPaymentStatus.SUCCESS));
		baseDao.save(payment);

		order.setGatewayOrderId(payment.getGatewayOrderId());
		order.setPayment(payment);
		orderService.save(order);

		ShippingOrder shippingOrder = posService.createSOForStore(order, warehouse);

		posService.checkoutAndUpdateInventory(posLineItems, shippingOrder);

		String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().isB2bOrder());
		shippingOrder.setAccountingInvoiceNumber(seekInvoiceNumService.getInvoiceNum(invoiceType, shippingOrder.getWarehouse()));

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
		for (ShippingOrder shippingOrderInBaseOrder : order.getShippingOrders()) {
			shippingOrder = shippingOrderInBaseOrder;
			break;
		}

		return new RedirectResolution(AccountingInvoiceAction.class).addParameter("shippingOrder", shippingOrder);
	}

	public Resolution createReverseOrderForPOS() {
		//return new RedirectResolution(CreateReverseOrderAction.class, "createReverseOrderForPOS").addParameter("shippingGatewayOrderId", shippingGatewayOrderId);
		if (!StringUtil.isBlank(shippingGatewayOrderId)) {
			shippingOrder = shippingOrderService.findByGatewayOrderId(shippingGatewayOrderId);
		}
		if (shippingOrder != null) {
			if (reverseOrderService.getReverseOrderByShippingOrderId(shippingOrder.getId()) == null) {
				return new ForwardResolution("/pages/pos/posReturnOrder.jsp");
			} else {
				addRedirectAlertMessage(new SimpleMessage("Reverse Order has been already created for this SO"));
				return new RedirectResolution(SearchShippingOrderAction.class, "searchShippingOrder").addParameter("shippingOrderGatewayId", shippingOrder.getGatewayOrderId());
			}

		}
		addRedirectAlertMessage(new SimpleMessage("Invalid gateway Order Id"));
		return new RedirectResolution(POSAction.class);
	}

	public Resolution saveReverseOrder() {
		if (reverseOrderService.getReverseOrderByShippingOrderId(shippingOrder.getId()) == null) {
			ReverseOrder reverseOrder = reverseOrderService.createReverseOrder(shippingOrder, returnOrderReason, null);
			reverseOrderService.createReverseLineItems(reverseOrder, itemMap);
			//shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Reverse_Pickup_Initiated, null, returnOrderReason);
			shippingOrder.setOrderStatus(EnumShippingOrderStatus.SO_Customer_Return_Refunded.asShippingOrderStatus());
			shippingOrderService.save(shippingOrder);
			reverseOrder.setReceivedDate(new Date());
			reverseOrder.setReconciliationStatus(EnumReconciliationStatus.DONE.asReconciliationStatus());
			reverseOrderService.save(reverseOrder);
			addRedirectAlertMessage(new SimpleMessage("Reverse Order Created"));
			return new RedirectResolution(POSAction.class);
		} else {
			addRedirectAlertMessage(new SimpleMessage("Reverse Order has already been created for this Shipping order"));
			return new RedirectResolution(POSAction.class);
		}

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

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentReferenceNumber() {
		return paymentReferenceNumber;
	}

	public void setPaymentReferenceNumber(String paymentReferenceNumber) {
		this.paymentReferenceNumber = paymentReferenceNumber;
	}

	public String getPaymentRemarks() {
		return paymentRemarks;
	}

	public void setPaymentRemarks(String paymentRemarks) {
		this.paymentRemarks = paymentRemarks;
	}

	public Long getLastFourDigitCardNo() {
		return lastFourDigitCardNo;
	}

	public void setLastFourDigitCardNo(Long lastFourDigitCardNo) {
		this.lastFourDigitCardNo = lastFourDigitCardNo;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public boolean isNewAddress() {
		return newAddress;
	}

	public void setNewAddress(boolean newAddress) {
		this.newAddress = newAddress;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressPincode() {
		return addressPincode;
	}

	public void setAddressPincode(String addressPincode) {
		this.addressPincode = addressPincode;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getShippingGatewayOrderId() {
		return shippingGatewayOrderId;
	}

	public void setShippingGatewayOrderId(String shippingGatewayOrderId) {
		this.shippingGatewayOrderId = shippingGatewayOrderId;
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public String getReturnOrderReason() {
		return returnOrderReason;
	}

	public void setReturnOrderReason(String returnOrderReason) {
		this.returnOrderReason = returnOrderReason;
	}

	public Map<LineItem, Long> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<LineItem, Long> itemMap) {
		this.itemMap = itemMap;
	}
}
