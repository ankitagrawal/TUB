package com.hk.rest.impl.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.rest.models.order.APIAddress;
import com.hk.rest.models.order.APIOrder;
import com.hk.rest.models.order.APIOrderDetails;
import com.hk.rest.models.order.APIPayment;
import com.hk.rest.models.order.APIProductDetail;
import com.hk.rest.pact.service.APIOrderService;
import com.hk.rest.pact.service.APIUserService;
import com.hk.util.json.JSONResponseBuilder;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentMode;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:26:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class APIOrderServiceImpl implements APIOrderService {

  @Autowired
  ProductVariantService productVariantService;
  @Autowired
  PaymentManager paymentManager;
  @Autowired
  CartLineItemService cartLineItemService;
  @Autowired
  APIUserService apiUserService;
  @Autowired
  OrderManager orderManager;
  @Autowired
  OrderService orderService;
  @Autowired
  AddressDao addressDao;
  @Autowired
  PaymentModeDao paymentModeDao;
  @Autowired
  PaymentStatusDao paymentStatusDao;
  @Autowired
  PaymentService paymentService;


  public String createOrderInHK(APIOrder apiOrder) {
    Set<CartLineItem> cartLineItems =  new HashSet<CartLineItem>();
    //get hkuser object if he already exists or create a new hkuser
    User hkUser = getApiUserService().getHKUser(apiOrder.getApiUser());

    //first place a base order  or find one if it already exists
    Order order = getOrderManager().getOrCreateOrder(hkUser);
    order.setCartLineItems(cartLineItems);
    order = getOrderService().save(order);
    //add items in the cart
    cartLineItems = addCartLineItems(apiOrder.getApiOrderDetails(), order);
    //add promotional freebie - for MIH = SPT397-01
    //cartLineItems = addFreeCartLineItems("SPT397-01", order);

    order.setCartLineItems(cartLineItems);
    order = getOrderService().save(order);
    
    //how to check if address always exists or create a new address everytime?
    Address address = createAddress(apiOrder.getApiAddress(), hkUser);
    //save address in the base_order
    order.setAddress(address);
    //set store in base order
    order.setStore(hkUser.getStore());
    order = getOrderService().save(order);
    //update amount to be paid for the order... sequence is important here address need to be created priorhand!!
    getOrderManager().recalAndUpdateAmount(order);

    //create a payment
    Payment payment = createPayment(order, apiOrder.getApiPayment());

    //update order payment status and order status in general
    getOrderManager().orderPaymentReceieved(payment);

    return new JSONResponseBuilder().addField("hkOrderId", order.getId()).build();
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
    //dont set payment amount it will be taken from the order
    payment.setOrder(order);
    PaymentMode paymentMode;
    paymentMode = getPaymentModeDao().getPaymentModeById(new Long(apiPayment.getPaymentmodeId()));
    payment.setPaymentMode(paymentMode);
    // payment.setIp(remoteAddr);
    payment.setBankCode(apiPayment.getBankId());
    payment = getPaymentManager().createNewPayment(order, paymentMode, "182.12.1.1", apiPayment.getBankId()); //remote ip adddress is hard coded
    PaymentStatus paymentStatus = getPaymentStatusDao().getPaymentStatusById(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
    if (EnumPaymentMode.getPrePaidPaymentModes().contains(paymentMode.getId())) {
      paymentStatus = getPaymentStatusDao().getPaymentStatusById(EnumPaymentStatus.SUCCESS.getId());
    }
    payment.setPaymentStatus(paymentStatus);
    return getPaymentService().save(payment);
  }

  public Set<CartLineItem> addCartLineItems(APIOrderDetails apiOrderDetails, Order order) {
    Set<CartLineItem> cartLineItems = order.getCartLineItems();
    for (APIProductDetail detail : apiOrderDetails.getApiProductDetails()) {
      ProductVariant productVariant = getProductVariantService().getVariantById(detail.getProductId().trim());
      if (productVariant != null) {
        productVariant.setQty(new Long(detail.getQty()));
        CartLineItem cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
        cartLineItem = getCartLineItemService().save(cartLineItem);
        cartLineItems.add(cartLineItem);
      }
    }
    return cartLineItems;
  }

   public Set<CartLineItem> addFreeCartLineItems(String variantId, Order order) {
    Set<CartLineItem> cartLineItems = order.getCartLineItems();
    ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
    if (productVariant != null) {
      productVariant.setQty(1L);
      CartLineItem cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
      cartLineItem = getCartLineItemService().save(cartLineItem);
      cartLineItems.add(cartLineItem);
    }
    return cartLineItems;
  }

  @Override
  public String trackOrder(String orderId) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
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
}
