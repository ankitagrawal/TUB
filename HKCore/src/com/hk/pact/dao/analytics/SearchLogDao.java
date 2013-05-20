package com.hk.pact.dao.analytics;

import com.hk.domain.search.SearchLog;
import com.hk.pact.dao.BaseDao;

public interface SearchLogDao extends BaseDao {

	public SearchLog getLatestSearchLog(Long trafficTrackingId);

}