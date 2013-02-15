package com.hk.pact.service.codbridge;

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
public class CodConfirmationOrPaymentFailureCalling {

	@Autowired
	ProducerFactory producerFactory;

	private static Logger logger = LoggerFactory.getLogger(CodConfirmationOrPaymentFailureCalling.class);

	public void triggerAutomaticCodCustomerCalling(Order order ,ProducerTypeEnum producerTypeEnum) throws Exception{
		try{
		String customerPhoneNumber = order.getAddress().getPhone();
		String orderId = order.getId().toString();
		String orderAmount = order.getAmount().toString();
		String customerName = order.getUser().getName();
		String email =  order.getUser().getLogin();
		OrderStatusMessage orderStatusMessage = new OrderStatusMessage();
		orderStatusMessage.setAmount(Double.valueOf(orderAmount));
		orderStatusMessage.setEmail(email);
		orderStatusMessage.setOrderId(orderId);
		orderStatusMessage.setPhone(Long.valueOf(customerPhoneNumber));
		orderStatusMessage.setName(customerName);
		Producer producer = producerFactory.getProducer(producerTypeEnum);
		producer.publishMessage(orderStatusMessage);
		}catch (Exception ex){
			logger.error("Error In Triggering calling JMS for Order " + order.getId() );
		}

	}

}
