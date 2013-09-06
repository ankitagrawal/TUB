package com.hk.web.action.pages;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.service.feedback.FeedbackService;
import com.hk.pact.service.order.OrderService;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA. User: Rohit Date: 8/20/12 Time: 4:54 PM To change this template use File | Settings |
 * File Templates.
 */

@UrlBinding("/feedback")
@Component
public class FeedbackAction extends BaseAction {
    Order order;
    private Long recommendToFriends;
    private Long websiteExperienceFeedback;
    private Long customerServiceFeedback;
    private String comments;
    private Long baseOrderId;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    OrderService orderService;

    @DefaultHandler
    public Resolution pre() {
        if (baseOrderId != null) {
            order = orderService.find(baseOrderId);
        }
        if (order != null) {
            Feedback feedback = getFeedbackService().getOrCreateFeedbackForOrder(order);
            getFeedbackService().updateFeedback(feedback, recommendToFriends, customerServiceFeedback, websiteExperienceFeedback, comments);
        }
        return new ForwardResolution("/pages/static/feedback.jsp");
    }

    public Resolution save() {
        User user = getPrincipalUser();
        if (order != null) {
            Feedback feedback = getFeedbackService().getOrCreateFeedbackForOrder(order);
            getFeedbackService().updateFeedback(feedback, recommendToFriends, customerServiceFeedback, websiteExperienceFeedback, comments);
            return new ForwardResolution("/pages/static/feedbackCapture.jsp");
        } else if (user != null) {
            getFeedbackService().createFeedbackForUser(user, recommendToFriends, customerServiceFeedback, websiteExperienceFeedback, comments);
            return new ForwardResolution("/pages/static/feedbackCapture.jsp");
        } else {
            addRedirectAlertMessage(new SimpleMessage("Login to the site to give your feedback."));
        }
        return new ForwardResolution("/pages/static/feedback.jsp");
    }

    public Long getWebsiteExperienceFeedback() {
        return websiteExperienceFeedback;
    }

    public void setWebsiteExperienceFeedback(Long websiteExperienceFeedback) {
        this.websiteExperienceFeedback = websiteExperienceFeedback;
    }

    public Long getCustomerServiceFeedback() {
        return customerServiceFeedback;
    }

    public void setCustomerServiceFeedback(Long customerServiceFeedback) {
        this.customerServiceFeedback = customerServiceFeedback;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public FeedbackService getFeedbackService() {
        return feedbackService;
    }

    public void setFeedbackService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getRecommendToFriends() {
        return recommendToFriends;
    }

    public void setRecommendToFriends(Long recommendToFriends) {
        this.recommendToFriends = recommendToFriends;
    }

    public Long getBaseOrderId() {
        return baseOrderId;
    }

    public void setBaseOrderId(Long baseOrderId) {
        this.baseOrderId = baseOrderId;
    }
}
