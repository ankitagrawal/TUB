package com.hk.web.action.core.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.ecs.epg.sfa.java.Merchant;
import com.ecs.epg.sfa.java.PGResponse;
import com.ecs.epg.sfa.java.PostLib;
import com.hk.constants.payment.EnumCitrusResponseCodes;
import com.hk.dao.payment.PaymentDao;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.CitrusTestPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;

@Component
public class CitrusGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<CitrusTestPaymentGatewayWrapper> {

  private static Logger logger = LoggerFactory.getLogger(CitrusGatewaySendReceiveAction.class);

  
  PaymentDao paymentDao;
  
  PaymentManager paymentManager;
  
  LinkManager linkManager;
   //@Named(Keys.App.environmentDir) 
   String environmemtDir;
   EmailManager emailManager;

  protected CitrusTestPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
    CitrusTestPaymentGatewayWrapper citrusTestPaymentGatewayWrapper = new CitrusTestPaymentGatewayWrapper(environmemtDir);
    Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
    String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

    String propertyLocatorFileLocation = environmemtDir + "/citrus.live.properties";
    Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

    Merchant oMerchant = new Merchant();
    com.ecs.epg.sfa.java.IssuerData oIssuerData = new com.ecs.epg.sfa.java.IssuerData();
    com.ecs.epg.sfa.java.PGReserveData oPGReserveData = new com.ecs.epg.sfa.java.PGReserveData();

    Map testMap = new HashMap();
    testMap.put("abc", 1);

    oPGReserveData.setAdditionalData((HashMap) testMap);

    PostLib oPostLib = null;
    try {
      oPostLib = new PostLib("ABC");
    } catch (Exception e) {
      logger.info("some exception");
    }

    oMerchant.setMerchantDetails(
        properties.getProperty("MerchantId")
        , properties.getProperty("MerchantId")
        , properties.getProperty("MerchantId")
        , "127.0.0.1"
        , payment.getGatewayOrderId()
        , payment.getGatewayOrderId()
//        , "http://www.healthkart.com/CitrusGatewaySendReceiveAction.action"
        , linkManager.getCitrusPaymentGatewayUrl()
        , properties.getProperty("ResponseMethod")
        , properties.getProperty("CurrCode")
        , payment.getGatewayOrderId()
        , "P"
        , amountStr
        , "GMT+05:30"
        , "Ext1"
        , "true"
        , "Ext3"
        , "Ext4"
        , "Ext5a"
    );

    /*added for NetBanking Transaction*/
    oIssuerData.setIssuerDetails(data.getPaymentMethod());

    PGResponse oPgResp = oPostLib.postNBMOTO(null, null, oMerchant, oPGReserveData, oIssuerData);

    if (oPgResp.getRedirectionUrl() != null) {
      String strRedirectionURL = oPgResp.getRedirectionUrl();
      citrusTestPaymentGatewayWrapper.setGatewayUrl(strRedirectionURL);
    } else {
      logger.info("Error encountered. Error Code : " + oPgResp.getRespCode() + " . Message " + oPgResp.getRespMessage());
    }
    logger.info("sending to payment gateway Citrus");

    return citrusTestPaymentGatewayWrapper;
  }

  @DefaultHandler
  public Resolution callback() {
    /*
      now do all sorts of verifications before proceeding
      1. do gateway specific validations first
      2. check is this payment status is already success/fail
      3. get order id from this and generate a checksum, the compare checksum value with the one in payment
      4. verify ip (should be same)
      5. see if the order is already paid for (raise a double payment alert to admins)

      if payment is verified, then proceed with other stuff
      - change order status to reflect payment
      - fire transaction success emails with invoice
      - generate order
      - finally redirect to the required page (success, fail, authPending, double payment, etc)
    */

    String data = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.param_data);
    String responseMethod = getContext().getRequest().getMethod();
    String propertyFilePath = environmemtDir + "/citrus.live.properties";
    String validatedData = "";

    if (data != null) {
      logger.info("returning from payment gateway Citrus with the parameter string msg : " + data);
      // citrus specific validation
      validatedData = CitrusTestPaymentGatewayWrapper.validateEncryptedData(data, propertyFilePath);
    }
/*
    String authStatus = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.RespCode);
    String issuerRefNo = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.RRN);
    String gatewayOrderId = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.TxnID);
    String responseMsg = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.Message);
    String txnRefNo = getContext().getRequest().getParameter(CitrusTestPaymentGatewayWrapper.ePGTxnID);
*/

    Map<String, String> paramMap = CitrusTestPaymentGatewayWrapper.parseResponse(validatedData, responseMethod);

    String amountStr = paramMap.get(CitrusTestPaymentGatewayWrapper.Amount);
    Double amount = NumberUtils.toDouble(amountStr);
    String authStatus = paramMap.get(CitrusTestPaymentGatewayWrapper.RespCode);
    String responseMsg = ((String) paramMap.get(CitrusTestPaymentGatewayWrapper.Message)).replace('+', ' ');
    String gatewayOrderId = paramMap.get(CitrusTestPaymentGatewayWrapper.TxnID);
    String authIdCode = paramMap.get(CitrusTestPaymentGatewayWrapper.AuthIdCode);
    String issuerRefNo = paramMap.get(CitrusTestPaymentGatewayWrapper.RRN);
    String txnRefNo = paramMap.get(CitrusTestPaymentGatewayWrapper.ePGTxnID);

    logger.info("response msg received from citrus is " + responseMsg   + "for gateway order id " + gatewayOrderId);

    String merchantParam = null;

    Resolution resolution = null;
    try {

      //our own validations
      paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

      logger.info("Status returned from Citrus Paymnet Gateway" + authStatus);

      // payment callback has been verified. now see if it is successful or failed from the gateway response
      if (authStatus.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
        paymentManager.success(gatewayOrderId);
        resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
      } /*else if (EnumCitrusResponseCodes.Rejected_By_Issuer.getId().equals(authStatus)) {
        paymentManager.pendingApproval(gatewayOrderId);
        resolution = new RedirectResolution(PaymentPendingApprovalAction.class).addParameter("gatewayOrderId", gatewayOrderId);
      } */ else if (EnumCitrusResponseCodes.Rejected_By_Issuer.getId().equals(authStatus)) {
        paymentManager.fail(gatewayOrderId);
        emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
        resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
      } else {
        emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
        throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
      }
    } catch (HealthkartPaymentGatewayException e) {
      emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
      paymentManager.error(gatewayOrderId, e);
      resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
    }
    return resolution;
  }


}
