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

    Prepaid_Air(10L, "prepaid_air"),
    Prepaid_Ground(20L, "prepaid_ground"),
    Cod_Air(30L, "cod_air"),
    Cod_Ground(40L, "cod_ground");
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

    public static List<EnumShipmentServiceType> getCodEnumShipmentServiceTypes(){
        return Arrays.asList(EnumShipmentServiceType.Cod_Air,EnumShipmentServiceType.Cod_Ground);
    }

    public static List<EnumShipmentServiceType> getGroundEnumShipmentServiceTypes(){
        return Arrays.asList(EnumShipmentServiceType.Prepaid_Ground,EnumShipmentServiceType.Cod_Ground);
    }

    public static List<EnumShipmentServiceType> getAirEnumShipmentServiceTypes(){
        return Arrays.asList(EnumShipmentServiceType.Cod_Air,EnumShipmentServiceType.Prepaid_Air);
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
  public static List<EnumShipmentServiceType> getAllShipmentServiceType(){
    return Arrays.asList(EnumShipmentServiceType.Cod_Air,EnumShipmentServiceType.Cod_Ground,EnumShipmentServiceType.Prepaid_Air,EnumShipmentServiceType.Prepaid_Ground);
  }
}
