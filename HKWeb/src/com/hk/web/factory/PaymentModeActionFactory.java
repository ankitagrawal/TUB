package com.hk.web.factory;


import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.payment.Gateway;
import com.hk.web.action.core.payment.CodGatewaySendReceiveAction;
import com.hk.web.action.core.payment.TekprocessGatewaySendReceiveAction;
import com.hk.web.action.core.payment.gateway.*;
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
        } else if (EnumPaymentMode.CITRUS_NetBanking_Old.getId().equals(enumPaymentMode.getId())) {
            return CitrusGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.CITRUS_NetBanking_New.getId().equals(enumPaymentMode.getId())) {
            return CitrusNetbankingSendReceiveAction.class;
        } else if (EnumPaymentMode.CITRUS_CreditDebit.getId().equals(enumPaymentMode.getId())) {
            return CitrusCreditDebitSendReceiveAction.class;
        } else if (EnumPaymentMode.ICICI.getId().equals(enumPaymentMode.getId())) {
            return IciciGatewaySendReceiveAction.class;
        } else if (EnumPaymentMode.EBS.getId().equals(enumPaymentMode.getId())) {
            return EbsSendReceiveAction.class;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends BasePaymentGatewaySendReceiveAction> getActionClassByGatewayIssuer(Gateway gateway, String issuerType) {

        if (EnumGateway.CCAVENUE_DUMMY.getId().equals(gateway.getId())) {
            return CCAvenueDummyGatewaySendReceiveAction.class;
        } else if (EnumGateway.TECHPROCESS.getId().equals(gateway.getId())) {
            return TekprocessGatewaySendReceiveAction.class;
        } else if (EnumGateway.EBS.getId().equals(gateway.getId())) {
            return EbsSendReceiveAction.class;
        } else if (EnumGateway.CITRUS.getId().equals(gateway.getId()) && EnumIssuerType.Bank.getId().equals(issuerType)) {
            return CitrusNetbankingSendReceiveAction.class;
        } else if (EnumGateway.CITRUS.getId().equals(gateway.getId()) && EnumIssuerType.Card.getId().equals(issuerType)) {
            return CitrusCreditDebitSendReceiveAction.class;
        } else if (EnumGateway.ICICI.getId().equals(gateway.getId())) {
            return IciciGatewaySendReceiveAction.class;
        } else {
            return null;
        }
    }

}
