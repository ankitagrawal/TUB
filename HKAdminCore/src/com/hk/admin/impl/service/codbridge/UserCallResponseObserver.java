package com.hk.admin.impl.service.codbridge;


import com.hk.constants.core.Keys;
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

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
    private String healthkartRestUrl;

    @PostConstruct
    void init() {
        producerFactory.register(this);
    }

    @Transactional
    public void onResponse(OrderResponse orderResponse) {

        Order order;
        UserCodCall userCodCall;

        logger.info("response received for  " + orderResponse.getOrderId());
        try {

            Long orderId = Long.parseLong(orderResponse.getOrderId());
            String sourceOfMessage = orderResponse.getSource() + " COD ,Requested By User";
            order = orderService.find(orderId);
            if (order.getUserCodCall() == null) {
                userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.PENDING_WITH_THIRD_PARTY);
                userCodCall.setRemark(sourceOfMessage);

            } else {
                userCodCall = order.getUserCodCall();
            }
            String urlStr = String.format(healthkartRestUrl + "user/order/source/%s/order/%d/action/%s", sourceOfMessage, orderId, orderResponse.getOrderStatus().name());
            ClientRequest request = new ClientRequest(urlStr);
            request.getQueryParameters().add("authToken", "US3jbSEN5EKVVzlabDl95loyWf_hloCZ");
            request.setHttpMethod("POST");
            ClientResponse<String> response = request.post();
            int status = response.getStatus();
            logger.info("Calling Post API " + urlStr);
            if (status == 200) {
                logger.info("Post API returned correct status");
                userCodCall.setCallStatus(EnumUserCodCalling.valueOf(orderResponse.getOrderStatus().name()).getId());
                userCodCall.setRemark(orderResponse.getOrderStatus().name() + " Request Successful");
                orderService.saveUserCodCall(userCodCall);
            } else {
                logger.error("Unable to update order status.." + Integer.toString(status));
                userCodCall.setRemark(orderResponse.getOrderStatus().name() + "Request From Admin Failed..");
                orderService.saveUserCodCall(userCodCall);
            }
        } catch (Exception ex) {
            logger.error("Exception in Receiving Response " + ex.getMessage());
        }
    }
}

