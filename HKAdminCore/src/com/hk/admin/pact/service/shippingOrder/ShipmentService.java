package com.hk.admin.pact.service.shippingOrder;

import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Awb;
import com.hk.domain.order.ShippingOrder;


public interface ShipmentService  {

    public Shipment saveShipmentDate(Shipment shipment);

    public Shipment save(Shipment shipment);

    public Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder);

    public Shipment findByAwb(Awb awb);
}
