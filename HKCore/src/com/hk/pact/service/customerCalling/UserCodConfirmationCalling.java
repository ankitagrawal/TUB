package com.hk.pact.service.customerCalling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;
import com.hk.pact.service.order.OrderService;
import com.hk.producer.ProducerFactory;
import com.hk.hkjunction.observers.CODOrderObserver;


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


	public void triggerAutomaticCodCustomerCalling(Order order) {
		String customerPhoneNumber = order.getAddress().getPhone();
		String orderId = order.getId().toString();
		String orderAmount = order.getAmount().toString();
		String inputLine = "";
		String response = "";
		producerFactory.postCODMessage("test");
//		try {
////calkl jms
//			UserCodCall userCodCall = new UserCodCall();
//			userCodCall.setBasOrder(order);
//			userCodCall.setRemark("knowlarity call started , wiating for response");
//			userCodCall.setCallStatus(10L);
//			userCodCall = orderService.saveUserCodCallStatus(userCodCall);
//		} catch (MalformedURLException mfu) {
//			//call outbound call
//			logger.error("Invalid Url" + mfu.getMessage());
//		}
//		catch (IOException ioe) {
//			logger.error("IO Exception" + ioe.getMessage());
//		}
	 return;
	}




	void onCODResponse(String s){

	}

}
