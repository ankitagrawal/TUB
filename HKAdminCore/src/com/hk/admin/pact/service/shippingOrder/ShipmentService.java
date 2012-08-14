package com.hk.admin.pact.service.shippingOrder;

import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Awb;

public interface ShipmentService {

    public Shipment createShipment(ShippingOrder shippingOrder);

    public Shipment saveShipmentDate(Shipment shipment);

    public Shipment save(Shipment shipment);

    public Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder);

    public Shipment findByAwb(Awb awb);

    public void delete(Shipment shipment);
}
