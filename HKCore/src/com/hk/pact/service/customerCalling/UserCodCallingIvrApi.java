package com.hk.pact.service.customerCalling;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.hk.domain.user.UserCodCall;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.OrderService;
import com.hk.constants.core.UserCodCallingConstants;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 7, 2013
 * Time: 4:09:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/ivrcod")
@Component
public class UserCodCallingIvrApi {
	private static Logger logger = LoggerFactory.getLogger(OrderService.class);
	@Autowired
	OrderService orderService;

	@Path("/")
	public void getCallStatus(@QueryParam("orderId") String orderID, @QueryParam("keypress") String keypress) {
		String remark = "";
		String callStatus = "";
		UserCodCall userCodCall ;
		try {
			if (keypress == null || orderID == null) {
				remark = "Null Values comes from IVR side";
				callStatus = UserCodCallingConstants.NULL_ERROR;
			}
			if (keypress.equalsIgnoreCase(UserCodCallingConstants.OK)) {
				callStatus = UserCodCallingConstants.OK;
			} else {
				callStatus = UserCodCallingConstants.USER_ERROR;
				remark = "Call Failed , get reason for it";
			}

			Order order = orderService.find(Long.getLong(orderID));
			if (order == null) {
				logger.error("Invalid Order ID returning from IVR USER COD AUTOMATIC CALL Api call for ");
			} else {
				userCodCall = order.getUserCodCall();
				userCodCall.setCallStatus(0);
				userCodCall.setRemark(remark);
				orderService.saveUserCodCall(userCodCall);

			}
		} catch (Exception ex) {
			logger.error("UserCodCallingApi  Exception" + ex.getMessage());

		}
	}



}
