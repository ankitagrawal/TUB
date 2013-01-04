package com.hk.admin.pact.dao.courier;

import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.ShipmentServiceType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/10/12
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PincodeCourierMappingDao {

    public List<PincodeCourierMapping> getApplicablePincodeCourierMapping(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier);

    List<ShipmentServiceType> getShipmentServiceType(Set<CartLineItem> productCartLineItems, boolean checkForCod);

    ShipmentServiceType getShipmentServiceType(ShippingOrder shippingOrder);

    ShipmentServiceType getShipmentServiceType(boolean isCod, boolean isGround);

    PincodeCourierService savePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping);

    public Courier searchDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);

}
