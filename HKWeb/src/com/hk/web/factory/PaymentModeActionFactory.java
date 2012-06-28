package com.hk.web.factory;


import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.web.action.core.payment.*;

public class PaymentModeActionFactory {

    @SuppressWarnings("unchecked")
    public static Class<? extends BasePaymentGatewaySendReceiveAction> getActionClassForPaymentMode(EnumPaymentMode enumPaymentMode) {

        if (EnumPaymentMode.CCAVENUE_DUMMY.getId().equals(enumPaymentMode.getId())) {
            return CCAvenueDummyGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.TECHPROCESS_TEST.getId().equals(enumPaymentMode.getId())) {
            return TekprocessTestGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.TECHPROCESS.getId().equals(enumPaymentMode.getId())) {
            return TekprocessGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.COD.getId().equals(enumPaymentMode.getId())) {
            return CodGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.CITRUS.getId().equals(enumPaymentMode.getId())) {
//            return CitrusGatewaySendReceiveAction.class;
            return CitrusNetbankingSendReceiveAction.class;
        } else {
            return null;
        }
    }
}
