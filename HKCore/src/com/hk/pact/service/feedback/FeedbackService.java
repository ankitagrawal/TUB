package com.hk.pact.service.feedback;

import com.hk.domain.order.Order;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/20/12
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FeedbackService {

	public void createFeedbackForOrder(Order order, Long recommendToFriends, Long customerServiceFeedback,
	                                   Long websiteExperienceFeedback, String comments);
}
