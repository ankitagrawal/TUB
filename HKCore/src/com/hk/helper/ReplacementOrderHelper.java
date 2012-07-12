package com.hk.helper;

import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 10, 2012
 * Time: 11:39:29 AM
 * To change this template use File | Settings | File Templates.
 */


public class ReplacementOrderHelper {

    public static LineItem getLineItemForReplacementOrder(LineItem lineItem) {
        LineItem replacementOrderLineItem = new LineItem();
        replacementOrderLineItem.setSku(lineItem.getSku());
//        replacementOrderLineItem.setShippingOrder(lineItem.getShippingOrder());
        replacementOrderLineItem.setCartLineItem(lineItem.getCartLineItem());
//        replacementOrderLineItem.setQty(lineItem.getQty());
        replacementOrderLineItem.setCostPrice(lineItem.getCartLineItem().getProductVariant().getCostPrice());
        replacementOrderLineItem.setMarkedPrice(lineItem.getCartLineItem().getMarkedPrice());
        replacementOrderLineItem.setHkPrice(lineItem.getCartLineItem().getHkPrice());
        replacementOrderLineItem.setDiscountOnHkPrice(lineItem.getCartLineItem().getDiscountOnHkPrice());
        replacementOrderLineItem.setTax(lineItem.getSku().getTax());
        replacementOrderLineItem.setQty(lineItem.getQty());
        return replacementOrderLineItem;
    }

    public static ReplacementOrder getReplacementOrderFromShippingOrder(ShippingOrder shippingOrder,ShippingOrderStatusService shippingOrderStatusService, ReconciliationStatusDao reconciliationStatusDao) {
        ReplacementOrder replacementOrder = new ReplacementOrder();
        replacementOrder.setBaseOrder(shippingOrder.getBaseOrder());
        replacementOrder.setWarehouse(shippingOrder.getWarehouse());
        replacementOrder.setCancellationType(shippingOrder.getCancellationType());
        replacementOrder.setCancellationRemark(shippingOrder.getCancellationRemark());
        replacementOrder.setBasketCategory(shippingOrder.getBasketCategory());
        replacementOrder.setServiceOrder(shippingOrder.isServiceOrder());
        replacementOrder.setVersion(shippingOrder.getVersion());
        replacementOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ReadyForProcess));
        replacementOrder.setCreateDate(new Date());
        replacementOrder.setUpdateDate(new Date());
        replacementOrder.setAmount(0D);
        replacementOrder.setReconciliationStatus(reconciliationStatusDao.getReconciliationStatusById(EnumReconciliationStatus.PENDING));
        return replacementOrder;
    }
}
