package com.hk.pact.service.customerCalling;
import com.hk.hkjunction.observers.OrderResponseObserver;
import com.hk.hkjunction.observers.OrderResponse;
import com.hk.pact.service.order.OrderService;
import com.hk.domain.order.Order;
import com.hk.domain.user.UserCodCall;
import com.hk.constants.core.UserCodCallingConstants;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 13, 2013
 * Time: 11:47:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserCODConfirmationCallObserver implements OrderResponseObserver{

	@Autowired
	OrderService orderService;

	 public void onResponse(OrderResponse orderResponse){
		 String remark = "Response Recieved From ";
		 boolean error = false;
		 Order order = null;
		 Integer callStatus = null;
		 
		 OrderResponse.OrderStatus[] orderStatusArray = OrderResponse.OrderStatus.values();
		 String keyPressResponse = null;
		 if (orderStatusArray != null && orderStatusArray.length > 0) {
			 keyPressResponse = orderStatusArray[0].toString();
		 }
		 String orderId = orderResponse.getOrderId();
		 String sourceOfMessage = orderResponse.getSource();

		 if (orderId == null || orderId.isEmpty()) {
			 remark = remark + " Order ID null or Empty recieved ";
			 error = true;
		 }
		 else{
			order  =  orderService.find(Long.valueOf(orderId.trim()));
			 if(order == null){
		     remark = remark + "Invalid Order Id Received ";
			 error = true;
			 }
		 }
		 if (keyPressResponse == null || keyPressResponse.isEmpty()) {
			 remark = remark + "Key Press value received Empty ";
			 error = true;
		 }
		 else{
		   keyPressResponse = keyPressResponse.trim().toUpperCase();

			 if(keyPressResponse.equalsIgnoreCase(UserCodCallingConstants.CONFIRMED)){

			 }

			 else if(keyPressResponse.equalsIgnoreCase(UserCodCallingConstants.CONFIRMED)){

			 }


		 }

		 if (sourceOfMessage == null || sourceOfMessage.isEmpty()) {
			 remark = remark + "Source Of Message received Empty ";
			 error = true;
		 }
		 else{
			 remark = remark +sourceOfMessage;
		 }

		 if (!error) {
			 if (order.getUserCodCall() == null) {
				 UserCodCall userCodCall = orderService.createUserCodCall(order);
				 userCodCall.setRemark(remark);
				 userCodCall.setCallStatus();
			 }


		 }
		 



	 }
}
