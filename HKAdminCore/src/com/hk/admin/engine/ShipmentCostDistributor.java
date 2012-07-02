package com.hk.admin.engine;

import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 6/29/12
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipmentCostDistributor {
        /*
        when (view1.weight_of_shipment is null or view1.weight_of_shipment = 0)
     then lt.qty/view1.total_qty*st.shipment_charge
    else
    (lt.qty*pv.weight/view1.weight_of_shipment)*st.shipment_charge end ,2)
         */

    public void distributeShipping(ShippingOrder shippingOrder){

       if(shippingOrder != null){
           Shipment shipment = shippingOrder.getShipment();
           if(shipment != null){
               Double totalShipmentCharge = shipment.getEstmShipmentCharge();
               Double totalReconciliationCharge = shipment.getEstmCollectionCharge();
               Double totalExtraCost = shipment.getExtraCharge();
               Double totalQtyOrdered = 0D;
               for (LineItem lineItem : shippingOrder.getLineItems()) {
                   totalQtyOrdered += lineItem.getQty();
               }

               Double shipmentWeight = shipment.getBoxWeight();

               for (LineItem lineItem : shippingOrder.getLineItems()) {
                   Double skuWeight = lineItem.getSku().getProductVariant().getWeight();
                   skuWeight = skuWeight != null ? skuWeight : 125D;

                   Double estLineItemShipmentCost = ((skuWeight * lineItem.getQty()) / shipmentWeight) * totalShipmentCharge;

                   Double lineItemOfferPrice = lineItem.getHkPrice() - lineItem.getDiscountOnHkPrice() - lineItem.getRewardPoints() + lineItem.getShippingCharges() + lineItem.getCodCharges();
                   Double estLIReconCost = (lineItemOfferPrice * lineItem.getQty()) / shippingOrder.getAmount();

                   Double estPackingCost =  (lineItem.getQty() / totalQtyOrdered) * totalExtraCost;

               }
           }
       }

    }

}
