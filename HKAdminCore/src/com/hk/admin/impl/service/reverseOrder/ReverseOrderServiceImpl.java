package com.hk.admin.impl.service.reverseOrder;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.courier.ReverseOrderDao;
import com.hk.pact.service.UserService;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.akube.framework.dao.Page;

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

	@Autowired
	ReverseOrderDao reverseOrderDao;

	public ReverseOrder createReverseOrder (ShippingOrder shippingOrder, String returnOrderReason, String reverseOrderType){
		User loggedOnUser = userService.getLoggedInUser();
		ReverseOrder reverseOrder = new ReverseOrder();
		reverseOrder.setShippingOrder(shippingOrder);
		reverseOrder.setCourierPickupDetail(null);
		reverseOrder.setAmount(0.0);
		reverseOrder.setReturnReason(returnOrderReason);
		reverseOrder.setReverseOrderType(reverseOrderType);
		reverseOrder.setUser(loggedOnUser);
		reverseOrder.setReconciliationStatus(EnumReconciliationStatus.PENDING.asReconciliationStatus());
		reverseOrder.setActionProposed(null);
		return save(reverseOrder);
	}

	public void createReverseLineItems(ReverseOrder reverseOrder, Map<LineItem, Long> itemMap){
		Iterator itemIterator = itemMap.keySet().iterator();
		Long returnQty;
		Set<ReverseLineItem> reverseLineItemSet = new HashSet<ReverseLineItem>();
		while(itemIterator.hasNext()){
			LineItem item = (LineItem)itemIterator.next();
			returnQty = itemMap.get(item);
			if (returnQty > 0) {
				ReverseLineItem reverseLineItem = new ReverseLineItem();
				reverseLineItem.setReferredLineItem(item);
				reverseLineItem.setReturnQty(returnQty);
				reverseLineItem.setReverseOrder(reverseOrder);
				reverseLineItem.setCreateDate(new Date());
				reverseLineItem = (ReverseLineItem) getBaseDao().save(reverseLineItem);
				reverseLineItemSet.add(reverseLineItem);
			}
		}

		if (reverseLineItemSet.size() != 0) {
			reverseOrder.setReverseLineItems(reverseLineItemSet);
			Double amount = getAmountForReverseOrder(reverseOrder);
			reverseOrder.setAmount(amount);
			getReverseOrderDao().save(reverseOrder);
		}
	}

	public void setCourierDetails(ReverseOrder reverseOrder, CourierPickupDetail courierPickupDetail){
		reverseOrder.setCourierPickupDetail(courierPickupDetail);
		getReverseOrderDao().save(reverseOrder);
	}

	public double getAmountForReverseOrder(ReverseOrder reverseOrder) {
        double rvoBaseAmt = 0.0;
        for (ReverseLineItem reverselineItem : reverseOrder.getReverseLineItems()) {
			LineItem lineItem = reverselineItem.getReferredLineItem();
            double lineItemAmount = lineItem.getHkPrice() * reverselineItem.getReturnQty();
            double totalDiscountOnLineItem = lineItem.getDiscountOnHkPrice() + lineItem.getOrderLevelDiscount() + lineItem.getRewardPoints();
            double forwardingCharges = lineItem.getShippingCharges() + lineItem.getCodCharges(); // check this calculation
			//double forwardingCharges = 0.0;
            rvoBaseAmt += (lineItemAmount - totalDiscountOnLineItem + forwardingCharges);
        }
        return rvoBaseAmt;
    }
	
	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, Long courierId, int page, int perPage){
        return getReverseOrderDao().getPickupRequestsByStatuses(shippingOrderId, pickupStatusId, reconciliationStatusId, courierId, page, perPage);
    }

	public ReverseOrder save(ReverseOrder reverseOrder){
		 return getReverseOrderDao().save(reverseOrder);
	}

	public ReverseOrder getReverseOrderById(Long id){
		return getReverseOrderDao().getReverseOrderById(id);
	}

	public Page getReverseOrderWithNoPickupSchedule( int page, int perPage){
		return getReverseOrderDao().getReverseOrderWithNoPickupSchedule(page, perPage);
	}

	public ReverseOrder getReverseOrderByShippingOrderId(Long shippingOrderId){
		return getReverseOrderDao().getReverseOrderByShippingOrderId(shippingOrderId);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public ReverseOrderDao getReverseOrderDao() {
		return reverseOrderDao;
	}
}
