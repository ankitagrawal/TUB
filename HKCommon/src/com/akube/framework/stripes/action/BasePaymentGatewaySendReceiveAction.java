package com.akube.framework.stripes.action;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.ssl.SslUtil;

import org.springframework.stereotype.Component;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;

/**
 * Extend this action for any action that will send and receive requests to a payment gateway.<br/> Author: Kani Date:
 * Dec 30, 2008
 */
@Component
public abstract class BasePaymentGatewaySendReceiveAction<T extends PaymentGatewayWrapper> extends BaseAction {

    /**
     * implement this method to return an instance of PaymentGatewayWrapper
     * 
     * @param data
     * @return
     */
    protected abstract T getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data);

    /**
     * this is the resolution that should be called when someone is to be redirected to a payment gateway.
     * 
     * @return
     */
    public Resolution proceed() {

        String encodedData = getContext().getRequest().getParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM);
        BasePaymentGatewayWrapper.TransactionData data = BasePaymentGatewayWrapper.decodeTransactionDataParam(encodedData);
        PaymentGatewayWrapper paymentGatewayWrapper = getPaymentGatewayWrapperFromTransactionData(data);
        getContext().getRequest().setAttribute("PaymentGatewayWrapper", paymentGatewayWrapper);
        if(isHybridRelease()){
            return new ForwardResolution("/gatewayProcessBeta.jsp");
        }
        return new ForwardResolution("/gatewayProcess.jsp");

    }

    /**
     * this will be the defaultHandler. Any request returning from the payment gateway will automatically be calling
     * this resolution. This resolution will take care of validating the request, authenticating the payment and
     * subsequently updating the order state and redirecting the user to the appropriate screens
     * 
     * @return
     */
    public abstract Resolution callback();

    /**
     * returns the redirect url. automatically check for ssl annotations on the subclass and prepend http or https to
     * the url. the context path is also appended the callback event handler shouls be made the default handler so that
     * callbacks automatically invoke that method.
     * 
     * @return
     */
    public String getRedirectUrl() {
        return getRedirectUrl(this);
    }

    /**
     * returns the redirect url. automatically check for ssl annotations on the subclass and prepend http or https to
     * the url. the context path is also appended the callback event handler shouls be made the default handler so that
     * callbacks automatically invoke that method.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getRedirectUrl(BasePaymentGatewaySendReceiveAction sendReceiveAction) {

        String redirectUrl = StripesFilter.getConfiguration().getActionResolver().getUrlBinding(sendReceiveAction.getClass());
        return SslUtil.encodeUrlFullForced(getContext().getRequest(), getContext().getResponse(), redirectUrl, null);
    }

}
