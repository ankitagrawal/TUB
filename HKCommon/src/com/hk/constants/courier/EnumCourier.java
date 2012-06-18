package com.hk.constants.courier;

import com.hk.domain.courier.Courier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum EnumCourier {

    DTDC_Plus(10L, "DTDC Plus"),
    DTDC_Lite(11L, "DTDC Lite"),
    DTDC_COD(12L, "DTDC COD"),
    DTDC_Surface(13L, "DTDC Surface"),
    Aramex(20L, "Aramex"),
    Speedpost(30L, "Speed Post"),
    Delhivery(40L, "Delhivery-Delhi-NCR"),
    Chhotu(45L, "Chhotu-Delhi-NCR"),
    OfficePickup(50L, "Office Pickup"),
    AFLWiz(60L, "AFL Wiz"),
    BlueDart(70L, "Blue Dart"),
    BlueDart_COD(71L, "Blue Dart COD"),
    FirstFLight(80L, "First Flight"),
    FirstFLight_COD(85L, "First Flight COD"),

    Other(100L, "Other"),

    IndiaEarthMovers(110L, " India Earth Movers"),
    Safexpress_Chhotu(120L, " Safexpress - Chhotu"),
    Safexpress_Delhivery(130L, " Safexpress - Delhivery"),

    Delhivery_Ahmedabad(140L, "Delhivery - Ahmedabad"),
    Delhivery_Bangalore(150L, "Delhivery - Bangalore"),
    Delhivery_Chennai(160L, "Delhivery - Chennai"),

    EarthMoversPune(200L, "Earth Movers Pune"),

    HK_Delivery(500L, "HealthKart Delivery"),

    MIGRATE(-1L, "MIGRATE");

    private java.lang.String name;
    private java.lang.Long id;

    EnumCourier(java.lang.Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }


    public Courier asCourier() {
        Courier courier = new Courier();
        courier.setId(this.id);
        courier.setName(this.name);
        return courier;
    }

    public static List<Long> getCourierIDs(List<EnumCourier> enumCourierList) {
        List<Long> courierIds = new ArrayList<Long>();
        for (EnumCourier enumCourier : enumCourierList) {
            courierIds.add(enumCourier.getId());
        }
        return courierIds;
    }


    public static List<EnumCourier> getRestOfIndiaAvailableCouriers() {
        return Arrays.asList(
                EnumCourier.DTDC_Plus,
                EnumCourier.DTDC_Lite,
                EnumCourier.BlueDart,
                EnumCourier.BlueDart_COD,
                EnumCourier.Speedpost,
                EnumCourier.DTDC_Surface,
                EnumCourier.Speedpost,
                EnumCourier.Delhivery,
                EnumCourier.Chhotu,
                EnumCourier.OfficePickup,
                EnumCourier.AFLWiz,
                EnumCourier.BlueDart,
                EnumCourier.BlueDart_COD,
                EnumCourier.FirstFLight,
                EnumCourier.FirstFLight_COD,
                EnumCourier.IndiaEarthMovers,
                EnumCourier.EarthMoversPune,
                EnumCourier.DTDC_COD);
    }

    public static List<EnumCourier> getDelhiveryCouriers() {
        return Arrays.asList(
                EnumCourier.Delhivery,
                EnumCourier.Delhivery_Ahmedabad,
                EnumCourier.Delhivery_Bangalore,
                EnumCourier.Delhivery_Chennai);
    }

    public static List<Long> getDTDCCouriers() {
        List<Long> dtdcCourierIds = new ArrayList<Long>();
        dtdcCourierIds.add(EnumCourier.DTDC_COD.getId());
        dtdcCourierIds.add(EnumCourier.DTDC_Lite.getId());
        dtdcCourierIds.add(EnumCourier.DTDC_Plus.getId());
        dtdcCourierIds.add(EnumCourier.DTDC_Surface.getId());
        return dtdcCourierIds;
    }

    public static List<Long> getBlueDartCouriers() {
        List<Long> blueDartCourierIds = new ArrayList<Long>();
        blueDartCourierIds.add(EnumCourier.BlueDart.getId());
        blueDartCourierIds.add(EnumCourier.BlueDart_COD.getId());
        return blueDartCourierIds;
    }

    public static List<Long> getDelhiveryCourierIds() {
        List<Long> delhiveryCourierIds = new ArrayList<Long>();
        delhiveryCourierIds.add(EnumCourier.Delhivery.getId());
        delhiveryCourierIds.add(EnumCourier.Delhivery_Ahmedabad.getId());
        delhiveryCourierIds.add(EnumCourier.Delhivery_Bangalore.getId());
        delhiveryCourierIds.add(EnumCourier.Delhivery_Chennai.getId());
        return delhiveryCourierIds;
    }
}

