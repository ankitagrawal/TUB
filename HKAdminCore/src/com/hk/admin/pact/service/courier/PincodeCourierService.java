package com.hk.admin.pact.service.courier;

import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.ShipmentServiceType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;

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

    public ShippingOrder setTargetDeliveryDate(ShippingOrder shippingOrder);

    public boolean isCourierAvailable(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);

    public List<ShipmentServiceType> getShipmentServiceType(Set<CartLineItem> productCartLineItems, boolean checkForCod);

    public List<ShipmentServiceType> getShipmentServiceTypes(List<EnumShipmentServiceType> enumShipmentServiceTypes);

    public ShipmentServiceType getShipmentServiceType(ShippingOrder shippingOrder);


    public Courier getApplicableCourier(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier);

//    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<Courier> getApplicableCouriers(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier);


    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier);

    public PincodeCourierMapping getApplicablePincodeCourierMapping(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier);

    public PincodeCourierMapping createPincodeCourierMapping(Pincode pincode, Courier courier, boolean prepaidAir, boolean prepaidGround, boolean codAir, boolean codGround);

    public PincodeCourierMapping savePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping);

    public void deletePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping);

    public boolean isDefaultCourierApplicable(Pincode pincode, Courier courier, boolean isGround, boolean isCod);

    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGround);

    public PincodeDefaultCourier getPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGround);

    public boolean changePincodeCourierMapping(PincodeCourierMapping pincodeCourierMappingDb, PincodeCourierMapping pincodeCourierMappingSoft);

    public Map<String,Boolean> generateDetailedAnalysis(List<PincodeCourierMapping> pincodeCourierMappings);

    public List<Courier> getDefaultCouriers(Pincode pincode, Boolean isCOD, Boolean isGroundShipping, Warehouse warehouse);

    public Courier getDefaultCourier(ShippingOrder shippingOrder);

    public PincodeDefaultCourier createPincodeDefaultCourier(Pincode pincode, Courier courier, Warehouse warehouse, boolean isGroundShippingAvailable, boolean isCODAvailable, Double estimatedShippingCost);

    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);
}
