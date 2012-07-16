package com.hk.admin.pact.service.shippingOrder;

import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;


public interface ShipmentService {

  public Shipment createShipment(ShippingOrder shippingOrder);
  
  public Shipment saveShipmentDate(Shipment shipment) ;

  public Shipment save(Shipment shipment) ;

}
