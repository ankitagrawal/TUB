package com.hk.constants.courier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.courier.Courier;

public enum EnumCourier {

    DTDC_Plus(10L, "DTDC Plus"),
    DTDC_Lite(11L, "DTDC Lite"),
    DTDC_COD(12L, "DTDC COD"),
    DTDC_Surface(13L, "DTDC Surface"),
    Aramex(20L, "Aramex"),
    Speedpost(30L, "Speed Post"),
    Delhivery(40L, "Delhivery"),
	Delhivery_Surface(41L, "Delhivery-Surface"),
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
    Safexpress(170L, " Safexpress"),
    Quantium(180L, "Quantium"),
    Smile_EExpress(190L, "Smile EExpress"),
    Smile_Express_DSP(195L,"Smile Express DSP"),
    EarthMoversPune(200L, "Earth Movers Pune"),
	IndiaOnTime(210L, "IndiaOn Time"),
    HK_Delivery(500L, "HealthKart Delivery"),
    FedEx(600L, "FedEx"),
    FedEx_Surface(610L, "FedEx Surface"),
    Gati(650L, "Gati"),
    Star_Track(660L, "Star Track"),

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

    public static EnumCourier getEnumCourierFromCourierId(Long id) {
        for (EnumCourier enumCourier : values()) {
            if (enumCourier.getId().equals(id))
                return enumCourier;
        }
        return null;
    }

    public static List<Long> getCourierIDs(List<EnumCourier> enumCourierList) {
        List<Long> courierIds = new ArrayList<Long>();
        for (EnumCourier enumCourier : enumCourierList) {
            courierIds.add(enumCourier.getId());
        }
        return courierIds;
    }


    public static List<EnumCourier> getCurrentlyApplicableCouriers() {
        return Arrays.asList(
                EnumCourier.DTDC_Plus,
                EnumCourier.DTDC_Lite,
                EnumCourier.DTDC_Surface,
                EnumCourier.DTDC_COD,
                EnumCourier.Speedpost,
                EnumCourier.Delhivery,
                EnumCourier.Delhivery_Surface,
                EnumCourier.Smile_EExpress,
                EnumCourier.Smile_Express_DSP,
                EnumCourier.AFLWiz,
                EnumCourier.BlueDart,
                EnumCourier.Safexpress,
                EnumCourier.Quantium,
                EnumCourier.HK_Delivery,
                EnumCourier.FedEx,
                EnumCourier.FedEx_Surface
        );
    }


     public static List<EnumCourier> getCurrentlyApplicableGroundShippedCouriers() {
        return Arrays.asList(
                EnumCourier.DTDC_Surface,
                EnumCourier.Delhivery_Surface,
                EnumCourier.FedEx_Surface
        );
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
        return blueDartCourierIds;
    }

    public static List<Long> getDelhiveryCourierIds() {
        List<Long> delhiveryCourierIds = new ArrayList<Long>();
        delhiveryCourierIds.add(EnumCourier.Delhivery.getId());
        delhiveryCourierIds.add(EnumCourier.Delhivery_Surface.getId());
        return delhiveryCourierIds;
    }

	 public static List<Long> getFedexCouriers() {
        List<Long> fedexCourierIds = new ArrayList<Long>();
        fedexCourierIds.add(EnumCourier.FedEx.getId());
        fedexCourierIds.add(EnumCourier.FedEx_Surface.getId());
        return fedexCourierIds;
    }

	public static List<Long> getDispatchLotCouriers(){
		List<Long> dispatchLotCourierIds = new ArrayList<Long>();
		dispatchLotCourierIds.add(EnumCourier.HK_Delivery.getId());
		return dispatchLotCourierIds;
	}

    public static List<EnumCourier> getCurrentlyApplicableVendorCouriers() {
        return Arrays.asList(
                EnumCourier.BlueDart,
                EnumCourier.Gati,
                EnumCourier.Safexpress,
                EnumCourier.Star_Track
        );
    }
}

