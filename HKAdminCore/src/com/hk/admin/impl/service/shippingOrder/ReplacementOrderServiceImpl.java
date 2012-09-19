package com.hk.admin.impl.service.shippingOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
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
    BaseDao                            baseDao;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private ReconciliationStatusDao    reconciliationStatusDao;

    public void createReplaceMentOrder(ShippingOrder shippingOrder, List<LineItem> lineItems, Boolean isRto) {
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

        replacementOrder.setRefShippingOrder(shippingOrder);
        replacementOrder = (ReplacementOrder) getBaseDao().save(replacementOrder);
        ShippingOrderHelper.setGatewayIdAndTargetDateOnShippingOrder(replacementOrder);
        getBaseDao().saveOrUpdate(replacementOrder);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public ReconciliationStatusDao getReconciliationStatusDao() {
        return reconciliationStatusDao;
    }

    public void setReconciliationStatusDao(ReconciliationStatusDao reconciliationStatusDao) {
        this.reconciliationStatusDao = reconciliationStatusDao;
    }
}
