package com.hk.impl.service.feedback;

import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	BaseDao baseDao;

	public void createFeedbackForOrder(Order order, Long recommendToFriends, Long customerServiceFeedback,
	                                   Long websiteExperienceFeedback, String comments) {
		User user = userDao.getUserById(1L);
		Feedback feedback = new Feedback();
		feedback.setUser(user);
		feedback.setCustomerCareExperience(customerServiceFeedback);
		feedback.setWebsiteExperience(websiteExperienceFeedback);
		feedback.setComments(comments);
		getBaseDao().save(feedback);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
