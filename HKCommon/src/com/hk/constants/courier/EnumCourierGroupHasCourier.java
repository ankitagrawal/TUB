package com.hk.constants.courier;

import java.util.Arrays;
import java.util.List;

public enum EnumCourierGroupHasCourier {

    COMMON(
            EnumCourierGroup.COMMON,
            Arrays.asList(
                    EnumCourier.DTDC_COD,
                    EnumCourier.DTDC_Lite,
                    EnumCourier.DotZot_Economy,
                    EnumCourier.DotZot_Express,
                    EnumCourier.Speedpost,
                    EnumCourier.AFLWiz
            )
    ),

    Bluedart(
            EnumCourierGroup.Bluedart,
            Arrays.asList(
                    EnumCourier.BlueDart
            )
    ),


    Local(
            EnumCourierGroup.Local,
            Arrays.asList(
                    EnumCourier.HK_Delivery,
                    EnumCourier.Quantium,
                    EnumCourier.Smile_EExpress,
                    EnumCourier.Smile_Express_DSP,
                    EnumCourier.Safexpress,
                    EnumCourier.Delhivery_Surface,
                    EnumCourier.Delhivery
            )
    ),;

    EnumCourierGroup enumCourierGroup;
    List<EnumCourier> enumCouriers;

    EnumCourierGroupHasCourier(EnumCourierGroup enumCourierGroup, List<EnumCourier> enumCouriers) {
        this.enumCourierGroup = enumCourierGroup;
        this.enumCouriers = enumCouriers;
    }

    public List<EnumCourier> getEnumCouriers() {
        return enumCouriers;
    }

    public void setEnumCouriers(List<EnumCourier> enumCouriers) {
        this.enumCouriers = enumCouriers;
    }

    public String getCourierGroupName() {
        return enumCourierGroup.getName();
    }
}
