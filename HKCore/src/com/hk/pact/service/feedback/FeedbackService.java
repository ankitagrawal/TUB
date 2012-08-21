package com.hk.pact.service.feedback;

import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/20/12
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FeedbackService {

	public Feedback getOrCreateFeedbackForOrder(Order order);

	public Feedback updateFeedback(Feedback feedback, Long recommendToFriends, Long customerServiceFeedback, Long websiteExperienceFeedback, String comments);
}
