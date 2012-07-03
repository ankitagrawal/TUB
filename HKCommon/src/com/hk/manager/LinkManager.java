package com.hk.manager;

import com.hk.domain.TempToken;
import com.hk.domain.Ticket;
import com.hk.domain.accounting.AccountingInvoice;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LinkManager {

    public String getAuthRedirectUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/auth/AuthRequired.action");
        return getUrlFromResolution(redirectResolution);
    }

    /**
     * This method generated a full URL from the redirect resolution
     * 
     * @param redirectResolution
     * @return
     */

    private String getUrlFromResolution(RedirectResolution redirectResolution) {
        String url = redirectResolution.getUrl(Locale.getDefault());
        if (WebContext.getRequest() != null && WebContext.getResponse() != null) {
            return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, null);
        }
        String contextPath = AppConstants.contextPath;
        return SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, contextPath);
    }

    public String getTermsAndConditionsUrl() {
        return "terms.jsp";
    }

    public String getCodGatewayUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/payment/CodGatewaySendReceive.action");
        return getUrlFromResolution(redirectResolution);
    }

    public String getSelectAddressUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/user/SelectAddress.action");
        return getUrlFromResolution(redirectResolution);
    }

    public String getOrderTrackLink(String trackingId, Long courierId, ShippingOrder shippingOrder) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/order/TrackCourier.action").addParameter("trackingId", trackingId).addParameter("courierId",
                courierId).addParameter("shippingOrder", shippingOrder);
        return getUrlFromResolution(redirectResolution);
    }

    public String getOrderInvoiceLink(Order order) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/SOInvoice.action").addParameter("order", order.getId());
        return getUrlFromResolution(redirectResolution);
    }

    public String getShippingOrderInvoiceLink(ShippingOrder shippingOrder) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/SOInvoice.action").addParameter("shippingOrder", shippingOrder.getId());
        return getUrlFromResolution(redirectResolution);
    }

    public String getRetailInvoiceLink(AccountingInvoice accountingInvoice) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/AccountingInvoice.action").addParameter("accountingInvoice", accountingInvoice.getId());
        return getUrlFromResolution(redirectResolution);
    }

    public String getCartUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/cart/Cart.action");
        return getUrlFromResolution(redirectResolution);
    }

    public String getUserActivationLink(TempToken token) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/user/VerifyUser.action").addParameter("token", token.getToken());
        return getUrlFromResolution(redirectResolution);
    }

    public String getReferralSignupLink(User user) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/referral/ReferralSignup.action").addParameter("userHash", user.getUserHash());
        return getUrlFromResolution(redirectResolution);
    }

    public String getReferralProgramUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/referral/ReferralProgram.action");
        return getUrlFromResolution(redirectResolution);
    }

    public String getEmailUnsubscribeLink(EmailRecepient emailRecepient) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/email/UnsubscribeEmail.action").addParameter("unsubscribeToken", emailRecepient.getUnsubscribeToken());
        return getUrlFromResolution(redirectResolution);
    }

    public String getViewTicketUrl(Ticket ticket) {
        RedirectResolution redirectResolution = new RedirectResolution("/admin/ticket/ViewAndEditTicket.action").addParameter("ticket", ticket.getId());
        return getUrlFromResolution(redirectResolution);
    }

    public String getResetPasswordLink(TempToken token) {
        RedirectResolution redirectResolution = new RedirectResolution("/core/user/PasswordReset.action").addParameter("token", token.getToken());
        return getUrlFromResolution(redirectResolution);
    }
    public String getCitrusPaymentNetBankingGatewayUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/payment/CitrusNetbankingSendReceive.action");
        return getUrlFromResolution(redirectResolution);
    }
    public String getCitrusPaymentGatewayUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/payment/CitrusGatewaySendReceive.action");
        return getUrlFromResolution(redirectResolution);
    }
    public String getCitrusPaymentCreditDebitGatewayUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/payment/CitrusCreditDebitSendReceive.action");
        return getUrlFromResolution(redirectResolution);
    }

    public String getEbsPaymentGatewayReturnUrl() {
        RedirectResolution redirectResolution = new RedirectResolution("/core/payment/test/EbsSendReceive.action");
        return getUrlFromResolution(redirectResolution);
    }
}
