package com.hk.admin.impl.service.shippingOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.pact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;

/**
 * Created by IntelliJ IDEA. User: user Date: Jul 5, 2012 Time: 5:44:02 PM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class ReplacementOrderServiceImpl implements ReplacementOrderService {
    @Autowired
    ShippingOrderService               shippingOrderService;
    @Autowired
    LineItemDao                        lineItemDao;
    @Autowired
    ReplacementOrderDao                replacementOrderDao;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private ReconciliationStatusDao    reconciliationStatusDao;
	@Autowired
	UserService                        userService;
    

    public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto, ReplacementOrderReason replacementOrderReason) {
        Set<LineItem> lineItemSet = new HashSet<LineItem>();
        ReplacementOrder replacementOrder = ReplacementOrderHelper.getReplacementOrderFromShippingOrder(shippingOrder, shippingOrderStatusService, reconciliationStatusDao);
        for (LineItem lineItem : lineItems) {
            if (lineItem.getQty() != 0) {
                // lineItem = lineItemDao.getLineItem(lineItem.getSku(), shippingOrder);
                // LineItem lineItemNew = ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem);
                lineItem.setShippingOrder(replacementOrder);
                if (!isRto) {
                    lineItem.setHkPrice(0.00);
                    lineItem.setCodCharges(0.00);
                    lineItem.setDiscountOnHkPrice(0.00);
                    lineItem.setOrderLevelDiscount(0.00);
                    lineItem.setShippingCharges(0.00);
                    lineItem.setRewardPoints(0.00);
                }
                lineItemSet.add(lineItem);
                // lineItem.setShippingOrder(replacementOrder);
                // lineItemDao.save(lineItemNew);
            }
        }
        replacementOrder.setLineItems(lineItemSet);
        replacementOrder.setAmount(ShippingOrderHelper.getAmountForSO(replacementOrder));
        replacementOrder.setRto(isRto);
	    replacementOrder.setReplacementOrderReason(replacementOrderReason);

        replacementOrder.setRefShippingOrder(shippingOrder);
        replacementOrder = (ReplacementOrder) getReplacementOrderDao().save(replacementOrder);
        shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(replacementOrder);
	    replacementOrder.getBaseOrder().setOrderStatus(EnumOrderStatus.InProcess.asOrderStatus());

	    replacementOrder = (ReplacementOrder)getReplacementOrderDao().save(replacementOrder);
	    shippingOrderService.logShippingOrderActivity(replacementOrder, userService.getAdminUser(),
				        EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(),
				        "Replacement order created");
        return replacementOrder;
    }

    @Override
    public List<ReplacementOrder> getReplacementOrderForRefShippingOrder(Long refShippingOrderId) {
        return getReplacementOrderDao().getReplacementOrderFromShippingOrder(refShippingOrderId);
    }

    public ReplacementOrderDao getReplacementOrderDao() {
        return replacementOrderDao;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public ReconciliationStatusDao getReconciliationStatusDao() {
        return reconciliationStatusDao;
    }

    public void setReconciliationStatusDao(ReconciliationStatusDao reconciliationStatusDao) {
        this.reconciliationStatusDao = reconciliationStatusDao;
    }

    
    
    
}
