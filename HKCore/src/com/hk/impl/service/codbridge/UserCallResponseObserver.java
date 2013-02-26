package com.hk.impl.service.codbridge;


import com.hk.constants.core.Keys;
import com.hk.hkjunction.observers.OrderObserver;
import com.hk.hkjunction.observers.OrderResponse;

import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;

import com.hk.constants.core.EnumUserCodCalling;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jms.MessageListener;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 13, 2013
 * Time: 11:47:26 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class UserCallResponseObserver{
    private static Logger logger = LoggerFactory.getLogger(UserCallResponseObserver.class);

    @Value("#{hkEnvProps['" + Keys.Env.healthkartRestUrl + "']}")
    private String healthkartRestUrl;
    //Assumption is that this class is singleton which is default behavior of Spring Beans
    static boolean isSubscribed = false;

    @PostConstruct
    void init() {

    }

    @Transactional
    public void onResponse(OrderResponse orderResponse) {

        Order order;
        UserCodCall userCodCall;

        logger.info("response received for  " + orderResponse.getOrderId());
        try {

            Long orderId = Long.parseLong(orderResponse.getOrderId());
            String sourceOfMessage = orderResponse.getSource() + "CodCall";
            //order = orderService.find(orderId);

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
        } catch (Exception ex) {
            logger.error("Exception in Receiving Response " + ex.getMessage());
        }
    }
}

