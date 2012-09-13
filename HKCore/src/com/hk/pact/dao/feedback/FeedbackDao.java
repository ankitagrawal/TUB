package com.hk.pact.dao.feedback;

import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/21/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FeedbackDao extends BaseDao{
	public Feedback findByOrder(Order order);
}
