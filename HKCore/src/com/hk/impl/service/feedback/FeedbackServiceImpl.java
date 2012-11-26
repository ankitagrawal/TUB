package com.hk.impl.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.feedback.FeedbackDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.feedback.FeedbackService;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/20/12
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	UserDao userDao;
	@Autowired
	FeedbackDao feedbackDao;

	public Feedback updateFeedback(Feedback feedback, Long recommendToFriends, Long customerServiceFeedback, Long websiteExperienceFeedback, String comments) {
		feedback.setRecommendToFriends(recommendToFriends);
		feedback.setCustomerCareExperience(customerServiceFeedback);
		feedback.setWebsiteExperience(websiteExperienceFeedback);
		feedback.setComments(comments);
		getFeedbackDao().save(feedback);
		return feedback;
	}

	public Feedback getOrCreateFeedbackForOrder(Order order) {
		Feedback feedback = getFeedbackDao().findByOrder(order);
		if(feedback == null) {
			feedback = new Feedback();
			feedback.setOrder(order);
			feedback.setUser(order.getUser());
			feedback.setFeedbackDate(new Date());
		}
		return feedback;
	}

	public Feedback createFeedbackForUser(User user, Long recommendToFriends, Long customerServiceFeedback, Long websiteExperienceFeedback, String comments) {
		Feedback feedback = new Feedback();
		feedback.setUser(user);
		feedback.setFeedbackDate(new Date());
		return updateFeedback(feedback, recommendToFriends, customerServiceFeedback, websiteExperienceFeedback, comments);
	}

	public FeedbackDao getFeedbackDao() {
		return feedbackDao;
	}

	public void setFeedbackDao(FeedbackDao feedbackDao) {
		this.feedbackDao = feedbackDao;
	}
}
