package com.hk.loyaltypg.dao;

import com.akube.framework.dao.Page;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface UserOrderKarmaProfileDao extends BaseDao {

	
	/**
	 * This method is used to return customer karma points based on user, page and page number.
	 * @param user
	 * @param page
	 * @param perPage
	 * @return
	 */
	public Page listKarmaPointsForUser(User user, int page, int perPage);
	
}
