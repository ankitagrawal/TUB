package com.hk.web.action.admin.hkbridge;

import com.akube.framework.stripes.action.BaseAction;
import com.google.gson.Gson;
import com.hk.constants.core.Keys;

import com.hk.hkjunction.observers.OrderLifecycleSummary;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/10/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserCallResponseSummaryAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(UserCallResponseSummaryAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.hkBridgeRestUrl + "']}")
    String hkBridgeRestUrl;

    private int totalCODCount;
    private int totalEfforBpoCODCount;
    private int totalEfforBpoCODConfirmedCount;
    private int totalEfforBpoCODCancelledCount;
    private int totalEfforBpoPaymentFailureCount;
    private int totalKnowlarityCODConfirmedCount;
    private int totalKnowlarityCODCancelledCount;
    private Date startDate;
    private Date endDate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/hkbridge/userCallResponseDashboard.jsp");
    }

    public Resolution getUserCallResponseSummary() {

        try {
            String urlStr = hkBridgeRestUrl + "summary";
            ClientRequest request = new ClientRequest(urlStr);
            String startDateString = simpleDateFormat.format(startDate);
            String endDateString = simpleDateFormat.format(endDate);
            request.getQueryParameters().add("startDate", startDateString);
            request.getQueryParameters().add("endDate", endDateString);
            ClientResponse response = request.get();
            int status = response.getStatus();
            logger.info("Calling Knowlarity Rest Api For Call Summary " + urlStr);
            if (status == 200) {
                String data = (String) response.getEntity(String.class);
                OrderLifecycleSummary orderLifecycleSummary = new Gson().fromJson(data, OrderLifecycleSummary.class);
                totalCODCount = orderLifecycleSummary.getTotalCODCount();
                totalEfforBpoCODCount = orderLifecycleSummary.getTotalEfforBpoCODCount();
                totalEfforBpoCODConfirmedCount = orderLifecycleSummary.getTotalEfforBpoCODConfirmedCount();
                totalEfforBpoCODCancelledCount = orderLifecycleSummary.getTotalEfforBpoCODCancelledCount();
                totalEfforBpoPaymentFailureCount = orderLifecycleSummary.getTotalEfforBpoPaymentFailureCount();
                totalKnowlarityCODConfirmedCount = orderLifecycleSummary.getTotalKnowlarityCODConfirmedCount();
                totalKnowlarityCODCancelledCount = orderLifecycleSummary.getTotalKnowlarityCODCancelledCount();


            } else {
                addRedirectAlertMessage(new SimpleMessage("Error In Fetching Data"));
                logger.error("Unable to update order status.." + Integer.toString(status));
            }

        } catch (Exception ex) {
            addRedirectAlertMessage(new SimpleMessage("Exception In Fetching Data"));
            logger.error("Exveption " + ex.getStackTrace());
        }

        return new ForwardResolution("/pages/admin/hkbridge/userCallResponseDashboard.jsp");

    }

    public int getTotalCODCount() {
        return totalCODCount;
    }

    public void setTotalCODCount(int totalCODCount) {
        this.totalCODCount = totalCODCount;
    }

    public int getTotalEfforBpoCODCount() {
        return totalEfforBpoCODCount;
    }

    public void setTotalEfforBpoCODCount(int totalEfforBpoCODCount) {
        this.totalEfforBpoCODCount = totalEfforBpoCODCount;
    }

    public int getTotalEfforBpoCODConfirmedCount() {
        return totalEfforBpoCODConfirmedCount;
    }

    public void setTotalEfforBpoCODConfirmedCount(int totalEfforBpoCODConfirmedCount) {
        this.totalEfforBpoCODConfirmedCount = totalEfforBpoCODConfirmedCount;
    }

    public int getTotalEfforBpoCODCancelledCount() {
        return totalEfforBpoCODCancelledCount;
    }

    public void setTotalEfforBpoCODCancelledCount(int totalEfforBpoCODCancelledCount) {
        this.totalEfforBpoCODCancelledCount = totalEfforBpoCODCancelledCount;
    }

    public int getTotalEfforBpoPaymentFailureCount() {
        return totalEfforBpoPaymentFailureCount;
    }

    public void setTotalEfforBpoPaymentFailureCount(int totalEfforBpoPaymentFailureCount) {
        this.totalEfforBpoPaymentFailureCount = totalEfforBpoPaymentFailureCount;
    }

    public int getTotalKnowlarityCODConfirmedCount() {
        return totalKnowlarityCODConfirmedCount;
    }

    public void setTotalKnowlarityCODConfirmedCount(int totalKnowlarityCODConfirmedCount) {
        this.totalKnowlarityCODConfirmedCount = totalKnowlarityCODConfirmedCount;
    }

    public int getTotalKnowlarityCODCancelledCount() {
        return totalKnowlarityCODCancelledCount;
    }

    public void setTotalKnowlarityCODCancelledCount(int totalKnowlarityCODCancelledCount) {
        this.totalKnowlarityCODCancelledCount = totalKnowlarityCODCancelledCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

