package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.helper.ShippingOrderHelper;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 5, 2012
 * Time: 5:44:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReplacementOrderServiceImpl implements ReplacementOrderService{
  @Autowired
  ShippingOrderService shippingOrderService;
  @Autowired
  LineItemDao lineItemDao;
  @Autowired
  BaseDao baseDao;
  @Autowired
  private ShippingOrderStatusService shippingOrderStatusService;
  @Autowired
  private ReconciliationStatusDao reconciliationStatusDao;

  public ReplacementOrder createReplaceMentOrder(ShippingOrder shippingOrder, ArrayList<LineItem> lineItems, Boolean isRto) {
    ReplacementOrder replacementOrder = getReplacementOrderFromShippingOrder(shippingOrder);
    for (LineItem lineItem : lineItems){
      if(lineItem.getQty() != 0){
//        lineItem = lineItemDao.getLineItem(lineItem.getSku(), shippingOrder);
        LineItem lineItemNew = getLineItemForReplacementOrder(lineItem);
        lineItemNew.setShippingOrder(replacementOrder);
        if(!isRto){
          lineItemNew.setHkPrice(0.00);
          lineItemNew.setCodCharges(0.00);
        }
        replacementOrder.getLineItems().add(lineItemNew);
//      lineItem.setShippingOrder(replacementOrder);
//      lineItemDao.save(lineItemNew);
      }
    }
    replacementOrder.setAmount(ShippingOrderHelper.getAmountForSO(replacementOrder));
    replacementOrder.setRto(isRto);
    replacementOrder.setRefShippingOrder(shippingOrder);
    save(replacementOrder);
    ShippingOrderHelper.setGatewayIdOnShippingOrder(replacementOrder);
    save(replacementOrder);
    return replacementOrder;
  }

  private ReplacementOrder getReplacementOrderFromShippingOrder(ShippingOrder shippingOrder){
    ReplacementOrder replacementOrder = new ReplacementOrder();
    replacementOrder.setBaseOrder(shippingOrder.getBaseOrder());
    replacementOrder.setWarehouse(shippingOrder.getWarehouse());
    replacementOrder.setCancellationType(shippingOrder.getCancellationType());
    replacementOrder.setCancellationRemark(shippingOrder.getCancellationRemark());
    replacementOrder.setBasketCategory(shippingOrder.getBasketCategory());
    replacementOrder.setServiceOrder(shippingOrder.isServiceOrder());
    replacementOrder.setVersion(shippingOrder.getVersion());
    replacementOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.RO_Created));
    replacementOrder.setCreateDate(new Date());
    replacementOrder.setUpdateDate(new Date());
    replacementOrder.setAmount(0D);
    replacementOrder.setReconciliationStatus(getReconciliationStatusDao().getReconciliationStatusById(EnumReconciliationStatus.PENDING));
    return replacementOrder;
  }

  private LineItem getLineItemForReplacementOrder(LineItem lineItem){
    LineItem replacementOrderLineItem = new LineItem();
    replacementOrderLineItem.setSku(lineItem.getSku());
//    replacementOrderLineItem.setShippingOrder(shippingOrder);
    replacementOrderLineItem.setCartLineItem(lineItem.getCartLineItem());
//    replacementOrderLineItem.setQty(cartLineItem.getQty());
    replacementOrderLineItem.setCostPrice(lineItem.getCartLineItem().getProductVariant().getCostPrice());
    replacementOrderLineItem.setMarkedPrice(lineItem.getCartLineItem().getMarkedPrice());
    replacementOrderLineItem.setHkPrice(lineItem.getCartLineItem().getHkPrice());
    replacementOrderLineItem.setDiscountOnHkPrice(lineItem.getCartLineItem().getDiscountOnHkPrice());
    replacementOrderLineItem.setTax(lineItem.getSku().getTax());
    replacementOrderLineItem.setQty(lineItem.getQty());
    return replacementOrderLineItem;
  }

  public void save(ReplacementOrder replacementOrder){
    getBaseDao().save(replacementOrder);
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
