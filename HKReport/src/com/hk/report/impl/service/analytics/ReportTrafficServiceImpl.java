package com.hk.report.impl.service.analytics;

import com.hk.report.pact.service.analytics.ReportTrafficService;
import com.hk.report.pact.dao.analytics.ReportTrafficDao;
import com.hk.report.dto.analytics.TrafficSrcPerformanceDto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 25 Nov, 2012
 * Time: 7:21:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReportTrafficServiceImpl implements ReportTrafficService {

	@Autowired
	ReportTrafficDao reportTrafficDao;

	@Override
	public List<TrafficSrcPerformanceDto> getTrafficSrcPerformanceDtoList() {
		return getReportTrafficDao().getTrafficSrcPerformanceDtoList();
	}

	public ReportTrafficDao getReportTrafficDao() {
		return reportTrafficDao;
	}
}
