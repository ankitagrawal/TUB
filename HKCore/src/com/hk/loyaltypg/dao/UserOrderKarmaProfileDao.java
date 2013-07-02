package com.hk.loyaltypg.dao;

import java.util.List;
import java.util.Map;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
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

	
	/**
	 * This method is used to search customer karma profile based on given parameters.
	 * @param searchMap
	 * @return
	 */
	public List<UserOrderKarmaProfile> searchUserOrderKarmaProfile(Map<String, Object> searchMap);
	
}
