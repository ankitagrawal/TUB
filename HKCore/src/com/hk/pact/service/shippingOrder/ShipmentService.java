package com.hk.pact.service.shippingOrder;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;

public interface ShipmentService {

    public Shipment createShipment(ShippingOrder shippingOrder, Boolean validate);

    public Shipment validateShipment(ShippingOrder shippingOrder);

    public Shipment save(Shipment shipment);

    public Shipment findByAwb(Awb awb);

    public void delete(Shipment shipment);

    public Shipment changeCourier(Shipment shipment, Courier newCourier, boolean preserveAwb);

    public Shipment changeAwb(Shipment shipment,Awb awbNumber,boolean preserveAwb);

    public Shipment recreateShipment(ShippingOrder shippingOrder);

    public boolean isValidShipment(Shipment shipment);

    public boolean isShippingOrderHasInstallableItem(ShippingOrder shippingOrder);

    public Double getEstimatedWeightOfShipment(ShippingOrder shippingOrder);

}
