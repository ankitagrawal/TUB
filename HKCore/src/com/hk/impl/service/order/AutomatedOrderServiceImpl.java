package com.hk.impl.service.order;

import java.util.Set;

import com.hk.pact.service.inventory.InventoryHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.InventoryManagement.InventoryManageService;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.order.AutomatedOrderService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 6:46 PM
 */
@Service
public class AutomatedOrderServiceImpl implements AutomatedOrderService{

    @Autowired
    private OrderService         orderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private LineItemDao lineItemDao;
    @Autowired
    private PaymentStatusDao paymentStatusDao;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InventoryHealthService inventoryHealthService;

    /**
     * creates base orders from within the code rather than through the usual UI flow.
     * This can be used for subscriptions and orders that are placed by other stores like MIH
     * @param order
     * @param cartLineItems
     * @param address
     * @param payment
     * @param store
     * @param isSubscriptionOrder
     * @return
     */
    @Transactional
    public Order placeOrder(Order order, Set<CartLineItem> cartLineItems, Address address, Payment payment, Store store, boolean isSubscriptionOrder){
        //first of all save the cartLine items
        order.setCartLineItems(cartLineItems);
        //set subscriptionOrder if true
        order.setSubscriptionOrder(isSubscriptionOrder);
        //set address in the base_order
        order.setAddress(address);
        //set store in base order
        order.setStore(store);
        order = orderService.save(order);

        //update amount to be paid for the order... sequence is important here address need to be created priorhand!!
        order=recalAndUpdateAmount(order);

        //update order payment status and order status in general
        order.setGatewayOrderId(payment.getGatewayOrderId());
        order.setPayment(payment);
        // save order with placed status since amount has been applied

	    Set<OrderCategory> categories = orderService.getCategoriesForBaseOrder(order);
	    order.setCategories(categories);

        order.setOrderStatus(EnumOrderStatus.Placed.asOrderStatus());
        //User adminUser = UserCache.getInstance().getAdminUser();
        User adminUser = getUserService().getAdminUser();
        getOrderLoggingService().logOrderActivity(order, adminUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderPlaced), "Automated Order Placement");

	    order=orderService.save(order);
        //creating entries in sku_item_CLI

        inventoryHealthService.tempBookSkuLineItemForOrder(order);
        orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);

       return  order;
    }

    private Order recalAndUpdateAmount(Order order){
         Set<CartLineItem> cartLineItems=order.getCartLineItems();
         double orderAmount=0.0;
         for(CartLineItem cartLineItem:cartLineItems){
              orderAmount=orderAmount+cartLineItem.getHkPrice()-cartLineItem.getDiscountOnHkPrice();
         }
         order.setAmount(orderAmount);
         return orderService.save(order);
    }

    public Order createNewOrder(User user){
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(orderStatusService.find(EnumOrderStatus.InCart));
        order.setAmount(0D);
        order.setSubscriptionOrder(false);
        order = orderService.save(order);
        return order;
    }

    public Payment createNewPayment(Order order, Double amount, PaymentMode paymentMode){
        Payment payment=new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        PaymentStatus paymentStatus = paymentStatusDao.getPaymentStatusById(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());;

        if (EnumPaymentMode.getPrePaidPaymentModes().contains(paymentMode.getId())) {
            paymentStatus = paymentStatusDao.getPaymentStatusById(EnumPaymentStatus.SUCCESS.getId());
        }
        payment.setPaymentStatus(paymentStatus);
        payment.setGatewayOrderId(PaymentManager.getUniqueGatewayOrderId(order));
        payment.setCreateDate(BaseUtils.getCurrentTimestamp());
        payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
        return paymentService.save(payment);
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
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

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public LineItemDao getLineItemDao() {
        return lineItemDao;
    }

    public void setLineItemDao(LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
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