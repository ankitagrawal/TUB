package com.hk.impl.dao.feedback;

import com.hk.domain.feedback.Feedback;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.feedback.FeedbackDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 8/21/12
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class FeedbackDaoImpl extends BaseDaoImpl implements FeedbackDao{
	public Feedback findByOrder(Order order) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Feedback.class);
		criteria.add(Restrictions.eq("order", order));
		List<Feedback> feedbackList = findByCriteria(criteria);
		if(feedbackList != null && feedbackList.size() > 0) {
			return feedbackList.get(0);
		}
		return null;
	}

}
