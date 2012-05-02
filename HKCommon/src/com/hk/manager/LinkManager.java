package com.hk.manager;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import com.hk.domain.TempToken;
import com.hk.domain.Ticket;
import com.hk.domain.accounting.AccountingInvoice;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;
import com.sun.istack.internal.Nullable;
@Component
public class LinkManager {

   //@Named(Keys.App.contextPath)
   /* @Value("#{hkEnvProps['contextPath']}")
   String contextPath;*/

  public String getAuthRedirectUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("AuthRequiredAction.class");
    return getUrlFromResolution(redirectResolution);
  }

  /**
   * This method generated a full URL from the redirect resolution
   *
   * @param redirectResolution
   * @return
   */
  @Nullable
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

  @Nullable
  public String getCodGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("CodGatewaySendReceiveAction.class");
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable
  public String getSelectAddressUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("SelectAddressAction.class");
    return getUrlFromResolution(redirectResolution);
  }

  public String getOrderTrackLink(String trackingId, Long courierId) {
    RedirectResolution redirectResolution = new RedirectResolution("TrackCourierAction.class")
        .addParameter("trackingId", trackingId)
        .addParameter("courierId", courierId);
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable
  public String getOrderInvoiceLink(Order order) {
    RedirectResolution redirectResolution = new RedirectResolution("SOInvoiceAction.class")
        .addParameter("order", order.getId());
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable
  public String getShippingOrderInvoiceLink(ShippingOrder shippingOrder) {
    RedirectResolution redirectResolution = new RedirectResolution("SOInvoiceAction.class")
        .addParameter("shippingOrder", shippingOrder.getId());
    return getUrlFromResolution(redirectResolution);
  }

  public String getRetailInvoiceLink(AccountingInvoice accountingInvoice) {
    RedirectResolution redirectResolution = new RedirectResolution("AccountingInvoiceAction.class")
        .addParameter("accountingInvoice", accountingInvoice.getId());
    return getUrlFromResolution(redirectResolution);
  }

  public String getCartUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("CartAction.class");
    return getUrlFromResolution(redirectResolution);
  }

   @Nullable public String getUserActivationLink(TempToken token) {
    RedirectResolution redirectResolution = new RedirectResolution("VerifyUserAction.class").addParameter("token", token.getToken());
    return getUrlFromResolution(redirectResolution);
  }

  public String getReferralSignupLink(User user) {
    RedirectResolution redirectResolution = new RedirectResolution("ReferralSignupAction.class").addParameter("userHash", user.getUserHash());
    return getUrlFromResolution(redirectResolution);
  }

   public String getReferralProgramUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("ReferralProgramAction.class");
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable public String getEmailUnsubscribeLink(EmailRecepient emailRecepient) {
    RedirectResolution redirectResolution = new RedirectResolution("UnsubscribeEmailAction.class")
        .addParameter("unsubscribeToken", emailRecepient.getUnsubscribeToken());
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable public String getViewTicketUrl(Ticket ticket) {
    RedirectResolution redirectResolution = new RedirectResolution("ViewAndEditTicketAction.class")
        .addParameter("ticket", ticket.getId());
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable public String getResetPasswordLink(TempToken token) {
    RedirectResolution redirectResolution = new RedirectResolution("PasswordResetAction.class").addParameter("token", token.getToken());
    return getUrlFromResolution(redirectResolution);
  }

  @Nullable public String getCitrusPaymentGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("CitrusGatewaySendReceiveAction.class");
    return getUrlFromResolution(redirectResolution);
   }
}
