package com.hk.admin.impl.service.shippingOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.reverseOrder.ReverseOrder;
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
    OrderService orderService;
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
	@Autowired
	ShipmentService shipmentService;
	@Autowired
	ReverseOrderService reverseOrderService;
	@Autowired
	InventoryService inventoryService;
    

    public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto, ReplacementOrderReason replacementOrderReason,
                                                   String roComment) {
        Set<LineItem> lineItemSet = new HashSet<LineItem>();
        //User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        User loggedOnUser = userService.getLoggedInUser();
	    if(roComment == null){
		    roComment = "";
	    }
        ReplacementOrder replacementOrder = ReplacementOrderHelper.getReplacementOrderFromShippingOrder(shippingOrder, shippingOrderStatusService, reconciliationStatusDao);
        for (LineItem lineItem : lineItems) {
            if (lineItem.getQty() != 0) {
                // lineItem = lineItemDao.getLineItem(lineItem.getSku(), shippingOrder);
                // LineItem lineItemNew = ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem);
                lineItem.setShippingOrder(replacementOrder);
				/*
				This case is to replace for customer returns, a reverse order is created first and then the replacement order.
				 */
                if (!isRto) {
                    //lineItem.setHkPrice(0.00);
                    //lineItem.setCodCharges(0.00);
                    //lineItem.setDiscountOnHkPrice(0.00);
                    //lineItem.setOrderLevelDiscount(0.00);
                    //lineItem.setShippingCharges(0.00);
                    //lineItem.setRewardPoints(0.00);
                }
                
                lineItem.setMarkedPrice(lineItem.getCartLineItem().getProductVariant().getMarkedPrice());
                lineItemDao.save(lineItem);
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
        replacementOrder.setShippingOrderCategories(orderService.getCategoriesForShippingOrder(replacementOrder));
        shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(replacementOrder);
	    replacementOrder.getBaseOrder().setOrderStatus(EnumOrderStatus.InProcess.asOrderStatus());
		
		if (!isRto){
			replacementOrder.setReverseOrder(reverseOrderService.getReverseOrderByShippingOrderId(replacementOrder.getRefShippingOrder().getId()));
		}
	    replacementOrder = (ReplacementOrder)getReplacementOrderDao().save(replacementOrder);
	    shippingOrderService.logShippingOrderActivity(replacementOrder, loggedOnUser,
				        EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(),
                null, "Replacement order created for shipping order: "+shippingOrder.getGatewayOrderId()+" .Status of old shipping order: "+shippingOrder.getOrderStatus().getName() +
                        ". Special comment: "+roComment);

	    shippingOrderService.logShippingOrderActivity(shippingOrder, loggedOnUser,
			    EnumShippingOrderLifecycleActivity.RO_Created.asShippingOrderLifecycleActivity(),
                null, "Replacement order created. Gateway order Id of replacement order: "+replacementOrder.getGatewayOrderId());


        orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(replacementOrder.getBaseOrder());

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
