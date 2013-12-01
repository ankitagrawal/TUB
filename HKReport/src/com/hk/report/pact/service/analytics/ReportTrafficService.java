package com.hk.report.pact.service.analytics;

import com.hk.report.dto.analytics.TrafficSrcPerformanceDto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 25 Nov, 2012
 * Time: 7:20:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportTrafficService {

	List<TrafficSrcPerformanceDto> getTrafficSrcPerformanceDtoList();
}
