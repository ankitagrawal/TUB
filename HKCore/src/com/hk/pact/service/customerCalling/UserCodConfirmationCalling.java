package com.hk.pact.service.customerCalling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.net.URL;
import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;
import com.hk.pact.service.order.OrderService;

import com.hk.hkjunction.producer.ProducerFactory;
import com.hk.hkjunction.producer.ProducerTypeEnum;
import com.hk.hkjunction.producer.Producer;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 2:18:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserCodConfirmationCalling {

	@Autowired
	OrderService orderService;
	@Autowired
	ProducerFactory producerFactory;

	private static Logger logger = LoggerFactory.getLogger(UserCodConfirmationCalling.class);

	private BufferedReader bufferedReader = null;
	private URL url = null;
	private UserCodCall userCodCall;

	private static final String ivrUserName = "health_kart";
	private static final String ivrPassword = "123456";
	private static final String ivrId = "800059891";


	public void triggerAutomaticCodCustomerCalling(Order order) throws Exception{
		String customerPhoneNumber = order.getAddress().getPhone();
		String orderId = order.getId().toString();
		String orderAmount = order.getAmount().toString();
		String inputLine = "";
		String response = "";
		String messageToPublish = orderId + "," ;
		Producer producer = producerFactory.getProducer(ProducerTypeEnum.COD_PRODUCER);
		producer.publishMessage(messageToPublish);

	}

}
