package web.factory;

import web.action.core.payment.CCAvenueDummyGatewaySendReceiveAction;
import web.action.core.payment.CitrusGatewaySendReceiveAction;
import web.action.core.payment.CodGatewaySendReceiveAction;
import web.action.core.payment.TekprocessGatewaySendReceiveAction;
import web.action.core.payment.TekprocessTestGatewaySendReceiveAction;

import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.constants.payment.EnumPaymentMode;

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
        } else {
            return null;
        }
    }
}
