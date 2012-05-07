package com.hk.admin.impl.dao.courier;

import org.springframework.stereotype.Repository;

import com.hk.impl.dao.BaseDaoImpl;

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Mar 6, 2012 Time: 6:40:31 PM To change this template use File | Settings |
 * File Templates.
 */

@Repository
public class ShipmentDao extends BaseDaoImpl {

    
    /*public Shipment getOrCreateShipment(ShippingOrder shippingOrder, Courier courier, BoxSize boxSize, Double boxWeight, String trackingId) {
    Shipment shipment = shippingOrder.getShipment();
    if (shipment != null) {
      shipment.setCourier(courier);
      shipment.setBoxSize(boxSize);
      shipment.setBoxWeight(boxWeight);
      shipment.setTrackingId(trackingId);
    } else {
      shipment = new Shipment();
      shipment.setCourier(courier);
      shipment.setBoxSize(boxSize);
      shipment.setBoxWeight(boxWeight);
      shipment.setTrackingId(trackingId);
      shipment = shipmentDaoProvider.get().save(shipment);
    }
    return shipment;
  }
*/
    


}
