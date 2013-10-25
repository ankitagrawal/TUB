package com.hk.manager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.hk.constants.core.Keys;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.catalog.product.ProductVariant;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.domain.TempToken;
import com.hk.domain.Ticket;
import com.hk.domain.accounting.AccountingInvoice;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.web.AppConstants;
import com.hk.web.filter.WebContext;

@Component
public class LinkManager {

  @Value("#{hkEnvProps['" + Keys.Env.userEmailLinksRedirected + "']}")
  private String userEmailLinksRedirected;
  @Value("#{hkEnvProps[" + Keys.Env.hybridRelease + "]}")
  private boolean hybridRelease;
  @Value("#{hkEnvProps['" + Keys.Env.USER_EMAIL_LINK_TO_BETA + "']}")
  private String userEmailLinkToBeta;

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

  public String getPaymentModesUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/PaymentMode.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getSelectAddressUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/user/SelectAddress.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getOrderTrackLink(String trackingId, Long courierId, ShippingOrder shippingOrder) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/order/TrackCourier.action").addParameter("trackingId", trackingId).addParameter("courierId",
        courierId).addParameter("shippingOrder", shippingOrder);
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getOrderInvoiceLink(Order order) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/SOInvoice.action").addParameter("order", order.getId());
    return getUrlFromResolution(redirectResolution);
  }

  public String getShippingOrderInvoiceLink(ShippingOrder shippingOrder) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/SOInvoice.action").addParameter("shippingOrder", shippingOrder.getId());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getRetailInvoiceLink(AccountingInvoice accountingInvoice) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/accounting/AccountingInvoice.action").addParameter("accountingInvoice", accountingInvoice.getId());
    return getUrlFromResolution(redirectResolution);
  }

  public String getCartUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/cart/Cart.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getReviewPageLink(HashMap params) {
    if (isHybridRelease()) {
      Map<String, String> hkWebParams = new HashMap<String, String>();
      hkWebParams.put("reviewCrud","");
      hkWebParams.put("productVariantId",params.get("productVariant").toString());
      RedirectResolution redirectResolution = new RedirectResolution("/beta/review/StoreVariantReview.action").addParameters(hkWebParams);
      return userEmailLinkToBeta + redirectResolution.getUrl(Locale.getDefault());
    }
    RedirectResolution redirectResolution = new RedirectResolution("/core/catalog/product/ProductReview.action").addParameters(params);
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getUnsubscribeLink(User user) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/email/HKUnsubscribeEmail.action").addParameter("unsubscribeToken", user.getUnsubscribeToken());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getUserActivationLink(TempToken token) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/user/VerifyUser.action").addParameter("token", token.getToken());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getReferralSignupLink(User user) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/referral/ReferralSignup.action").addParameter("userHash", user.getUserHash());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getReferralProgramUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/referral/ReferralProgram.action");
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getEmailUnsubscribeLink(EmailRecepient emailRecepient) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/email/UnsubscribeEmail.action").addParameter("unsubscribeToken", emailRecepient.getUnsubscribeToken());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getViewTicketUrl(Ticket ticket) {
    RedirectResolution redirectResolution = new RedirectResolution("/admin/ticket/ViewAndEditTicket.action").addParameter("ticket", ticket.getId());
    return getUrlFromResolution(redirectResolution);
  }

  public String getResetPasswordLink(TempToken token) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/user/PasswordReset.action").addParameter("token", token.getToken());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getSSOResetPasswordLink(TempToken token) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/user/SSOPasswordReset.action").addParameter("token", token.getToken());
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getCitrusPaymentNetBankingGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/CitrusNetbankingSendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getCitrusPaymentGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/CitrusGatewaySendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getIciciPaymentGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/IciciGatewaySendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getCitrusPaymentCreditDebitGatewayUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/CitrusCreditDebitSendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getEbsPaymentGatewayReturnUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/EbsSendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }

  public String getCodConverterLink(Order order) {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/RegisterOnlinePayment.action").addParameter("order", order);
    return getUrlFromResolution(redirectResolution);
  }

  public String getPayPalPaymentGatewayReturnUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/gateway/PayPalCreditDebitSendReceive.action");
    return getUrlFromResolution(redirectResolution);
  }


  public String getPayPalPaymentGatewayCancelUrl() {
    RedirectResolution redirectResolution = new RedirectResolution("/core/payment/PaymentMode.action");
    return getUrlFromResolution(redirectResolution);
  }


  public String getRelativeProductURL(Product product, Long productReferrerId) {
    String productURL = null;
    String productSlug = product.getSlug();
    String productId = product.getId();
    productURL = "/product/" + productSlug + "/" + productId;

    if (productReferrerId != null && productReferrerId != 0) {
      productURL = productURL.concat("?" + HealthkartConstants.URL.productReferrerId + "=" + productReferrerId);
    }

    return productURL;

  }

  public String getProductURL(Product product, Long productReferrerId) {

    String productURL = null;
    String productSlug = product.getSlug();
    String productId = product.getId();
    productURL = "/product/" + productSlug + "/" + productId;

    RedirectResolution redirectResolution = new RedirectResolution(productURL);
    if (productReferrerId != null && productReferrerId != 0) {
      redirectResolution.addParameter(HealthkartConstants.URL.productReferrerId, productReferrerId);
    }

    return getUrlFromResolution(redirectResolution);

        /*
         * RedirectResolution redirectResolution = new RedirectResolution(ProductAction.class). addParameter("referrer",
         * referrerId). addParameter("productId", productId).addParameter("productSlug", productSlug); return
         * getUrlFromResolution(redirectResolution);
         */

  }

  public String getFeedbackPage() {
    RedirectResolution redirectResolution = new RedirectResolution("/feedback");
    return userEmailLinksRedirected + redirectResolution.getUrl(Locale.getDefault());
  }

  public String getTryOnImageURL(ProductVariant productVariant) {

    String tryOnURL = "/makeover/tryOn.jsp";

    RedirectResolution redirectResolution = new RedirectResolution(tryOnURL);
    if (productVariant != null) {
      redirectResolution.addParameter("productid", productVariant);
//            redirectResolution.addParameter("type", productVariant.getProduct().getSecondaryCategory());
    }

    return getUrlFromResolution(redirectResolution);
  }

  public boolean isHybridRelease() {
    return hybridRelease;
  }
}
