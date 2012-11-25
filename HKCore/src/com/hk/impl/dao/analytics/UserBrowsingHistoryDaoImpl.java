package com.hk.impl.dao.analytics;

import com.hk.domain.analytics.UserBrowsingHistory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.analytics.UserBrowsingHistoryDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserBrowsingHistoryDaoImpl extends BaseDaoImpl implements UserBrowsingHistoryDao {

	public UserBrowsingHistory getUserBrowsingHistory(Long trafficTrackingId, String productId) {
		String queryString = "from UserBrowsingHistory ubh where ubh.trafficTrackingId=:trafficTrackingId and ubh.productId=:productId";
		return (UserBrowsingHistory) findUniqueByNamedParams(queryString, new String[]{"trafficTrackingId", "productId"}, new Object[]{trafficTrackingId, productId});
	}

}