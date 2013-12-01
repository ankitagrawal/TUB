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

public class DailyCategorySalesReportJob implements StatefulJob {

  
  BatchProcessWorkManager batchProcessWorkManager;
  
  ReportManager reportManager;
  
  EmailManager emailManager;

  @SuppressWarnings("unused")
private static Logger log = LoggerFactory.getLogger(DailyCategorySalesReportJob.class);

  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    @SuppressWarnings("unused")
    Map<String, CategoriesOrderReportDto> categoriesOrderReportDtosMap = new HashMap<String, CategoriesOrderReportDto>();

/*
    try {
      batchProcessWorkManager.beginWork();
      log.info("=====================================DailyCategoryPerformanceReportJob==============================================");
      categoriesOrderReportDtosMap = reportManager.generateDailyCategoryPerformaceReportUI();
      emailManager.sendDailyCategorySalesReportToCategoryManager(categoriesOrderReportDtosMap);
    } finally {
      batchProcessWorkManager.endWork();
    }
*/

  }

}
