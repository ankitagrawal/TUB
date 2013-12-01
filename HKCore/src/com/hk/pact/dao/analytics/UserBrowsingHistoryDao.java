package com.hk.pact.dao.analytics;

import com.hk.domain.analytics.UserBrowsingHistory;
import com.hk.pact.dao.BaseDao;

public interface UserBrowsingHistoryDao extends BaseDao {

	public UserBrowsingHistory getUserBrowsingHistory(Long trafficTrackingId, String pageUrl);

}