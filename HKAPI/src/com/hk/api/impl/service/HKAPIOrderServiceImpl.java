package com.hk.api.impl.service;

import com.akube.framework.util.BaseUtils;
import com.hk.api.constants.EnumHKAPIErrorCode;
import com.hk.api.constants.HKAPIOperationStatus;
import com.hk.api.dto.HKAPIBaseDTO;
import com.hk.api.dto.order.*;
import com.hk.api.pact.service.HKAPIOrderService;
import com.hk.api.pact.service.HKAPIUserService;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.api.HkApiUser;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.order.AutomatedOrderService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.store.StoreService;
import com.hk.security.HkAuthService;
import com.hk.util.json.JSONResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 1, 2012 Time: 1:26:17 PM
 */
@Service
public class HKAPIOrderServiceImpl implements HKAPIOrderService {
    private static Logger logger = LoggerFactory.getLogger(HKAPIOrderService.class);

    @Autowired
    HkAuthService hkAuthService;

    @Autowired
    ProductVariantService productVariantService;
    @Autowired
    CartLineItemService   cartLineItemService;
    @Autowired
    HKAPIUserService hkapiUserService;
    @Autowired
    OrderManager          orderManager;
    @Autowired
    OrderService          orderService;
    @Autowired
    AddressDao            addressDao;
    @Autowired
    PaymentModeDao        paymentModeDao;
    @Autowired
    PaymentStatusDao      paymentStatusDao;
    @Autowired
    PaymentService        paymentService;
    @Autowired
    OrderStatusService    orderStatusService;
    @Autowired
    OrderLoggingService   orderLoggingService;
    @Autowired
    UserService           userService;
    @Autowired
    AutomatedOrderService automatedOrderService;
    @Autowired
    PincodeService pincodeService;
    @Autowired
    StoreService storeService;
    @Autowired
    BaseDao basedao;

    public static final Long INDIA_COUNTRY_ID=80L;

    public HKAPIBaseDTO createOrderInHK(String appToken, HKAPIOrderDTO hkapiOrderDTO) {
        HKAPIBaseDTO hkapiBaseDTO=new HKAPIBaseDTO();
        if(hkapiOrderDTO==null){
           hkapiBaseDTO.setStatus(HKAPIOperationStatus.ERROR);
           return hkapiBaseDTO;
        }
        if(!validateAppForOrderPlacement(appToken)){
            return new HKAPIBaseDTO(EnumHKAPIErrorCode.UnauthorizedToPlaceOrder);
        }
        Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();
        // get hkuser object if he already exists or create a new hkuser
        User hkUser = getHkapiUserService().getHKUser(hkapiOrderDTO.getHkapiUserDTO(), hkapiOrderDTO.getStoreId());

        Order order = automatedOrderService.createNewOrder(hkUser);
        order.setUserComments(hkapiOrderDTO.getHkapiOrderDetailsDTO().getUserComments());
        order.setScore(0L);
        order.setStore(storeService.getStoreById(hkapiOrderDTO.getStoreId()));

        //create cart line items
        cartLineItems = addCartLineItems(hkapiOrderDTO.getHkapiOrderDetailsDTO(), order);

        // how to check if address always exists or create a new address everytime?
        Address address = createAddress(hkapiOrderDTO.getHkapiAddressDTO(), hkUser);

       // create a payment
        Payment payment = createPayment(order,cartLineItems, hkapiOrderDTO.getHkapiPaymentDTO());

        if (cartLineItems.size() > 0) {
            order = automatedOrderService.placeOrder(order,cartLineItems, address, payment,hkUser.getStore(), false);
            logger.info("order placed from store "+order.getStore().getId()+" with hk order id "+order.getId());
            String hkOrderId=order.getId().toString();
            hkapiBaseDTO.setData(hkOrderId);
        } else {
            hkapiBaseDTO=new HKAPIBaseDTO(EnumHKAPIErrorCode.EmptyCart);
        }
        return hkapiBaseDTO;
    }

    protected boolean validateAppForOrderPlacement(String appToken){
        HkApiUser apiUser=hkAuthService.getApiUserFromAppToken(appToken);
        return apiUser.isOrderPlacementEnabled();
    }
    public Address createAddress(HKAPIAddressDTO hkapiAddressDTO, User hkUser) {
        Pincode pincode = pincodeService.getByPincode(hkapiAddressDTO.getPin());
        Address address = new Address();
        address.setLine1(hkapiAddressDTO.getLine1());
        address.setLine2(hkapiAddressDTO.getLine2());
        address.setState(hkapiAddressDTO.getState());
        address.setPincode(pincode);
        address.setName(hkapiAddressDTO.getName());
        address.setPhone(hkapiAddressDTO.getPhone());
        address.setCity(hkapiAddressDTO.getCity());
        address.setUser(hkUser);
        address.setCountry(addressDao.getCountry(INDIA_COUNTRY_ID));
        return getAddressDao().save(address);
    }

    public Payment createPayment(Order order, Set<CartLineItem> cartLineItems, HKAPIPaymentDTO hkapiPaymentDTO) {
        double orderAmount=0.0;
        for(CartLineItem cartLineItem:cartLineItems){
            orderAmount=orderAmount+cartLineItem.getHkPrice()*cartLineItem.getQty()-cartLineItem.getDiscountOnHkPrice();
        }
        PaymentMode paymentMode = getPaymentModeDao().getPaymentModeById(new Long(hkapiPaymentDTO.getPaymentmodeId()));
        Payment payment= automatedOrderService.createNewPayment(order,orderAmount, paymentMode);
        if(hkapiPaymentDTO.getGatewayId()!=null){
              Gateway gateway= basedao.get(Gateway.class, hkapiPaymentDTO.getGatewayId()) ;
            if(gateway!=null){
                payment.setGateway(gateway);
                payment=paymentService.save(payment);
            }
        }
        return  payment;
    }

    public Set<CartLineItem> addCartLineItems(HKAPIOrderDetailsDTO HKAPIOrderDetailsDTO, Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        for (HKAPICartLineItemDTO apiCartLineItem : HKAPIOrderDetailsDTO.getHkapiCartLineItemDTOs()) {
            if(apiCartLineItem.getCartLineItemType().equals(EnumCartLineItemType.Product.getId())||apiCartLineItem.getCartLineItemType().equals(EnumCartLineItemType.OrderLevelDiscount.getId())){
                ProductVariant productVariant = getProductVariantService().getVariantById(apiCartLineItem.getProductId().trim());
                if (productVariant != null) {
                    CartLineItemBuilder cartLineItemBuilder=new CartLineItemBuilder();
                    cartLineItemBuilder.ofType(getCartLineItemType(apiCartLineItem.getCartLineItemType()));
                    cartLineItemBuilder.forVariantQty(productVariant,apiCartLineItem.getQty()).hkPrice(apiCartLineItem.getStorePrice()).markedPrice(apiCartLineItem.getStoreMrp()).discountOnHkPrice(apiCartLineItem.getDiscountOnStorePrice());
                    CartLineItem cartLineItem=cartLineItemBuilder.build();
                    cartLineItem.setOrder(order);
                    cartLineItems.add(cartLineItem);
                }
            }else {
                CartLineItemBuilder cartLineItemBuilder=new CartLineItemBuilder();
                cartLineItemBuilder.ofType(getCartLineItemType(apiCartLineItem.getCartLineItemType()));
                cartLineItemBuilder.hkPrice(apiCartLineItem.getStorePrice()).markedPrice(apiCartLineItem.getStoreMrp()).discountOnHkPrice(apiCartLineItem.getDiscountOnStorePrice());
                CartLineItem cartLineItem=cartLineItemBuilder.build();
                cartLineItem.setQty(apiCartLineItem.getQty());
                cartLineItem.setOrder(order);
                cartLineItems.add(cartLineItem);
            }
        }
        return cartLineItems;
    }

    private  EnumCartLineItemType getCartLineItemType(Long itemType){
        for(EnumCartLineItemType enumType:EnumCartLineItemType.values()){
            if(enumType.getId().equals(itemType)){
                return enumType;
            }
        }
        return null;
    }

    @Deprecated
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
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_RTO.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Lost.getId()
                        && shippingOrder.getOrderStatus().getId() != EnumShippingOrderStatus.SO_Cancelled.getId()) {

                    trackingItem.setStatus(shippingOrder.getBaseOrder().getOrderStatus().getName());

                } else if (shippingOrder.getOrderStatus().getId() == EnumShippingOrderStatus.SO_Shipped.getId() && shipment != null) {
                    String trackingId = null;
                    if (shipment.getAwb() != null) {
                        trackingId = shipment.getAwb().getAwbNumber();
                    }

                    trackingItem.setStatus("Shipped by" + shipment.getAwb().getCourier().getName() + " - " + trackingId + " on " + shipment.getShipDate());
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
        User hkUser = getHkapiUserService().getHKUser(order.getUser());

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
        if(payment.getPaymentMode().getId().equals(15L)){
            payment.setPaymentMode(EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode());
        }
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

    public HKAPIUserService getHkapiUserService() {
        return hkapiUserService;
    }

    public void setHkapiUserService(HKAPIUserService hkapiUserService) {
        this.hkapiUserService = hkapiUserService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressDao addressDao) {
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
