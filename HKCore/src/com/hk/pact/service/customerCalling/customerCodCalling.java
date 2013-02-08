package com.hk.pact.service.customerCalling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.MalformedURLException;

import com.hk.domain.order.Order;
import com.hk.constants.core.CustomerCodCallingConstant;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 2:18:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerCodCalling {

	private static Logger logger = LoggerFactory.getLogger(CustomerCodCalling.class);

	private BufferedReader bufferedReader = null;
	private URL url = null;

	private static final String ivrUserName = "";
	private static final String ivrPassword = "";
	private static final String ivrId = "";


	public void triggerAutomaticCodCustomerCalling(Order order) {
		String customerPhoneNumber = order.getAddress().getPhone();
		String orderId = order.getId().toString();
		String orderAmount = order.getAmount().toString();
		String inputLine = "";
        String response = "";
		try {
			url = new URL("http://www.smartivr.in/api/voice/quickCall/?username=" + ivrUserName + "&password=" + ivrPassword + "&ivr_id=" + ivrId + "&format=xml&phone_book=`" + customerPhoneNumber + "," + orderId + "," + orderAmount + "'");
			bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));	} catch (MalformedURLException mfu) {
			logger.error("Invalid Url" + mfu.getMessage());
		}
		catch (IOException ioe) {
			logger.error("IO Exception" + ioe.getMessage());
		}
		
	}
}
