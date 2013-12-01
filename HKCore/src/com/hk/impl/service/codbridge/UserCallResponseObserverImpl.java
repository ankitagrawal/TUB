package com.hk.impl.service.codbridge;


import com.google.gson.Gson;
import com.hk.constants.core.Keys;
import com.hk.hkjunction.observers.OrderObserver;
import com.hk.hkjunction.observers.OrderResponse;

import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;

import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.pact.service.order.OrderService;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 13, 2013
 * Time: 11:47:26 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class UserCallResponseObserverImpl implements javax.jms.MessageListener/*,com.hk.pact.service.codbridge.UserCallResponseObserver*/{
    private static Logger logger = LoggerFactory.getLogger(UserCallResponseObserverImpl.class);

    @Value("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
    private String healthkartRestUrl;
    @Autowired
    ProducerFactory producerFactory;
    @Autowired
    OrderService orderService;
    //Assumption is that this class is singleton which is default behavior of Spring Beans
    static boolean isSubscribed = false;

    @PostConstruct
    void init() {

    }



    /*public void subscribe(){
       //producerFactory.register(this);
    }*/

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                String messageText = ((TextMessage) message).getText();
                logger.debug("recieved message in Order Observer: " + messageText);
                //OrderResponseObserver codObserver = beanFactory.getBean(OrderResponseObserver.class);
                OrderResponse orderResponse = new Gson().fromJson(messageText, OrderResponse.class);
                onResponse(orderResponse);
            }
            catch (JMSException ex) {
                logger.error("JMS Exception ", ex);
            }
        }
    }

    @Transactional
    public void onResponse(OrderResponse orderResponse) {

        Order order;
        UserCodCall userCodCall;

        logger.info("response received for  " + orderResponse.getOrderId());
        try {

            Long orderId = Long.parseLong(orderResponse.getOrderId());
            String sourceOfMessage = orderResponse.getSource();
            order = orderService.find(orderId);
            if (order != null) {
                String urlStr = String.format(healthkartRestUrl + "user/order/source/%s/order/%d/action/%s", sourceOfMessage, orderId, orderResponse.getOrderStatus().name());
                ClientRequest request = new ClientRequest(urlStr);
                request.getQueryParameters().add("authToken", "US3jbSEN5EKVVzlabDl95loyWf_hloCZ");
                request.setHttpMethod("POST");
                ClientResponse<String> response = request.post();
                int status = response.getStatus();
                logger.info("Calling Post API " + urlStr);
                if (status == 200) {
                    logger.info("Post API returned correct status");
                } else {
                    logger.error("Unable to update order status.." + Integer.toString(status));
                }
            } else {
                logger.error("Invalid Order Id Recieved From Third Party for COD confirmation.." + orderId);
            }
        } catch (Exception ex) {
            logger.error("Exception in Receiving Response " + ex.getMessage());
        }

    }
}

