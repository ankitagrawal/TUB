package com.hk.constants.courier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumCourierGroup {
    COMMON(10L, "COMMON", EnumCourier.DTDC_COD.getId()),
    Bluedart(20L, "Bluedart", EnumCourier.BlueDart.getId()),
    Local(30L, "Local", EnumCourier.Chhotu.getId()),
    EarthMovers(40L, "EarthMovers", EnumCourier.IndiaEarthMovers.getId()),
    Others(50L, "Others", EnumCourier.Speedpost.getId()),;

    private java.lang.String name;
    private java.lang.Long id;
    private java.lang.Long courierId;

    EnumCourierGroup(java.lang.Long id, java.lang.String name, java.lang.Long courierId) {
        this.name = name;
        this.id = id;
        this.courierId = courierId;
    }

    public static List<Long> getEnumIDs(List<EnumCourierGroup> enumList) {
        List<Long> getEnumIDs = new ArrayList<Long>();
        for (EnumCourierGroup enumCourierGroup : enumList) {
            getEnumIDs.add(enumCourierGroup.getId());
        }
        return getEnumIDs;
    }

	public static List<Long> getValidCourierGroupsInUse() {
        return getEnumIDs(Arrays.asList(EnumCourierGroup.COMMON, EnumCourierGroup.Bluedart, EnumCourierGroup.Local));
    }


    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getCourierId() {
        return courierId;
    }
}
