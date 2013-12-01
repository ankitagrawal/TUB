package com.hk.report.batch;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.manager.EmailManager;
import com.hk.report.dto.order.CategoriesOrderReportDto;
import com.hk.report.manager.ReportManager;
import com.hk.web.BatchProcessWorkManager;


/**
 * Created by IntelliJ IDEA.
 * User: Developer
 * Date: Jan 12, 2012
 * Time: 9:07:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SixHourlyCategorySalesReportJob implements StatefulJob {

	
	BatchProcessWorkManager batchProcessWorkManager;
	
	ReportManager reportManager;
	
	EmailManager emailManager;

	@SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(SixHourlyCategorySalesReportJob.class);

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		@SuppressWarnings("unused")
        Map<String, CategoriesOrderReportDto> sixHourlycategoriesOrderReportDtosMap = new HashMap<String, CategoriesOrderReportDto>();

/*
		try {
			batchProcessWorkManager.beginWork();
			log.info("=====================================SixHourlyCategoryPerformanceReportJob==============================================");
			sixHourlycategoriesOrderReportDtosMap = reportManager.generateSixHourlyCategoryPerformaceReportUI();
			emailManager.sendSixHourlyCategorySalesReportToCategoryManager(sixHourlycategoriesOrderReportDtosMap);
		} finally {
			batchProcessWorkManager.endWork();
		}
*/

	}
}
