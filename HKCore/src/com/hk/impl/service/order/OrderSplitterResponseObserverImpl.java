package com.hk.impl.service.order;

import com.google.gson.Gson;
import com.hk.domain.order.Order;
import com.hk.hkjunction.observers.OrderSplitterMessage;
import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.pact.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/*
 * User: Pratham
 * Date: 06/05/13  Time: 22:25
*/
@Service
public class OrderSplitterResponseObserverImpl implements javax.jms.MessageListener {

    private static Logger logger = LoggerFactory.getLogger(OrderSplitterResponseObserverImpl.class);

    @Autowired
    ProducerFactory producerFactory;
    @Autowired
    OrderService orderService;

    @PostConstruct
    void init() {
    }

    @Override
    @Transactional
    public void onMessage(javax.jms.Message message) {
        if (message instanceof TextMessage) {
            try {
                String orderSplitterMessageString = ((TextMessage) message).getText();
                OrderSplitterMessage orderResponse = new Gson().fromJson(orderSplitterMessageString, OrderSplitterMessage.class);
                Long orderId = Long.valueOf(orderResponse.getOrderId());
                Order order = orderService.find(orderId);
                if (order != null) {
                    logger.info("order id " + order.getId() + "order status is " + order.getOrderStatus().getId());
                    orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
                }
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }
}
