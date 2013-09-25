package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: user Date: Jul 5, 2012 Time: 5:44:02 PM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class ReplacementOrderServiceImpl implements ReplacementOrderService {
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    OrderService orderService;
    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    ReplacementOrderDao replacementOrderDao;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private ReconciliationStatusDao reconciliationStatusDao;
    @Autowired
    UserService userService;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    InventoryService inventoryService;


    public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto, ReplacementOrderReason replacementOrderReason,
                                                   String roComment) {
        Set<LineItem> lineItemSet = new HashSet<LineItem>();
        User loggedOnUser = userService.getLoggedInUser();
        if (roComment == null) roComment = "";

        ReplacementOrder replacementOrder = ReplacementOrderHelper.getReplacementOrderFromShippingOrder(shippingOrder, shippingOrderStatusService, reconciliationStatusDao);
        for (LineItem lineItem : lineItems) {
            if (lineItem.getRQty() != 0) {
                LineItem rLineItem = ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem, lineItem.getRQty());
                lineItemSet.add(rLineItem);
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

        replacementOrder = (ReplacementOrder) getReplacementOrderDao().save(replacementOrder);
        String comment = "Replacement order created for shipping order: " + shippingOrder.getGatewayOrderId() + " .Status of old shipping order: " + shippingOrder.getOrderStatus().getName() + ". Special comment: " + roComment;
        shippingOrderService.logShippingOrderActivity(replacementOrder, loggedOnUser, EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, comment);
        String comment2 = "Replacement order created. Gateway order Id of replacement order: " + replacementOrder.getGatewayOrderId();
        shippingOrderService.logShippingOrderActivity(shippingOrder, loggedOnUser, EnumShippingOrderLifecycleActivity.RO_Created.asShippingOrderLifecycleActivity(), null, comment2);

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
