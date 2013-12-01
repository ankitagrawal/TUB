package com.hk.util;

import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.domain.courier.ShipmentServiceType;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/19/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShipmentServiceMapper {

    public static Boolean isCod(ShipmentServiceType shipmentServiceType){
        for (EnumShipmentServiceType enumShipmentServiceType : EnumShipmentServiceType.getCodEnumShipmentServiceTypes()) {
            if(enumShipmentServiceType.getId().equals(shipmentServiceType.getId())){
                return true;
            }
        }
        return false;
    }

    public static Boolean isGround(ShipmentServiceType shipmentServiceType){
        for (EnumShipmentServiceType enumShipmentServiceType : EnumShipmentServiceType.getGroundEnumShipmentServiceTypes()) {
            if(enumShipmentServiceType.getId().equals(shipmentServiceType.getId())){
                return true;
            }
        }
        return false;
    }



}
