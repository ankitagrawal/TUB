package com.hk.web.action.core.payment.gateway;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.exception.CitruspayException;
import com.ecs.epg.sfa.java.*;
import com.hk.constants.payment.EnumCitrusResponseCodes;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.CitrusPaymentGatewayWrapper;
import com.hk.manager.payment.IciciPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import com.hk.web.filter.WebContext;
import com.opus.epg.sfa.java.*;
import com.opus.epg.sfa.java.BillToAddress;
import com.opus.epg.sfa.java.MPIData;
import com.opus.epg.sfa.java.Merchant;
import com.opus.epg.sfa.java.PGReserveData;
import com.opus.epg.sfa.java.PGResponse;
import com.opus.epg.sfa.java.PostLib;
import com.opus.epg.sfa.java.ShipToAddress;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/3/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class IciciGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<IciciPaymentGatewayWrapper> {
    private static Logger logger = LoggerFactory.getLogger(CitrusNetbankingSendReceiveAction.class);

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    EmailManager emailManager;

    protected IciciPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
        IciciPaymentGatewayWrapper iciciPaymentGatewayWrapper = new IciciPaymentGatewayWrapper(AppConstants.appBasePath);
        Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());
        User user = payment.getOrder().getUser();
        Address address= payment.getOrder().getAddress();

        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/icici.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
//        BillToAddress oBTA 	= new BillToAddress();
        Merchant oMerchant 	= new Merchant();
        PostLib oPostLib	= null;
/*
        oBTA.setAddressDetails(
                "CID"
                , address.getName()
                , address.getLine1()
                , address.getLine2()
                , ""
                , address.getCity()
                , address.getState()
                , address.getPin()
                , "IND"
                , user.getEmail()
        );
*/
        String MerchantId = properties.getProperty("MerchantId");
        oMerchant.setMerchantDetails(MerchantId,MerchantId,MerchantId,"127.0.0.1",
                payment.getGatewayOrderId(),
                payment.getGatewayOrderId()
                , linkManager.getIciciPaymentGatewayUrl(),
                properties.getProperty("ResponseMethod"), properties.getProperty("CurrCode"), payment.getGatewayOrderId(), "req.Sale",
                amountStr, "GMT+05:30", "Ext1", "true", "Ext3", "Ext4", "Ext5a");


//        PGResponse oPGResponse = oPostLib.postSSL(null,oSTA,oMerchant,oMPI,WebContext.getResponse(),oPGReserveData,oCustomer,oSessionDetail,oAirLineTrans,null);
        try {
            oPostLib = new PostLib();
            PGResponse oPGResponse = oPostLib.postSSL(null,null,oMerchant,null,WebContext.getResponse(),null,null,null,null,null);
//            PGResponse oPGResponse = oPostLib.postSSL(oBTA,oSTA,oMerchant,oMPI,WebContext.getResponse(),oPGReserveData,oCustomer,oSessionDetail,oAirLineTrans,null);
            if(oPGResponse.getRedirectionUrl() != null) {
                String strRedirectionURL = oPGResponse.getRedirectionUrl();
                iciciPaymentGatewayWrapper.setGatewayUrl(strRedirectionURL);
                logger.info("Icici url being generated is " + linkManager.getIciciPaymentGatewayUrl());
            }
            else {
                logger.info("Error encountered. Error Code : " + oPGResponse.getRespCode() + " . Message " + oPGResponse.getRespMessage());
                paymentManager.fail(data.getGatewayOrderId());
            }
        } catch (Exception e) {
            paymentManager.fail(data.getGatewayOrderId());
        }

        return iciciPaymentGatewayWrapper;
    }

    @Override
    @DefaultHandler
    public Resolution callback() {
//        logger.info("in icici callback -> " + getContext().getRequest().getParameterMap());

        String data = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.param_data);
        String responseMethod = getContext().getRequest().getMethod();
        String propertyFilePath = AppConstants.getAppClasspathRootPath() + "/icici.live.properties";

        String validatedData = "";

        if (data != null) {
            logger.info("returning from payment gateway Icici with the parameter string msg : " + data);
            try {
                validatedData = IciciPaymentGatewayWrapper.validateEncryptedData(data, propertyFilePath);
            } catch (Exception e) {
                paymentManager.fail("");
                logger.info("Payment failed while decrypting data in icici backend");
            }
        }

        Map<String, String> paramMap = IciciPaymentGatewayWrapper.parseResponse(validatedData, responseMethod);

        logger.info("validated date -> " + validatedData);
//        logger.info("param map->" + paramMap);
        String amountStr = paramMap.get(CitrusPaymentGatewayWrapper.Amount);
        Double amount = NumberUtils.toDouble(amountStr);
        String authStatus = paramMap.get(CitrusPaymentGatewayWrapper.RespCode);
        String responseMsg = ((String) paramMap.get(CitrusPaymentGatewayWrapper.Message)).replace('+', ' ');
        String gatewayOrderId = paramMap.get(CitrusPaymentGatewayWrapper.TxnID);

        logger.info("response msg received from citrus is " + responseMsg + "for gateway order id " + gatewayOrderId);

        String merchantParam = null;

        Resolution resolution = null;
        try {

            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

            logger.info("Status returned from Icici Payment Gateway" + authStatus);

            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if (authStatus.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
                paymentManager.success(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (EnumCitrusResponseCodes.Rejected_By_Issuer.getId().equals(authStatus)) {
                paymentManager.fail(gatewayOrderId);
                emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
                resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        } catch (HealthkartPaymentGatewayException e) {
            emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
            paymentManager.error(gatewayOrderId, e);
            resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
        }
        return resolution;
    }

    public String getSecureCookie(HttpServletRequest request) {
        String secureCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals("vsc")) {
                    secureCookie = cooky.getValue().trim();
                    break;
                }
            }
        }
        return secureCookie;
    }

}
