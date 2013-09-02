package com.hk.admin.engine;

import com.hk.admin.impl.service.shippingOrder.ShipmentServiceImpl;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.WHReportLineItem;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Pratham Date: 6/29/12 Time: 4:45 PM To change this template use File | Settings |
 * File Templates.
 */

public class ShipmentCostDistributor {

    public static WHReportLineItem distributeShippingCost(LineItem lineItem) {

        ShippingOrder shippingOrder = lineItem.getShippingOrder();
        if (shippingOrder != null) {
            WHReportLineItem whReportLineItem = lineItem.getWhReportLineItem();
            Shipment shipment = shippingOrder.getShipment();
            Double estLineItemShipmentCost = 0D;
            Double estLineItemReconCost = 0D;
            Double estLineItemPackingCost = 0D;
            Double lineItemPrice = 0D;

            if (shipment != null) {
                boolean flag = false;
                Double totalShipmentCharge = shipment.getEstmShipmentCharge();
                Double totalReconciliationCharge = shipment.getEstmCollectionCharge();
                Double totalExtraCost = shipment.getExtraCharge();
                Double totalQtyOrdered = 0D;
                Double totalWt = 0D;
                Double totalPrice = 0D;
                for (LineItem lineItem1 : shippingOrder.getLineItems()) {
                    totalQtyOrdered += lineItem1.getQty();
                    totalWt += lineItem1.getSku().getProductVariant().getWeight() * lineItem1.getQty();
                    totalPrice += lineItem1.getHkPrice() * lineItem1.getQty();
                    if(lineItem1.getSku().getProductVariant().getWeight() == 0) {
                        flag = true;
                    }
                }

                Double skuWeight = lineItem.getSku().getProductVariant().getWeight();
                skuWeight = skuWeight == null || skuWeight == 0D ? 0D : skuWeight;
                lineItemPrice = lineItem.getHkPrice();
                if (!flag) {
                    estLineItemShipmentCost = ((skuWeight * lineItem.getQty()) / totalWt) * totalShipmentCharge;
                } else {
                    estLineItemShipmentCost = ((lineItemPrice * lineItem.getQty()) / totalPrice) * totalShipmentCharge;
                }
                estLineItemReconCost = ((lineItemPrice * lineItem.getQty()) / totalPrice) * totalReconciliationCharge;
                estLineItemPackingCost = (lineItem.getQty() / totalQtyOrdered) * totalExtraCost;

                if (whReportLineItem == null) {
                    whReportLineItem = new WHReportLineItem();
                }

                whReportLineItem.setLineItem(lineItem);
                whReportLineItem.setEstmShipmentCharge(estLineItemShipmentCost);
                whReportLineItem.setEstmCollectionCharge(estLineItemReconCost);
                whReportLineItem.setExtraCharge(estLineItemPackingCost);
                return whReportLineItem;
            }
        }
        return null;
    }

}
