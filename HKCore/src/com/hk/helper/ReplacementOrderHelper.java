package com.hk.helper;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 10, 2012
 * Time: 11:39:29 AM
 * To change this template use File | Settings | File Templates.
 */


public class ReplacementOrderHelper {

    public static LineItem getLineItemForReplacementOrder(LineItem lineItem, Long qty, boolean freeOrder) {
        LineItem replacementOrderLineItem = new LineItem();
        replacementOrderLineItem.setSku(lineItem.getSku());
        replacementOrderLineItem.setCartLineItem(lineItem.getCartLineItem());
        replacementOrderLineItem.setCostPrice(lineItem.getCostPrice());
        replacementOrderLineItem.setMarkedPrice(lineItem.getSku().getProductVariant().getMarkedPrice());
        replacementOrderLineItem.setHkPrice(lineItem.getHkPrice());

        replacementOrderLineItem.setTax(lineItem.getTax());
        replacementOrderLineItem.setQty(qty);
        double factor = freeOrder ? 0 : qty / lineItem.getQty();

        if (freeOrder) {
            replacementOrderLineItem.setDiscountOnHkPrice(replacementOrderLineItem.getHkPrice());
        } else {
            replacementOrderLineItem.setDiscountOnHkPrice(lineItem.getDiscountOnHkPrice() * factor);
        }

        replacementOrderLineItem.setRewardPoints(lineItem.getRewardPoints() * factor);
        replacementOrderLineItem.setOrderLevelDiscount(lineItem.getOrderLevelDiscount() * factor);
        replacementOrderLineItem.setCodCharges(lineItem.getCodCharges() * factor);
        replacementOrderLineItem.setShippingCharges(lineItem.getShippingCharges() * factor);

        return replacementOrderLineItem;
    }

    public static ReplacementOrder getReplacementOrderFromShippingOrder(ShippingOrder shippingOrder, ShippingOrderStatusService shippingOrderStatusService, ReconciliationStatusDao reconciliationStatusDao) {
        ReplacementOrder replacementOrder = new ReplacementOrder();
        replacementOrder.setBaseOrder(shippingOrder.getBaseOrder());
        replacementOrder.setWarehouse(shippingOrder.getWarehouse());
        replacementOrder.setBasketCategory(shippingOrder.getBasketCategory());
        replacementOrder.setServiceOrder(shippingOrder.isServiceOrder());
        replacementOrder.setVersion(shippingOrder.getVersion());
        replacementOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
        replacementOrder.setCreateDate(new Date());
        //replacementOrder.setUpdateDate(new Date());
        replacementOrder.setAmount(0D);
        replacementOrder.setReconciliationStatus(reconciliationStatusDao.getReconciliationStatusById(EnumReconciliationStatus.PENDING));
        return replacementOrder;
    }
}
