package com.hk.web.action.core.user;

import java.util.Arrays;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.RewardPointService;
import com.hk.web.HealthkartResponse;

@Component
public class PublishOnFBAction extends BaseAction {

    private static Logger      logger = LoggerFactory.getLogger(PublishOnFBAction.class);
    @Autowired
    private RewardPointService rewardPointService;

    private Order              order;

    @DefaultHandler
    public Resolution pre() {
        logger.debug("orderId: " + order.getId());
        return new ForwardResolution("/pages/modal/publishOnFB.jsp");
    }

    @JsonHandler
    public Resolution share() {
        logger.debug("orderId: " + order.getId());
        if (getRewardPointService().findByReferredOrderAndRewardMode(order, getRewardPointService().getRewardPointMode(EnumRewardPointMode.FB_SHARING)) == null
                && order.getPayment().getAmount() > 500.0) {
            RewardPoint fbRewardPoint = getRewardPointService().addRewardPoints(order.getUser(), null, order, 50.0, "FB Order Sharing", EnumRewardPointStatus.PENDING,
                    getRewardPointService().getRewardPointMode(EnumRewardPointMode.FB_SHARING));
            rewardPointService.approveRewardPoints(Arrays.asList(fbRewardPoint), new DateTime().plusMonths(1).toDate());
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Published on facebook. Reward points added.");
            return new JsonResolution(healthkartResponse);
        } else {
            logger.info("Already published or an Error.");
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Already Published or Lower Amount. Not adding reward points.");
            return new JsonResolution(healthkartResponse);
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

}