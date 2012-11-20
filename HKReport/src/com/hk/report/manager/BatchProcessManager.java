package com.hk.report.manager;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class BatchProcessManager {

    private static Logger       log                                            = LoggerFactory.getLogger(BatchProcessManager.class);

    private final Scheduler     scheduler                                      = null;

    private static final String dailyCategorySalesReportGenerationCronTime     = "0 1 0 ? * *";                                     // sec
                                                                                                                                    // ||
    private static final String sixHourlyCategorySalesReportGenerationCronTime = "0 2 0,6,12,18 ? * *";

    private static final String dailyBadWordSearchCronTime                     = "0 30 17 ? * *";

    // TODO: rewrite
    /*
     * public BatchProcessManager(final SchedulerFactory schedulerFactory) //,final GuiceJobFactory guiceJobFactory)
     * throws SchedulerException { log.info("------- Initializing Batch Processer-------------------"); scheduler =
     * schedulerFactory.getScheduler(); //scheduler.setJobFactory(guiceJobFactory); }
     */

    public void start() {

        try {
            log.info("------- Scheduling Jobs ----------------");
            // jobs

            // TODO: uncomment later
            /*
             * JobDetail dailyCategorySalesReportJobDetail = new JobDetail("dailyCategorySalesReportJob", "group1",
             * DailyCategorySalesReportJob.class);
             */

            // JobDetail dailyGoogleAdsBannedWordsSearchJobDetail = new JobDetail("googleAdsBannedWordsSearchJob",
            // "group2", DailyGoogleAdsBannedWordsSearchJob.class);

            // JobDetail sixHourlyCategorySalesReportJobDetail = new JobDetail("sixHourlyCategorySalesReportJob",
            // "group5", SixHourlyCategorySalesReportJob.class);

            // TODO: uncomment later
            // CronTrigger dailyCategorySalesReportTrigger = new CronTrigger("emailJobTrigger", "group3",
            // dailyCategorySalesReportGenerationCronTime);
            // CronTrigger dailyGoogleAdsBannedWordsSearchTrigger = new CronTrigger("emailBannedWordsTrigger", "group4",
            // dailyBadWordSearchCronTime);
            // CronTrigger sixHourlyCategorySalesReportTrigger = new CronTrigger("emailJobTrigger", "group6",
            // sixHourlyCategorySalesReportGenerationCronTime);
            // sixHourlyCategorySalesReportTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
            // sixHourlyCategorySalesReportTrigger.setRepeatInterval(100);
            // dailyCategorySalesReportTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
            // dailyGoogleAdsBannedWordsSearchTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
            // Tell quartz to schedule the job using our trigger
            /* scheduler.scheduleJob(dailyCategorySalesReportJobDetail, dailyCategorySalesReportTrigger); */

            // scheduler.scheduleJob(dailyGoogleAdsBannedWordsSearchJobDetail, dailyGoogleAdsBannedWordsSearchTrigger);
            // scheduler.scheduleJob(sixHourlyCategorySalesReportJobDetail, sixHourlyCategorySalesReportTrigger);
            log.info("------- Starting Scheduler ----------------");
            scheduler.start();

        } catch (SchedulerException e) {
            log.error(e.toString(), e);
            // } catch (ParseException e) {
            // log.error(e.toString(), e);
        }
    }

    public void stop() {
        try {
            scheduler.shutdown(true);
            SchedulerMetaData metaData = scheduler.getMetaData();
            // log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        } catch (SchedulerException e) {
            log.error(e.toString());
        }
    }

}
