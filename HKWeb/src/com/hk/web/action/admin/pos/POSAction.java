package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.courier.ReverseOrderTypeConstants;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.loyaltypg.UserBadgeInfo;
import com.hk.domain.order.CartLineItem;
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
import com.hk.dto.pricing.PricingDto;
import com.hk.helper.InvoiceNumHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pricing.PricingEngine;
import com.hk.taglibs.Functions;
import com.hk.util.CartLineItemWrapper;
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
	private boolean addLoyaltyUser;
	private String cardNumber ;
	private boolean useRewardPoints;
	private User loyaltyCustomer;
	private boolean loyaltyCustomerAdded;
	
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
	private PincodeService pincodeService;
	@Autowired
	private SeekInvoiceNumService seekInvoiceNumService;
	@Autowired
	private SkuGroupService skuGroupService;
	@Autowired
	private ReverseOrderService reverseOrderService;
	@Autowired
	private LoyaltyProgramService loyaltyProgramService;
	@Autowired
	private RewardPointService rewardPointService;
	@Autowired
	private PricingEngine pricingEngine;
	@Autowired
	private OrderManager orderManager;
	
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
					dataMap.put("isLoyaltyUser", false);
				}
				if (customer.getRoleStrings().contains(RoleConstants.HK_LOYALTY_USER)) {
					// if already a loyalty user then fill params
					this.addLoyaltyUser = false;
					this.loyaltyCustomer = customer;
					dataMap.put("isLoyaltyUser", true);
					dataMap.put("loyaltyPoints", Functions.roundNumberForDisplay(loyaltyProgramService.calculateLoyaltyPoints(customer)));
					UserBadgeInfo badgeInfo = loyaltyProgramService.getUserBadgeInfo(customer);
					dataMap.put("badgeName", badgeInfo.getBadge().getBadgeName());
					dataMap.put("cardNumber", badgeInfo.getCardNumber());
				} else {
					this.addLoyaltyUser = true;
					dataMap.put("cardNumber", null);
				}
				// Did not use getEligibleRewardPointsForUser(login) API of rewardPointService to save a db hit to find the user again
				double rewardPoints = 0.0;
				if (customer.getUserAccountInfo()==null) {
					rewardPoints = rewardPointService.getTotalRedeemablePoints(customer);
				} else {
					rewardPoints = (rewardPointService.getTotalRedeemablePoints(customer)
							- customer.getUserAccountInfo().getOverusedRewardPoints());
				}
				
				if (rewardPoints > 0 ) {
					dataMap.put("rewardPoints", Functions.roundNumberForDisplay(rewardPoints));
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
		
		customer = this.updateCustomerDetails(warehouse);
		if (customer == null) {
			return new ForwardResolution("/pages/pos/pos.jsp");
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
		if (useRewardPoints) {
			Double redeemRewardPoints;
			if (customer.getUserAccountInfo() == null) {
				redeemRewardPoints = rewardPointService.getTotalRedeemablePoints(customer);
			} else {
				redeemRewardPoints = (rewardPointService.getTotalRedeemablePoints(customer) - customer
						.getUserAccountInfo().getOverusedRewardPoints());
			}
			if (redeemRewardPoints != null && redeemRewardPoints > 0) {
				CartLineItem rewardItem = pricingEngine.createRewardPointLineItemPOS(order, redeemRewardPoints);
				rewardItem.setOrder(order);
				order.getCartLineItems().add(rewardItem);
				order = orderService.save(order);
				order.setRewardPointsUsed(redeemRewardPoints);
				this.rewardPointService.redeemRewardPoints(order, redeemRewardPoints);
				if (order.getAmount() < redeemRewardPoints) {
					order.setAmount(0.0);
				} else {
					order.setAmount(order.getAmount() - redeemRewardPoints);
				}
				order = this.orderService.save(order);
			}
		}
		Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), null, null, null);
		if (payment == null) {
			addRedirectAlertMessage(new SimpleMessage("Payment could not be processed, contact Application Support"));
			return new ForwardResolution("/pages/pos/pos.jsp");
		}
		
		if (paymentMode.getId().equals(EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId())) {
			payment.setGatewayReferenceId(paymentReferenceNumber);
			payment.setBankName(paymentRemarks);
			payment.setLastFourDigitCardNo(lastFourDigitCardNo);
		}
		payment.setAmount(order.getAmount());
		
		payment.setPaymentStatus(paymentService.findPaymentStatus(EnumPaymentStatus.SUCCESS));
		paymentService.save(payment);

		order.setGatewayOrderId(payment.getGatewayOrderId());
		payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
		order.setPayment(payment);
		order = orderService.save(order);
		
		ShippingOrder shippingOrder = posService.createSOForStore(order, warehouse);

		posService.checkoutAndUpdateInventory(posLineItems, shippingOrder);

		String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().isB2bOrder());
		shippingOrder.setAccountingInvoiceNumber(seekInvoiceNumService.getInvoiceNum(invoiceType, shippingOrder.getWarehouse()));

		shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Delivered));
		ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
		shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
		shippingOrder = shippingOrderService.save(shippingOrder);
		
		order.setOrderStatus(EnumOrderStatus.Delivered.asOrderStatus());
		orderService.save(order);
		
		
		double loyaltyPointsEarned = 0.0;
		if (customer.getRoleStrings().contains(RoleConstants.HK_LOYALTY_USER)) {
			loyaltyPointsEarned = loyaltyProgramService.creditKarmaPoints(order);
			loyaltyProgramService.approveKarmaPoints(order);
			loyaltyProgramService.updateUserBadgeInfo(customer);
		}
		shippingOrderToPrint = shippingOrder;
		StringBuilder redirectMessage = new StringBuilder("Order processed successfully. ");
		if (loyaltyPointsEarned > 0) {
			redirectMessage.append(customer.getName() + " earned " + Functions.roundNumberForDisplay(loyaltyPointsEarned)
					+ " loyalty points for this transaction.");
		}
		addRedirectAlertMessage(new SimpleMessage(redirectMessage.toString()));
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

	public Resolution updateCustomerInfo () {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		User updatedCustomer = this.updateCustomerDetails(warehouse);
		if (updatedCustomer != null) {
			addRedirectAlertMessage(new SimpleMessage("Customer Info updated."));
		}
		
		return new ForwardResolution("/pages/pos/pos.jsp").addParameter("loyaltyCustomerAdded", loyaltyCustomerAdded);
	}
	
	private User updateCustomerDetails (Warehouse warehouse) {

		if (customer == null) {
			customer = posService.createUserForStore(email, name, null, RoleConstants.HK_USER);
		}

		if (newAddress) {
			if (StringUtils.isBlank(addressLine1) || StringUtils.isBlank(addressCity) || StringUtils.isBlank(addressPincode)) {
				address = posService.createDefaultAddressForUser(customer, phone, warehouse);
			} else {
				if (pincodeService.getByPincode(addressPincode) == null) {
					addRedirectAlertMessage(new SimpleMessage("Given pincode is not defined in the system, Order could not be processed"));
					return null;
				}
				address = posService.createAddressForUser(addressLine1, addressLine2, addressCity, addressState, addressPincode, phone, customer);
			}

		} else {
			if (address == null) {
				address = posService.createDefaultAddressForUser(customer, phone, warehouse);
			}
		}
		 if (addLoyaltyUser) {
			 loyaltyProgramService.createNewUserBadgeInfo(customer);
			 loyaltyCustomerAdded = true;
		 } else if (cardNumber != null && !cardNumber.isEmpty()) {
			 UserBadgeInfo info = loyaltyProgramService.getUserBadgeInfo(customer);
			 if (!cardNumber.trim().equalsIgnoreCase(info.getCardNumber())) {
				 loyaltyProgramService.updateCardNumber(loyaltyProgramService.getUserBadgeInfo(customer), cardNumber.trim());
			 }
		 }
		 	
		return customer;
	}
	
	public Resolution convertLoyaltyPoints () {
		double convertedPoints = 0.0;
		Map dataMap = new HashMap();
		if (loyaltyCustomer!= null) {
			convertedPoints= loyaltyProgramService.convertLoyaltyToRewardPoints(loyaltyCustomer);
		}
		if (convertedPoints > 0 ) {
			double totalRewardPoints = 0.0; 
			if (loyaltyCustomer.getUserAccountInfo()==null) {
				totalRewardPoints = rewardPointService.getTotalRedeemablePoints(loyaltyCustomer);
			} else {
				totalRewardPoints = (rewardPointService.getTotalRedeemablePoints(loyaltyCustomer)
						- loyaltyCustomer.getUserAccountInfo().getOverusedRewardPoints());
			}
			
			dataMap.put("totalRewardPoints", Functions.roundNumberForDisplay(totalRewardPoints));
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Customer has been awarded "
			+ Functions.roundNumberForDisplay(convertedPoints) + " reward Points.", dataMap);
			
			noCache();
			return new JsonResolution(healthkartResponse);
		} else {
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Insufficient loyalty points for conversion.", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);
		}
	}
	
	/**
	 * Setters and getters begin 
	 */
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the loyaltyCustomer
	 */
	public User getLoyaltyCustomer() {
		return loyaltyCustomer;
	}

	/**
	 * @param loyaltyCustomer the loyaltyCustomer to set
	 */
	public void setLoyaltyCustomer(User loyaltyCustomer) {
		this.loyaltyCustomer = loyaltyCustomer;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * @return the useRewardPoints
	 */
	public boolean isUseRewardPoints() {
		return useRewardPoints;
	}

	/**
	 * @param useRewardPoints the useRewardPoints to set
	 */
	public void setUseRewardPoints(boolean useRewardPoints) {
		this.useRewardPoints = useRewardPoints;
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

	/**
	 * @return the addLoyaltyUser
	 */
	public boolean isAddLoyaltyUser() {
		return addLoyaltyUser;
	}

	/**
	 * @param addLoyaltyUser the addLoyaltyUser to set
	 */
	public void setAddLoyaltyUser(boolean addLoyaltyUser) {
		this.addLoyaltyUser = addLoyaltyUser;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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

	/**
	 * @return the loyaltyCustomerAdded
	 */
	public boolean isLoyaltyCustomerAdded() {
		return loyaltyCustomerAdded;
	}

	/**
	 * @param loyaltyCustomerAdded the loyaltyCustomerAdded to set
	 */
	public void setLoyaltyCustomerAdded(boolean loyaltyCustomerAdded) {
		this.loyaltyCustomerAdded = loyaltyCustomerAdded;
	}
}
