package com.hk.constants.shipment;

import com.hk.domain.courier.ShipmentServiceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 12/10/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumShipmentServiceType {

    Prepaid_Air(10L, "prepaidAir"),
    Prepaid_Ground(20L, "prepaidGround"),
    Cod_Air(30L, "codAir"),
    Cod_Ground(40L, "codGround");
    //futuristic, drop ship, express shipping

    private java.lang.String name;
    private Long id;

    EnumShipmentServiceType(java.lang.Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static EnumShipmentServiceType getShipmentTypeFromId(Long id) {
        for (EnumShipmentServiceType shipmentType : values()) {
            if (shipmentType.getId().equals(id)) return shipmentType;
        }
        return null;
    }

    public static List<EnumShipmentServiceType> getCodEnumShipmentServiceTypes() {
        return Arrays.asList(Cod_Air,Cod_Ground);
    }

    public static List<EnumShipmentServiceType> getGroundEnumShipmentServiceTypes() {
        return Arrays.asList(Prepaid_Ground,Cod_Ground);
    }

    public static List<EnumShipmentServiceType> getAirEnumShipmentServiceTypes() {
        return Arrays.asList(Cod_Air,Prepaid_Air);
    }

    public static List<EnumShipmentServiceType> getAllShipmentServiceType() {
        return Arrays.asList(Cod_Air,Cod_Ground,Prepaid_Air,Prepaid_Ground);
    }

    public ShipmentServiceType asShipmentServiceType() {
        ShipmentServiceType shipmentServiceType = new ShipmentServiceType();
        shipmentServiceType.setId(id);
        shipmentServiceType.setName(name);
        return shipmentServiceType;
    }

    public static List<Long> getShipmentServiceTypesIds(List<EnumShipmentServiceType> enumShipmentServiceTypes) {
        List<Long> shipmentServiceTypeIds = new ArrayList<Long>();
        for (EnumShipmentServiceType enumShipmentServiceType : enumShipmentServiceTypes) {
            shipmentServiceTypeIds.add(enumShipmentServiceType.getId());
        }
        return shipmentServiceTypeIds;
    }
}
