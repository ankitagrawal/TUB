package com.hk.constants.courier;

public enum EnumCourierGroup {
    COMMON(10L, "COMMON", EnumCourier.DTDC_COD.getId()), Bluedart(20L, "Bluedart", EnumCourier.BlueDart.getId()), Local(30L, "Local", EnumCourier.Chhotu.getId()), EarthMovers(40L,
            "EarthMovers", EnumCourier.IndiaEarthMovers.getId()), ;

    private java.lang.String name;
    private java.lang.Long   id;
    private java.lang.Long   courierId;

    EnumCourierGroup(java.lang.Long id, java.lang.String name, java.lang.Long courierId) {
        this.name = name;
        this.id = id;
        this.courierId = courierId;
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
