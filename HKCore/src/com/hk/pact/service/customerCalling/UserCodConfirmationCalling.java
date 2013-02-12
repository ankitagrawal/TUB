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
		try {


		url = new URL("http://www.smartivr.in/api/voice/quickCall/?username=health_kart&password=123456&ivr_id=800059891&format=xml&phone_book='8860680752,Gayatri,5678,123'");
			//url = new URL("http://www.smartivr.in/api/voice/quickCall/?username=" + ivrUserName + "&password=" + ivrPassword + "&ivr_id=" + ivrId + "&format=xml&phone_book=`" + customerPhoneNumber + "," + orderId + "," + orderAmount + "'");
		bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
			UserCodCall userCodCall = new UserCodCall();
			userCodCall.setBasOrder(order);
			userCodCall.setRemark("knowlarity call started , wiating for response");
			userCodCall.setCallStatus(10L);
			userCodCall = orderService.saveUserCodCallStatus(userCodCall);
		} catch (MalformedURLException mfu) {
			//call outbound call
			logger.error("Invalid Url" + mfu.getMessage());
		}
		catch (IOException ioe) {
			logger.error("IO Exception" + ioe.getMessage());
		}
	 return;
	}

}
