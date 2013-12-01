package com.hk.impl.service.codbridge;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.util.StringUtils;
import com.google.gson.Gson;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.hkjunction.observers.OrderStatusMessage;
import com.hk.hkjunction.observers.OrderStatusUpdate;
import com.hk.hkjunction.observers.OrderType;
import com.hk.hkjunction.producer.Producer;
import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.hkjunction.producer.ProducerTypeEnum;
import com.hk.pact.service.UserService;
import com.hk.pact.service.codbridge.UserCallResponseObserver;
import com.hk.pact.service.codbridge.UserCartDetail;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.service.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 2:18:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OrderEventPublisher {

    @Value("#{hkEnvProps['" + Keys.Env.codRoute + "']}")
    private String codRoute;

	private static Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

	@Autowired
    ProducerFactory producerFactory;
    /*@Autowired
    UserCallResponseObserver userCallResponseObserver;*/
    @Autowired
    OrderService orderService;
    
    @Autowired Properties hkEnvProps;
    
    private ExecutorService splitExecutorService = null;
    
    @Autowired OrderLoggingService orderLoggingService;
    
    @Autowired UserService userService;
    
    @PostConstruct
    void init() {
    	int poolSize = 50;
    	if(hkEnvProps.getProperty("splitter.threadpool.size") != null) {
    		poolSize = Integer.valueOf(hkEnvProps.getProperty("splitter.threadpool.size"));
    	}
    	splitExecutorService = Executors.newFixedThreadPool(poolSize);
    }

    private String getCartDetailsJson(Order order) {

        UserCartDetail userCartDetail = new UserCartDetail();
        String jsonString = null;

        List<UserCartDetail.ProductVariant> productPurchasedList = new ArrayList<UserCartDetail.ProductVariant>();

        Set<CartLineItem> cartLineItemSet = order.getCartLineItems();
        if (cartLineItemSet != null) {
            for (CartLineItem cartLineItem : cartLineItemSet) {
                if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                    UserCartDetail.ProductVariant product = userCartDetail.new ProductVariant();
                    product.setId(cartLineItem.getProductVariant().getId());
                    product.setName(cartLineItem.getProductVariant().getProduct().getName());
                    product.setQty(cartLineItem.getQty());
                    product.setPrice(cartLineItem.getHkPrice());
                    productPurchasedList.add(product);
                }
            }
            userCartDetail.setOrderId(order.getId());
            userCartDetail.setProductList(productPurchasedList);
            Gson gson = JsonUtils.getGsonDefault();
            jsonString = gson.toJson(userCartDetail);

        }

        return jsonString;
    }

    private OrderStatusMessage getOrderMessage(Order order){
        Long customerPhoneNumber =  0L;
        try{
            customerPhoneNumber = StringUtils.getUserPhone(order.getAddress().getPhone());
        }catch (NumberFormatException ex){
            logger.error(String.format("Wrong phone number for order %d",order.getId()));
        }

        String orderId = order.getId().toString();
        String customerName = order.getUser().getName();
        String email =  order.getUser().getLogin();
        OrderStatusMessage orderStatusMessage = new OrderStatusMessage();
        orderStatusMessage.setAmount(order.getAmount());
        orderStatusMessage.setEmail(email);
        orderStatusMessage.setOrderId(orderId);
        orderStatusMessage.setPhone(customerPhoneNumber);
        orderStatusMessage.setName(customerName);
        String cartDetailJson = getCartDetailsJson(order);
        orderStatusMessage.setCustomParam(cartDetailJson);
        return orderStatusMessage;
    }

    public boolean publishCODEvent(Order order){
        boolean messagePublished = false;
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            orderStatusMessage.setOrderType(OrderType.COD);
            Long customerPhoneNumber =  0L;
            try{
                customerPhoneNumber = StringUtils.getUserPhone(order.getPayment().getContactNumber());
            }catch (NumberFormatException ex){
                logger.error(String.format("Wrong phone number for order %d",order.getId()));
            }
            orderStatusMessage.setName(order.getPayment().getContactName());
            orderStatusMessage.setPhone(customerPhoneNumber);
            Producer producer = null;

            if (codRoute != null && codRoute.equalsIgnoreCase("smsCountry")) {
                producer = producerFactory.getProducer(ProducerTypeEnum.COD_SMS_PRODUCER);
            } else {
                producer = producerFactory.getProducer(ProducerTypeEnum.COD_PRODUCER);
            }

            messagePublished = producer.publishMessage(orderStatusMessage);
            //userCallResponseObserver.subscribe();
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
        return messagePublished;
    }

    public boolean publishOrderPlacedEvent(final Order order){
        boolean messagePublished = false;
        try{
	        final Long userId = order.getUser().getId();
        	splitExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						final User loggedInUser = userService.getUserById(userId);
						logger.info("Splitting for logged in user " + loggedInUser.getId() + " for thread id " + Thread.currentThread().getId() + " name " + Thread.currentThread().getName());
						UserThreadLocal.set(loggedInUser);
						orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
					} catch (Throwable t) {
						logger.error("Error while Splitting the order with orderID: " + order.getId(), t);
						orderLoggingService.logOrderActivity(order, userService.getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), t.getMessage());
					} finally {
						UserThreadLocal.unset();
					}
				}
			});
        }catch (Exception ex){
            logger.error("SPLIT EVENT: Error while publishing event for Order " + order.getId(), ex );
        }
        return messagePublished;
    }

    public boolean publishPaymentFailureEvent(Order order) {
        boolean messagePublished = false;
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            orderStatusMessage.setOrderType(OrderType.PAYMENT_FAILURE);
            Producer producer = producerFactory.getProducer(ProducerTypeEnum.PAYMENT_FAILURE_PRODUCER);
            messagePublished =  producer.publishMessage(orderStatusMessage);
            //userCallResponseObserver.subscribe();
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
        return messagePublished;
    }

    public void publishPaymentSuccessEvent(Order order){
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            orderStatusMessage.setOrderType(OrderType.PAYMENT_SUCCESS);
            Producer producer = producerFactory.getProducer(ProducerTypeEnum.PAYMENT_SUCCESS_PRODUCER);
            boolean messagePublished = producer.publishMessage(orderStatusMessage);
            //userCallResponseObserver.subscribe();
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
    }

    public void publishCodStatus(Order order) {
        Producer producer = null;
        try {
            OrderStatusUpdate orderStatusUpdate = getOrderStatusUpdate(order);
            if (codRoute != null && codRoute.equalsIgnoreCase("smsCountry")) {
                producer = producerFactory.getProducer(ProducerTypeEnum.ORDER_STATUS_NOTIFY);
                producer.publishMessage(orderStatusUpdate);
            }
        } catch (Exception e) {
            logger.error("Error while publishing event for Order " + order.getId() );
        }
    }

    private OrderStatusUpdate getOrderStatusUpdate(Order order) {
        OrderStatusUpdate orderStatusUpdate = new OrderStatusUpdate();
        orderStatusUpdate.setOrderId(order.getId().toString());
        if (!EnumOrderStatus.Cancelled.getId().equals(order.getOrderStatus().getId())) {
            orderStatusUpdate.setOrderStatusType(OrderStatusUpdate.OrderStatusType.CONFIRMED);
        } else {
            orderStatusUpdate.setOrderStatusType(OrderStatusUpdate.OrderStatusType.CANCELED);
        }
        return orderStatusUpdate;
    }


}
