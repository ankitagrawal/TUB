package com.hk.admin.engine;

import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.WHReportLineItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: Pratham Date: 6/29/12 Time: 4:45 PM To change this template use File | Settings |
 * File Templates.
 */
public class ShipmentCostDistributor {
    /*
     * when (view1.weight_of_shipment is null or view1.weight_of_shipment = 0) then
     * lt.qty/view1.total_qty*st.shipment_charge else (lt.qty*pv.weight/view1.weight_of_shipment)*st.shipment_charge end
     * ,2)
     */

    public void distributeShippingCost(ShippingOrder shippingOrder) {

        if (shippingOrder != null) {
            Shipment shipment = shippingOrder.getShipment();
            if (shipment != null) {
                Double totalShipmentCharge = shipment.getEstmShipmentCharge();
                Double totalReconciliationCharge = shipment.getEstmCollectionCharge();
                Double totalExtraCost = shipment.getExtraCharge();
                Double totalQtyOrdered = 0D;
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    totalQtyOrdered += lineItem.getQty();
                }

                Double shipmentWeight = shipment.getBoxWeight();

                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    WHReportLineItem whReportLineItem = new WHReportLineItem();
                    Double estLineItemShipmentCost = 0D;
                    Double estLineItemReconCost = 0D;
                    Double estlineItemPackingCost = 0D;
                    Double lineItemOfferPrice = 0D;

                    Double skuWeight = lineItem.getSku().getProductVariant().getWeight();
                    skuWeight = skuWeight == null || skuWeight == 0D ? 0D : skuWeight;

                    if (skuWeight != 0D) {
                        estLineItemShipmentCost = ((skuWeight * lineItem.getQty()) / shipmentWeight) * totalShipmentCharge;
                    } else {
                        estLineItemShipmentCost = (lineItem.getQty() / totalQtyOrdered) * totalShipmentCharge;
                    }

                    lineItemOfferPrice = lineItem.getHkPrice() - lineItem.getDiscountOnHkPrice() - lineItem.getRewardPoints()
                                                        + lineItem.getShippingCharges() + lineItem.getCodCharges();
                    estLineItemReconCost = (lineItemOfferPrice * lineItem.getQty()) / shippingOrder.getAmount();
                    estlineItemPackingCost = (lineItem.getQty() / totalQtyOrdered) * totalExtraCost;

                    whReportLineItem.setLineItem(lineItem);
                    whReportLineItem.setEstmShipmentCharge(estLineItemShipmentCost);
                    whReportLineItem.setEstmCollectionCharge(estLineItemReconCost);
                    whReportLineItem.setExtraCharge(estlineItemPackingCost);
                }
            }
        }

    }

}
