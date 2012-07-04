package com.hk.web.factory;


import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.web.action.core.payment.CodGatewaySendReceiveAction;
import com.hk.web.action.core.payment.gateway.CitrusCreditDebitSendReceiveAction;
import com.hk.web.action.core.payment.gateway.CitrusGatewaySendReceiveAction;
import com.hk.web.action.core.payment.gateway.CitrusNetbankingSendReceiveAction;
import com.hk.web.action.core.payment.TekprocessGatewaySendReceiveAction;
import com.hk.web.action.core.payment.gateway.test.CCAvenueDummyGatewaySendReceiveAction;
import com.hk.web.action.core.payment.gateway.test.TekprocessTestGatewaySendReceiveAction;

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
            return CitrusGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.CITRUS_NetBanking_New.getId().equals(enumPaymentMode.getId())) {
            return CitrusNetbankingSendReceiveAction.class;
        } else if (EnumPaymentMode.CITRUS_CreditDebit.getId().equals(enumPaymentMode.getId())) {
            return CitrusCreditDebitSendReceiveAction.class;
        } else {
            return null;
        }
    }
}
