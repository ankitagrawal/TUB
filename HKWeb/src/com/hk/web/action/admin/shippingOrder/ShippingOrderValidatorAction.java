package com.hk.web.action.admin.shippingOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;

@Component
public class ShippingOrderValidatorAction extends BaseAction{
	
	private static Logger logger = LoggerFactory.getLogger(ShippingOrderValidatorAction.class);
	
	@Autowired
	ShippingOrderService shippingOrderService; 
	
	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		
		shippingOrders = getSortedShippingOrders(shippingOrders);
		Set<ShippingOrder> sortedShippingOrdersSet = new HashSet<ShippingOrder>(shippingOrders);
		
		for(ShippingOrder shippingOrder : sortedShippingOrdersSet){
			logger.debug("Validating Shipping Order -"+shippingOrder.getId());
			try{
				shippingOrderService.validateShippingOrder(shippingOrder);
			}
			catch(Exception e)
			{
				logger.debug("Exception while validating the Shipping Order"+shippingOrder.getId()+" "+e.getMessage());
				e.printStackTrace();
			}
			
		}
		addRedirectAlertMessage(new SimpleMessage("Shipping Orders Validated and entries in tables adjusted"));
		return new RedirectResolution(ActionAwaitingQueueAction.class); 
		
		
	}
	
	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
		soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_ReadyForDropShipping.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus());
		soStatusList.add(EnumShippingOrderStatus.SO_EscalatedBack.asShippingOrderStatus());
		shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
		List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
		paymentStatusList.add(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.AUTHORIZATION_PENDING.asPaymenStatus());
		shippingOrderSearchCriteria.setPaymentStatuses(paymentStatusList);
		return shippingOrderSearchCriteria;
	}
	
	public class ShippingOrderComparator implements Comparator<ShippingOrder> {

		@Override
		public int compare(ShippingOrder o1, ShippingOrder o2) {
			if (o1.getCreateDate() != null && o2.getCreateDate() != null) {
				return o1.getCreateDate().compareTo(o2.getCreateDate());
			}
			return -1;
		}

	}
	
	public List<ShippingOrder> getSortedShippingOrders(List<ShippingOrder> shippingOrders) {
		Collections.sort(shippingOrders, new ShippingOrderComparator());
		return shippingOrders;
	}

}
