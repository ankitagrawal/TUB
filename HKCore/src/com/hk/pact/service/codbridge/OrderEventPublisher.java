package com.hk.pact.service.codbridge;

import com.akube.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.order.Order;


import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.hkjunction.producer.ProducerTypeEnum;
import com.hk.hkjunction.producer.Producer;
import com.hk.hkjunction.observers.OrderStatusMessage;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 2:18:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OrderEventPublisher {

    @Autowired
    ProducerFactory producerFactory;

    private static Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);


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
        return orderStatusMessage;
    }

    public void publishCODEvent(Order order) throws Exception{
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            Producer producer = producerFactory.getProducer(ProducerTypeEnum.COD_PRODUCER);
            producer.publishMessage(orderStatusMessage);
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
    }

    public void publishPaymentFailureEvent(Order order) throws Exception{
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            Producer producer = producerFactory.getProducer(ProducerTypeEnum.PAYMENT_FAILURE_PRODUCER);
            producer.publishMessage(orderStatusMessage);
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
    }

    public void publishPaymentSuccessEvent(Order order) throws Exception{
        try{
            OrderStatusMessage orderStatusMessage = getOrderMessage(order);
            Producer producer = producerFactory.getProducer(ProducerTypeEnum.PAYMENT_SUCCESS_PRODUCER);
            producer.publishMessage(orderStatusMessage);
        }catch (Exception ex){
            logger.error("Error while publishing event for Order " + order.getId() );
        }
    }
}
