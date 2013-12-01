package com.hk.report.batch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.manager.EmailManager;
import com.hk.report.manager.ReportManager;
import com.hk.web.BatchProcessWorkManager;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 20, 2011
 * Time: 11:22:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DailyGoogleAdsBannedWordsSearchJob implements StatefulJob{


  
  BatchProcessWorkManager batchProcessWorkManager;
  
  ReportManager reportManager;
  
  EmailManager emailManager;

  @SuppressWarnings("unused")
private static Logger log = LoggerFactory.getLogger(DailyGoogleAdsBannedWordsSearchJob.class);

  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

 /*   try {
      batchProcessWorkManager.beginWork();
      log.info("=====================================DailyGoogleAdsBannedWordsSearchJob==============================================");
      List<GoogleBannedWordDto> googleBannedWordDtoList = new ArrayList<GoogleBannedWordDto>();
      googleBannedWordDtoList = reportManager.generateDailyGoogleAdsBannedWords();
      if(!googleBannedWordDtoList.isEmpty()) {
        emailManager.sendDailyGoogleAdsBannedWords(googleBannedWordDtoList);
      }

    } finally {
      batchProcessWorkManager.endWork();
    }*/
  }

}
