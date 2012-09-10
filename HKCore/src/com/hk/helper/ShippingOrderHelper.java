package com.hk.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import com.hk.util.TokenUtils;

/**
 * @author vaibhav.adlakha
 * 
 * This will be used to update information on shipping order and will set all values in memory like discounts/amounts. Hopefully we will refrain from commmiting to db from here,
 * should be done either from service or dao 
 */
public class ShippingOrderHelper {

  public static double getAmountForSO(ShippingOrder shippingOrder) {
    double soBaseAmt = 0.0;
    for (LineItem lineItem : shippingOrder.getLineItems()) {
      double lineItemAmount = lineItem.getHkPrice() * lineItem.getQty();
      double totalDiscountOnLineItem = lineItem.getDiscountOnHkPrice() + lineItem.getOrderLevelDiscount() + lineItem.getRewardPoints();
      double forwardingCharges = lineItem.getShippingCharges() + lineItem.getCodCharges();
      soBaseAmt += (lineItemAmount - totalDiscountOnLineItem + forwardingCharges);
    }
    return soBaseAmt;
  }

  public static ShippingOrder setGatewayIdAndTargetDateOnShippingOrder(ShippingOrder shippingOrder) {
    String shippingOrderGatewayId = TokenUtils.generateShippingOrderGatewayOrderId(shippingOrder);
    shippingOrder.setGatewayOrderId(shippingOrderGatewayId);
    
    Long[] dispatchDays = OrderUtil.getDispatchDaysForSO(shippingOrder);
    Date targetDelDate = HKDateUtil.addToDate(shippingOrder.getBaseOrder().getPayment().getPaymentDate(), Calendar.DAY_OF_MONTH, Integer.parseInt(dispatchDays[0].toString()));
    shippingOrder.setTargetDelDate(targetDelDate);
    
    return shippingOrder;
  }

  public static void updateAccountingOnSOLineItems(ShippingOrder shippingOrder, Order order) {

    double rewardPointsOnBO = AccountingHelper.getRewardPointsForBaseOrder(order.getCartLineItems());
    double codChargesOnBO = AccountingHelper.getCODChargesForBaseOrder(order.getCartLineItems());
    double shippingChargesOnBO = AccountingHelper.getShippingCostForBaseOrder(order.getCartLineItems());

    double soBaseAmt = getBaseAmountForSO(shippingOrder);
    double boBaseAmt = AccountingHelper.getBaseAmountOnBaseOrder(order);
    double mf = boBaseAmt != 0 ? (soBaseAmt / boBaseAmt)  : 0;

    double rewardPointsOnSO = rewardPointsOnBO * mf;
    double shippingChargeOnSO = shippingChargesOnBO * mf;
    double codChargesOnSO = codChargesOnBO * mf;


    Set<CartLineItem> cartLIOnOrder = order.getCartLineItems();
    for (LineItem shippingOrderLineItem : shippingOrder.getLineItems()) {
      double lineItemAmt = (shippingOrderLineItem.getHkPrice() * shippingOrderLineItem.getQty());
      double lineItemMf = soBaseAmt != 0 ? lineItemAmt / soBaseAmt : 0;

      shippingOrderLineItem.setRewardPoints(lineItemMf * rewardPointsOnSO);
      shippingOrderLineItem.setShippingCharges(lineItemMf * shippingChargeOnSO);
      shippingOrderLineItem.setCodCharges(lineItemMf * codChargesOnSO);

      double orderLvlDiscOnLI = AccountingHelper.getOrderLevelDiscOnCartLI(cartLIOnOrder, shippingOrderLineItem.getSku().getProductVariant(),
          shippingOrderLineItem.getCartLineItem().getCartLineItemConfig());

      shippingOrderLineItem.setOrderLevelDiscount(orderLvlDiscOnLI);
    }

  }

  private static double getBaseAmountForSO(ShippingOrder shippingOrder) {
    double soBaseAmt = 0.0;
    for (LineItem lineItem : shippingOrder.getLineItems()) {
      soBaseAmt += (lineItem.getHkPrice() * lineItem.getQty());
    }

    return soBaseAmt;
  }
}
