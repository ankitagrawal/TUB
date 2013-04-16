package com.hk.loyaltypg.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.loyaltypg.dao.UserOrderKarmaProfileDao;

@Repository
public class UserOrderKarmaProfileDaoImpl extends BaseDaoImpl implements UserOrderKarmaProfileDao {

	/* (non-Javadoc)
	 * @see com.hk.loyaltypg.dao.UserOrderKarmaProfileDao#listKarmaPointsForUser(com.hk.domain.user.User, int, int)
	 */
	@Override
	public Page listKarmaPointsForUser(User user, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserOrderKarmaProfile.class);
        criteria.add(Restrictions.eq("userOrderKey.user.id", user.getId()));
        //criteria.add(Restrictions.eq("userOrderKey", userOKey));
        criteria.addOrder(org.hibernate.criterion.Order.desc("creationTime"));
        return this.list(criteria, page, perPage);
    }

	@Override
	public Map<String, String> getUserPointsData(Long userId) {
		Map<String,String> pointsMap = new HashMap<String, String>();
		
		
		return pointsMap;
	}

}
