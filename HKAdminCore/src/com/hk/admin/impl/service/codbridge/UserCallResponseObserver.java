package com.hk.admin.impl.service.codbridge;


import com.hk.hkjunction.observers.OrderObserver;
import com.hk.hkjunction.observers.OrderResponseObserver;
import com.hk.hkjunction.observers.OrderResponse;

import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.UserService;

import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;

import com.hk.constants.core.EnumCancellationType;
import com.hk.constants.core.EnumUserCodCalling;

import com.hk.admin.pact.service.order.AdminOrderService;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hk.pact.service.codbridge.OrderEventPublisher;

import javax.annotation.PostConstruct;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 13, 2013
 * Time: 11:47:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserCallResponseObserver extends OrderObserver {
	private static Logger logger = LoggerFactory.getLogger(UserCallResponseObserver.class);

	@Autowired
	OrderService orderService;
	@Autowired
	AdminOrderService adminOrderService;
	@Autowired
    OrderEventPublisher userCodConfirmationCalling;
	@Autowired
	UserService userService;
    @Autowired
    ProducerFactory producerFactory;

    @PostConstruct
    void init(){
        logger.info("Initializing User Call Observer..");
        //Start listening to events
        producerFactory.register(this);
    }

    @Transactional
    @Override
	public void onResponse(OrderResponse orderResponse) {

		Order order;
		UserCodCall userCodCall;

        logger.info("response received for  " +orderResponse.getOrderId()) ;
		try {
            int keyPressResponse = orderResponse.getOrderStatus().ordinal();
            String orderId = orderResponse.getOrderId();
            String sourceOfMessage = orderResponse.getSource();
			order = orderService.find(Long.valueOf(orderId.trim()));
			if (order != null) {
				if (order.getUserCodCall() == null) {
					userCodCall = orderService.createUserCodCall(order,EnumUserCodCalling.PENDING_WITH_THIRD_PARTY);
					userCodCall.setRemark(sourceOfMessage);

				} else {
					userCodCall = order.getUserCodCall();
				}
				int cancelled = OrderResponse.OrderStatus.CANCELLED.ordinal();
				int confirmed = OrderResponse.OrderStatus.CONFIRMED.ordinal();

				if (keyPressResponse == confirmed) {
					userCodCall.setCallStatus(EnumUserCodCalling.CONFIRMED.getId());
					userCodCall.setRemark(EnumUserCodCalling.CONFIRMED.getName());
					orderService.saveUserCodCall(userCodCall);
                    String comment = "Order Confirmed By " +orderResponse.getSource();
					adminOrderService.confirmCodOrder(order ,comment);

				} else if (keyPressResponse == cancelled) {
					String cancellationRemark = sourceOfMessage + "called User for COD confirmation , User Request for cancel";
					adminOrderService.cancelOrder(order, EnumCancellationType.Customer_Not_Interested.asCancellationType(), cancellationRemark, userService.getAdminUser());
					userCodCall.setCallStatus(EnumUserCodCalling.CANCELLED.getId());
					userCodCall.setRemark(EnumUserCodCalling.CANCELLED.getName());
					orderService.saveUserCodCall(userCodCall);
				}

			} else {
				logger.error("Invalid Order ID Returned From JMS" +orderId);
			}
		} catch (Exception ex) {
			logger.error("Exception in Receiving Response " + ex.getMessage());
		}
	}
}

