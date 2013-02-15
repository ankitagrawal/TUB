package com.hk.pact.service.codbridge;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hk.hkjunction.observers.OrderStatusMessage;
import com.hk.hkjunction.producer.Producer;
import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.hkjunction.producer.ProducerTypeEnum;
import com.hk.domain.order.Order;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 15, 2013
 * Time: 2:19:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PaymentSucessListenerApiCall {
	private static Logger logger = LoggerFactory.getLogger(PaymentSucessListenerApiCall.class);
	@Autowired
	ProducerFactory producerFactory;

	public void notifyToPaymentSuccessListener(Order order) {
		try {
			OrderStatusMessage orderStatusMessage = new OrderStatusMessage();
			orderStatusMessage.setOrderId(order.getId().toString());
			Producer producer = producerFactory.getProducer(ProducerTypeEnum.PAYMENT_SUCCESS_PRODUCER);
			producer.publishMessage(orderStatusMessage);
		} catch (Exception ex) {
			logger.error("Eroor in Notigying JMS for Payment Sucess for Order" + order.getId());
		}
	}
}
