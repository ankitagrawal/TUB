package com.hk.web.action.pages;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.pact.service.feedback.FeedbackService;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/20/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */

@UrlBinding("/feedback")
@Component
public class FeedbackAction extends BaseAction {
	Order order;
	private Long recommendToFriends;
	private Long websiteExperienceFeedback;
	private Long customerServiceFeedback;
	private String comments;
	@Autowired
	FeedbackService feedbackService;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/static/feedback.jsp");
	}

	public Resolution save() {
		if (order != null) {
			Feedback feedback = getFeedbackService().getOrCreateFeedbackForOrder(order);
			getFeedbackService().updateFeedback(feedback, recommendToFriends, customerServiceFeedback, websiteExperienceFeedback, comments);
			addRedirectAlertMessage(new SimpleMessage("Thanks for your feedback, your feedback has been captured."));
		} else {
			addRedirectAlertMessage(new SimpleMessage("Feedback should be against any order, give the feedback using the order delivered email"));
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
}
