package com.hk.rest.impl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.pact.service.core.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.AutomatedOrderService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.rest.models.order.APIAddress;
import com.hk.rest.models.order.APIOrder;
import com.hk.rest.models.order.APIOrderDetails;
import com.hk.rest.models.order.APIOrderTrackingItem;
import com.hk.rest.models.order.APIPayment;
import com.hk.rest.models.order.APIProductDetail;
import com.hk.rest.pact.service.APIOrderService;
import com.hk.rest.pact.service.APIUserService;
import com.hk.util.json.JSONResponseBuilder;
import com.akube.framework.util.BaseUtils;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 1, 2012 Time: 1:26:17 PM
 */
@Service
public class APIOrderServiceImpl implements APIOrderService {

    @Autowired
    ProductVariantService productVariantService;
    @Autowired
    PaymentManager        paymentManager;
    @Autowired
    CartLineItemService   cartLineItemService;
    @Autowired
    APIUserService        apiUserService;
    @Autowired
    OrderManager          orderManager;
    @Autowired
    OrderService          orderService;
    @Autowired
    AddressService        addressDao;
    @Autowired
    PaymentModeDao        paymentModeDao;
    @Autowired
    PaymentStatusDao      paymentStatusDao;
    @Autowired
    PaymentService        paymentService;
    @Autowired
    LineItemDao           lineItemDao;
    @Autowired
    OrderStatusService    orderStatusService;
    @Autowired
    OrderLoggingService   orderLoggingService;
    @Autowired
    ShippingOrderService  shippingOrderService;
    @Autowired
    InventoryService      inventoryService;
    @Autowired
    UserService           userService;
    @Autowired
    ShipmentService       shipmentService;
    @Autowired
    AutomatedOrderService automatedOrderService;

    public String createOrderInHK(APIOrder apiOrder) {
        Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();
        // get hkuser object if he already exists or create a new hkuser
        User hkUser = getApiUserService().getHKUser(apiOrder.getApiUser());

        // first place a base order or find one if it already exists
        Order order = getOrderManager().getOrCreateOrder(hkUser);
        order.setCartLineItems(cartLineItems);
        order = getOrderService().save(order);
        // add items in the cart
        cartLineItems = addCartLineItems(apiOrder.getApiOrderDetails(), order);

        order.setCartLineItems(cartLineItems);
        order = getOrderService().save(order);

        // how to check if address always exists or create a new address everytime?
        Address address = createAddress(apiOrder.getApiAddress(), hkUser);
        // save address in the base_order
        order.setAddress(address);
        // set store in base order
        order.setStore(hkUser.getStore());
        order = getOrderService().save(order);
        // update amount to be paid for the order... sequence is important here address need to be created priorhand!!
        getOrderManager().recalAndUpdateAmount(order);

        // create a payment
        Payment payment = createPayment(order, apiOrder.getApiPayment());

        // update order payment status and order status in general
        getOrderManager().orderPaymentReceieved(payment);

        // finalize order -- create shipping order and update inventory
        finalizeOrder(order);
        return new JSONResponseBuilder().addField("hkOrderId", order.getId()).build();
    }

    public void finalizeOrder(Order order) {

        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

        boolean shippingOrderExists = false;

        // Check Inventory health of order lineitems
        for (CartLineItem cartLineItem : productCartLineItems) {
            if (lineItemDao.getLineItem(cartLineItem) != null) {
                shippingOrderExists = true;
            }
        }

        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

        if (!shippingOrderExists) {
            shippingOrders = getOrderService().createShippingOrders(order);
        }

        if (shippingOrders != null && shippingOrders.size() > 0) {
            // save order with InProcess status since shipping orders have been created
            order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InProcess));
            order.setShippingOrders(shippingOrders);
            order = getOrderService().save(order);

            /**
             * Order lifecycle activity logging - Order split to shipping orders
             */
            getOrderLoggingService().logOrderActivity(order, getUserService().getAdminUser(),
                    getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), null);

            // auto escalate shipping orders if possible
            if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                for (ShippingOrder shippingOrder : shippingOrders) {
                    getShippingOrderService().autoEscalateShippingOrder(shippingOrder);
                }
            }

            for (ShippingOrder shippingOrder : shippingOrders) {
                shipmentService.createShipment(shippingOrder);
            }

        }

        // Check Inventory health of order lineitems
        for (CartLineItem cartLineItem : productCartLineItems) {
            inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
        }

    }

    public Address createAddress(APIAddress apiAddress, User hkUser) {
        Address address = new Address();
        address.setLine1(apiAddress.getLine1());
        address.setLine2(apiAddress.getLine2());
        address.setState(apiAddress.getState());
        address.setPin(apiAddress.getPin());
        address.setName(apiAddress.getName());
        address.setPhone(apiAddress.getPhone());
        address.setCity(apiAddress.getCity());
        address.setUser(hkUser);
        return getAddressDao().save(address);
    }

    public Payment createPayment(Order order, APIPayment apiPayment) {
        Payment payment = new Payment();
        // dont set payment amount it will be taken from the order
        payment.setOrder(order);
        PaymentMode paymentMode;
        paymentMode = getPaymentModeDao().getPaymentModeById(new Long(apiPayment.getPaymentmodeId()));
        payment.setPaymentMode(paymentMode);
        // payment.setIp(remoteAddr);
        payment.setBankCode(apiPayment.getBankId());
        payment = getPaymentManager().createNewPayment(order, paymentMode, "182.12.1.1", apiPayment.getBankId()); // remote
                                                                                                                    // ip
                                                                                                                    // adddress
                                                                                                                    // is
                                                                                                                    // hard
                                                                                                                    // coded
        PaymentStatus paymentStatus = getPaymentStatusDao().getPaymentStatusById(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
        if (EnumPaymentMode.getPrePaidPaymentModes().contains(paymentMode.getId())) {
            paymentStatus = getPaymentStatusDao().getPaymentStatusById(EnumPaymentStatus.SUCCESS.getId());
        }
        payment.setPaymentStatus(paymentStatus);
        payment.setCreateDate(new Date());
        payment.setPaymentDate(new Date());
        return getPaymentService().save(payment);
    }

    public Set<CartLineItem> addCartLineItems(APIOrderDetails apiOrderDetails, Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (APIProductDetail detail : apiOrderDetails.getApiProductDetails()) {
            ProductVariant productVariant = getProductVariantService().getVariantById(detail.getProductId().trim());
            if (productVariant != null) {
                productVariant.setQty(new Long(detail.getQty()));
                productVariant.setHkPrice(detail.getStorePrice());
                CartLineItem cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
                cartLineItem = getCartLineItemService().save(cartLineItem);
                cartLineItems.add(cartLineItem);
            }
        }
        return cartLineItems;
    }

    public String trackOrder(String orderId) {
        Order order = getOrderService().find(new Long(orderId));

        Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();
        List<LineItem> lineItems = new ArrayList<LineItem>();
        List<ShippingOrder> shippingOrders = new ArrayList<ShippingOrder>(order.getShippingOrders());

        if (shippingOrders != null && shippingOrders.size() > 0) {
            for (ShippingOrder shippingOrder : shippingOrders) {
                lineItems.addAll(shippingOrder.getLineItems());
            }
        } else {
            cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        }

        String status = "";
        ArrayList<APIOrderTrackingItem> trackingItemList = new ArrayList<APIOrderTrackingItem>();

        if (!lineItems.isEmpty()) {
            for (LineItem lineItem : lineItems) {
                APIOrderTrackingItem trackingItem = new APIOrderTrackingItem();
                trackingItem.setItemName(lineItem.getCartLineItem().getProductVariant().getProduct().getName());
                trackingItem.setQty(lineItem.getCartLineItem().getQty());

                ShippingOrder shippingOrder = lineItem.getShippingOrder();
                Shipment shipment = shippingOrder.getShipment();

                if (shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Shipped.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Delivered.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Returned.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Lost.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Cancelled.getId()) {

                    trackingItem.setStatus(shippingOrder.getBaseOrder().getOrderStatus().getName());

                } else if (shippingOrder.getOrderStatus().getId() == EnumShippingOrderStatus.SO_Shipped.getId() && shipment != null) {
                    String trackingId = null;
                    if (shipment.getAwb() != null) {
                        trackingId = shipment.getAwb().getAwbNumber();
                    }

                    trackingItem.setStatus("Shipped by" + shipment.getCourier().getName() + " - " + trackingId + " on " + shipment.getShipDate());
                } else {
                    trackingItem.setStatus(shippingOrder.getOrderStatus().getName());
                }
                trackingItemList.add(trackingItem);
            }
            Map<String, Object> orderStatusDetails = new HashMap<String, Object>();
            orderStatusDetails.put("orderStatus", trackingItemList);
            return new JSONResponseBuilder().addMap(orderStatusDetails).addField("shipped", "true").build();
        } else {
            if (!cartLineItems.isEmpty()) {
                for (CartLineItem cartLineItem : cartLineItems) {
                    status = cartLineItem.getOrder().getOrderStatus().getName();
                }
            }
        }

        return new JSONResponseBuilder().addField("orderStatus", status).addField("shipped", "false").build();

    }

    public String createOrderInHk(Order order) {

        order.getUser().setStore(order.getStore());
        order.getUser().setId(null);
        User hkUser = getApiUserService().getHKUser(order.getUser());

        Order hkOrder = automatedOrderService.createNewOrder(hkUser);
        hkOrder.setUserComments(order.getUserComments());
	    hkOrder.setScore(0L);

        Set<CartLineItem> cartLineItemSet = order.getCartLineItems();
        Set<CartLineItem> hkCartLineItemSet = new HashSet<CartLineItem>();
        for (CartLineItem cartLineItem : cartLineItemSet) {
            cartLineItem.setId(null);
            cartLineItem.setOrder(hkOrder);
            cartLineItem = cartLineItemService.save(cartLineItem);
            hkCartLineItemSet.add(cartLineItem);
        }

        Address address = order.getAddress();
        address.setId(null);
	    address.setUser(hkUser);
        address = addressDao.save(address);

        Payment payment = order.getPayment();
        payment.setId(null);
        payment.setOrder(hkOrder);

        payment.setGatewayOrderId(hkOrder.getId().toString() +"-"+ order.getGatewayOrderId().split("-")[1]);
        //payment.setGatewayOrderId(order.getGatewayOrderId());
	    payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
        payment = paymentService.save(payment);
        if (cartLineItemSet.size() > 0) {
            hkOrder = automatedOrderService.placeOrder(hkOrder, hkCartLineItemSet, address, payment, order.getStore(), false);

            return new JSONResponseBuilder().addField("hkOrderId", hkOrder.getId()).build();
        } else {
            return null;
        }
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public CartLineItemService getCartLineItemService() {
        return cartLineItemService;
    }

    public void setCartLineItemService(CartLineItemService cartLineItemService) {
        this.cartLineItemService = cartLineItemService;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    public APIUserService getApiUserService() {
        return apiUserService;
    }

    public void setApiUserService(APIUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public AddressService getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressService addressDao) {
        this.addressDao = addressDao;
    }

    public PaymentModeDao getPaymentModeDao() {
        return paymentModeDao;
    }

    public void setPaymentModeDao(PaymentModeDao paymentModeDao) {
        this.paymentModeDao = paymentModeDao;
    }

    public PaymentStatusDao getPaymentStatusDao() {
        return paymentStatusDao;
    }

    public void setPaymentStatusDao(PaymentStatusDao paymentStatusDao) {
        this.paymentStatusDao = paymentStatusDao;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public LineItemDao getLineItemDao() {
        return lineItemDao;
    }

    public void setLineItemDao(LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
