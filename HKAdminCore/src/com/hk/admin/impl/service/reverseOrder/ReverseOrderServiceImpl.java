package com.hk.admin.impl.service.reverseOrder;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.inventory.EnumReconciliationStatus;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 8, 2013
 * Time: 11:33:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReverseOrderServiceImpl implements ReverseOrderService {

	@Autowired
	UserService userService;

	@Autowired
	BaseDao baseDao;

	public ReverseOrder createReverseOrder (ShippingOrder shippingOrder){
		User loggedOnUser = userService.getLoggedInUser();
		Double amount = 140.0;
		ReverseOrder reverseOrder = new ReverseOrder();
		reverseOrder.setShippingOrder(shippingOrder);
		reverseOrder.setCourierPickupDetail(null);
		reverseOrder.setAmount(amount);
		reverseOrder.setUser(loggedOnUser);
		reverseOrder.setReconciliationStatus(EnumReconciliationStatus.PENDING.asReconciliationStatus());
		reverseOrder.setActionProposed(null);
		return save(reverseOrder);
	}

	public void createReverseLineItems(ReverseOrder reverseOrder, Map<LineItem, Long> itemMap){
		Iterator itemIterator = itemMap.keySet().iterator();
		while(itemIterator.hasNext()){
			LineItem item = (LineItem)itemIterator.next();
			ReverseLineItem reverseLineItem = new ReverseLineItem();
			reverseLineItem.setReferredLineItem(item);
			reverseLineItem.setReturnQty(itemMap.get(item));
			reverseLineItem.setReverseOrder(reverseOrder);
			reverseLineItem.setCreateDate(new Date());
			getBaseDao().save(reverseLineItem);
		}
	}

	public void setCourierDetails(ReverseOrder reverseOrder, CourierPickupDetail courierPickupDetail){
		reverseOrder.setCourierPickupDetail(courierPickupDetail);
		getBaseDao().save(reverseOrder);
	}

	public ReverseOrder save(ReverseOrder reverseOrder){
		 return (ReverseOrder) getBaseDao().save(reverseOrder);
	}

	public ReverseOrder getReverseOrderById(Long id){
		return (ReverseOrder) getBaseDao().findUniqueByNamedQueryAndNamedParam("getReverseOrderById", new String[]{"reverseOrderId"}, new Object[]{id});
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
}
