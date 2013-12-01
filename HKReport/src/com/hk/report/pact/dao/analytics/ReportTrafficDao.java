package com.hk.report.pact.dao.analytics;

import com.hk.report.dto.analytics.TrafficSrcPerformanceDto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 25 Nov, 2012
 * Time: 7:25:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportTrafficDao {

	List<TrafficSrcPerformanceDto> getTrafficSrcPerformanceDtoList();
}
