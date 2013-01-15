package com.hk.admin.pact.service.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.ShipmentServiceType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/11/12
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PincodeCourierService {

    public boolean isCodAllowed(String pin);

    public boolean isGroundShippingAllowed(String pin);

    public boolean isCodAllowedOnGroundShipping(String pin);

    public List<Courier> getAvailableCouriers(Order order);

    public List<Courier> getApplicableCouriers(ShippingOrder shippingOrders);

    public boolean isCourierAvailable(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);


    public List<ShipmentServiceType> getShipmentServiceType(Set<CartLineItem> productCartLineItems, boolean checkForCod);

    public ShipmentServiceType getShipmentServiceType(ShippingOrder shippingOrder);


    public Courier getApplicableCourier(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier);


    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier);

    public PincodeCourierMapping getApplicablePincodeCourierMapping(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public PincodeCourierMapping createPincodeCourierMapping(Pincode pincode, Courier courier, boolean prepaidAir, boolean prepaidGround, boolean codAir, boolean codGround);

    public PincodeCourierMapping savePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping);


    public boolean isDefaultCourierApplicable(Pincode pincode, Courier courier, boolean isGround, boolean isCod);

    public Map<String,Boolean> generateDetailedAnalysis(List<PincodeCourierMapping> pincodeCourierMappings);

}
