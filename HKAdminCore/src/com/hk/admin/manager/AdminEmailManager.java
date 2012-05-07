package com.hk.admin.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.admin.dto.DisplayTicketHistoryDto;
import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.EnumEmailType;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.Ticket;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.EmailerHistoryDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.EmailService;
import com.hk.util.NotifyMeListUtil;

@SuppressWarnings("unchecked")
@Component
public class AdminEmailManager {

    public static final String GOOGLE_BANNED_WORD_LIST       = "googleBannedWordList";

    private  Set<String>  hkAdminEmails                 = null;
    private  Set<String>  hkReportAdminEmails           = null;
    private  Set<String>  babyAdminEmails               = null;
    private  Set<String>  beautyAdminEmails             = null;
    private  Set<String>  diabetesAdminEmails           = null;
    private  Set<String>  eyeAdminEmails                = null;
    private  Set<String>  homeDevicesAdminEmails        = null;
    private  Set<String>  nutritionAdminEmails          = null;
    private  Set<String>  personalCareAdminEmails       = null;
    private  Set<String>  logisticsAdminEmails          = null;
    private  Set<String>  sportsAdminEmails             = null;
    private  Set<String>  servicesAdminEmails           = null;
    private  Set<String>  marketingAdminEmails          = null;
    private  Set<String>  categoryHealthkartList        = null;

    @Value("#{hkEnvProps['hkAdminEmails']}")
    private String             hkAdminEmailsString;
    @Value("#{hkEnvProps['hkReportAdminEmails']}")
    private String             hkReportAdminEmailsString     = null;
    @Value("#{hkEnvProps['babyAdminEmails']}")
    private String             babyAdminEmailsString         = null;
    @Value("#{hkEnvProps['beautyAdminEmails']}")
    private String             beautyAdminEmailsString       = null;
    @Value("#{hkEnvProps['diabetesAdminEmails']}")
    private String             diabetesAdminEmailsString     = null;
    @Value("#{hkEnvProps['eyeAdminEmails']}")
    private String             eyeAdminEmailsString          = null;
    @Value("#{hkEnvProps['homeDevicesAdminEmails']}")
    private String             homeDevicesAdminEmailsString  = null;
    @Value("#{hkEnvProps['nutritionAdminEmails']}")
    private String             nutritionAdminEmailsString    = null;
    @Value("#{hkEnvProps['personalCareAdminEmails']}")
    private String             personalCareAdminEmailsString = null;
    @Value("#{hkEnvProps['logisticsAdminEmails']}")
    private String             logisticsAdminEmailsString    = null;
    @Value("#{hkEnvProps['sportsAdminEmails']}")
    private String             sportsAdminEmailsString       = null;
    @Value("#{hkEnvProps['servicesAdminEmails']}")
    private String             servicesAdminEmailsString     = null;
    @Value("#{hkEnvProps['marketingAdminEmails']}")
    private String             marketingAdminEmailsString    = null;
    @Value("#{hkEnvProps['categoryHealthkart']}")
    private String             categoryHealthkartListString  = null;

    @Autowired
    private EmailService       emailService;
    @Autowired
    private BaseDao            baseDao;
    @Autowired
    private EmailerHistoryDao  emailerHistoryDao;
    @Autowired
    private EmailRecepientDao  emailRecepientDao;
    @Autowired
    private EmailCampaignDao   emailCampaignDao;
    @Autowired
    private NotifyMeDao        notifyMeDao;
    @Autowired
    private LinkManager        linkManager;

    @PostConstruct
    public void postConstruction() {
        this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmailsString, ",");
        this.hkAdminEmails = BaseUtils.split(hkAdminEmailsString, ",");
        this.babyAdminEmails = BaseUtils.split(babyAdminEmailsString, ",");
        this.beautyAdminEmails = BaseUtils.split(beautyAdminEmailsString, ",");
        this.diabetesAdminEmails = BaseUtils.split(diabetesAdminEmailsString, ",");
        this.eyeAdminEmails = BaseUtils.split(eyeAdminEmailsString, ",");
        this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmailsString, ",");
        this.nutritionAdminEmails = BaseUtils.split(nutritionAdminEmailsString, ",");
        this.personalCareAdminEmails = BaseUtils.split(personalCareAdminEmailsString, ",");
        this.logisticsAdminEmails = BaseUtils.split(logisticsAdminEmailsString, ",");
        this.sportsAdminEmails = BaseUtils.split(sportsAdminEmailsString, ",");
        this.servicesAdminEmails = BaseUtils.split(servicesAdminEmailsString, ",");
        this.marketingAdminEmails = BaseUtils.split(marketingAdminEmailsString, ",");
        this.categoryHealthkartList = BaseUtils.split(categoryHealthkartListString, ",");
    }

    /*
     * private AdminEmailManager(EmailService emailService, // @Named(Keys.Env.hkAdminEmails) String hkAdminEmails, //
     * @Named(Keys.Env.hkReportAdminEmails) String hkReportAdminEmails, // @Named(Keys.Env.babyAdminEmails) String
     * babyAdminEmails, // @Named(Keys.Env.beautyAdminEmails) String beautyAdminEmails, //
     * @Named(Keys.Env.diabetesAdminEmails) String diabetesAdminEmails, // @Named(Keys.Env.eyeAdminEmails) String
     * eyeAdminEmails, // @Named(Keys.Env.homeDevicesAdminEmails) String homeDevicesAdminEmails, //
     * @Named(Keys.Env.nutritionAdminEmails) String nutritionAdminEmails, // @Named(Keys.Env.personalCareAdminEmails)
     * String personalCareAdminEmails, // @Named(Keys.Env.sportsAdminEmails) String sportsAdminEmails, //
     * @Named(Keys.Env.servicesAdminEmails) String servicesAdminEmails, // @Named(Keys.Env.logisticsAdminEmails) String
     * logisticsAdminEmails, // @Named(Keys.Env.marketingAdminEmails) String marketingAdminEmails, //
     * @Named(Keys.Env.categoryHealthkart) String categoryHealthkartList) { this.emailService = emailService;
     * this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmails, ","); this.hkAdminEmails =
     * BaseUtils.split(hkAdminEmails, ","); this.babyAdminEmails = BaseUtils.split(babyAdminEmails, ",");
     * this.beautyAdminEmails = BaseUtils.split(beautyAdminEmails, ","); this.diabetesAdminEmails =
     * BaseUtils.split(diabetesAdminEmails, ","); this.eyeAdminEmails = BaseUtils.split(eyeAdminEmails, ",");
     * this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmails, ","); this.nutritionAdminEmails =
     * BaseUtils.split(nutritionAdminEmails, ","); this.personalCareAdminEmails =
     * BaseUtils.split(personalCareAdminEmails, ","); this.logisticsAdminEmails = BaseUtils.split(logisticsAdminEmails,
     * ","); this.sportsAdminEmails = BaseUtils.split(sportsAdminEmails, ","); this.servicesAdminEmails =
     * BaseUtils.split(servicesAdminEmails, ","); this.marketingAdminEmails = BaseUtils.split(marketingAdminEmails,
     * ","); this.categoryHealthkartList = BaseUtils.split(categoryHealthkartList, ","); }
     */

    @SuppressWarnings("unchecked")
    public boolean sendCampaignMails(Set<User> emailersList, EmailCampaign emailCampaign, String xsmtpapi) {
        // logger.info("Reached Level 3");
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        for (User user : emailersList) {
            // find exisitng receipients or create receipients thru the emails ids passed
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
            // values that may be used in FTL
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("user", user);
            // subscribed user + same camapaign mail not yet sent
            if (emailRecepient.isSubscribed() && getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    emailService.sendHtmlEmail(emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), user.getName(), "info@healthkart.com", headerMap);
                    // keep a record in history
                    emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                    emailRecepient.setLastEmailDate(new Date());
                    getEmailRecepientDao().save(emailRecepient);
                    getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", getBaseDao().get(EmailType.class, EnumEmailType.CampaignEmail.getId()),
                            emailRecepient, emailCampaign, "");
                }
            }
        }
        // logger.info("Reached Level 3.5");
        return true;
    }

    public boolean sendTestCampaignMails(Set<User> emailersList, EmailCampaign emailCampaign) {
        for (User user : emailersList) {
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("user", user);
            emailService.sendHtmlEmail(emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), user.getName(), "info@healthkart.com");
        }
        return true;
    }

    // here mail will go once again even if for same campaign has been sent before, generally a reminder email
    public boolean sendCampaignMailsToListOfEmailIds(List<String> emailersList, EmailCampaign emailCampaign, String xsmtpapi) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        for (String email : emailersList) {
            // find existing recipients or create recipients through the emails ids passed
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(email);
            // values that may be used in FTL
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("user", email);
            // subscribed user + same campaign mail not yet sent
            if (emailRecepient.isSubscribed() && getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    emailService.sendHtmlEmail("/newsletters/" + emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), email, "info@healthkart.com", headerMap);
                    // keep a record in history
                    emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                    emailRecepient.setLastEmailDate(new Date());
                    getEmailRecepientDao().save(emailRecepient);
                    getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", getBaseDao().get(EmailType.class, EnumEmailType.CampaignEmail.getId()),
                            emailRecepient, emailCampaign, "");
                }
            }
        }
        return true;
    }

    public boolean sendGRNEmail(GoodsReceivedNote grn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("grn", grn);
        return emailService.sendHtmlEmail(EmailTemplateConstants.grnEmail, valuesMap, grn.getPurchaseOrder().getCreatedBy().getEmail(),
                grn.getPurchaseOrder().getCreatedBy().getName());
    }

    public boolean sendNotifyUsersMails(List<NotifyMe> notifyMeList, EmailCampaign emailCampaign, String xsmtpapi, Product product, ProductVariant productVariant,
            User notifedByuser) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        for (NotifyMe notifyMeObject : notifyMeList) {
            // find existing recipients or create recipients through the emails ids passed
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(notifyMeObject.getEmail());
            // values that may be used in FTL
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("notifiedUser", notifyMeObject);
            if (product != null) {
                valuesMap.put("product", product);
            } else {
                valuesMap.put("product", productVariant.getProduct());
            }
            valuesMap.put("emailCampaign", emailCampaign);
            // subscribed user + same campaign mail not yet sent
            if (getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    Boolean mailSentSuccessfully = emailService.sendHtmlEmail("/newsletters/" + emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(),
                            notifyMeObject.getName(), "info@healthkart.com", headerMap);
                    // keep a record in history
                    if (mailSentSuccessfully == true) {
                        emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                        emailRecepient.setLastEmailDate(new Date());
                        getEmailRecepientDao().save(emailRecepient);
                        getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart",
                                getBaseDao().get(EmailType.class, EnumEmailType.NotifyUserEmail.getId()), emailRecepient, emailCampaign, "");
                        List<NotifyMe> notifyMeListForSameUser = new ArrayList<NotifyMe>();
                        notifyMeListForSameUser = getNotifyMeDao().getAllNotifyMeForSameUser(notifyMeObject.getEmail());
                        if (notifyMeListForSameUser != null) {
                            for (NotifyMe notifyMe : notifyMeListForSameUser) {
                                if (notifyMe.getProductVariant().getProduct().equals(notifyMeObject.getProductVariant().getProduct())) {
                                    notifyMe.setNotifiedByUser(notifedByuser);
                                    notifyMe.setNotifiedDate(new Date());
                                    getNotifyMeDao().save(notifyMe);
                                }
                            }
                        }
                        notifyMeObject.setNotifiedByUser(notifedByuser);
                        notifyMeObject.setNotifiedDate(new Date());
                        getNotifyMeDao().save(notifyMeObject);
                    }
                }
            }
        }
        return true;
    }

    public boolean sendNotifyUserMailsForPVInStock(List<NotifyMe> notifyMeList, User notifiedByUser) {
        Map<String, String> headerMap = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (NotifyMe notifyMeObject : notifyMeList) {

            String emailCampaignName = notifyMeObject.getProductVariant().getProduct() != null ? notifyMeObject.getProductVariant().getProduct().getId()
                    : notifyMeObject.getProductVariant() != null ? notifyMeObject.getProductVariant().getProduct().getId() : "";
            emailCampaignName += "_" + sdf.format(new Date());
            EmailCampaign emailCampaign = getEmailCampaignDao().getOrCreateEmailCampaign(emailCampaignName, 0l, EmailTemplateConstants.notifyUserEmail);
            String xsmtpapi = NotifyMeListUtil.getSendGridHeaderJson(notifyMeObject.getProductVariant().getProduct(), notifyMeObject.getProductVariant(), emailCampaign);
            HashMap valuesMap = new HashMap();
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(notifyMeObject.getEmail());
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("notifiedUser", notifyMeObject);
            valuesMap.put("product", notifyMeObject.getProductVariant().getProduct());
            valuesMap.put("emailCampaign", emailCampaign);
            // subscribed user + same campaign mail not yet sent
            if (getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null && notifyMeObject.getNotifiedByUser() == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    Boolean mailSentSuccessfully = emailService.sendHtmlEmail("/newsletters/" + emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(),
                            notifyMeObject.getName(), "info@healthkart.com", headerMap);
                    if (mailSentSuccessfully) {
                        // keep a record in history
                        notifyMeObject.setNotifiedByUser(notifiedByUser);
                        notifyMeObject.setNotifiedDate(new Date());
                        notifyMeObject = getNotifyMeDao().save(notifyMeObject);
                        emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                        emailRecepient.setLastEmailDate(new Date());
                        emailRecepient = (EmailRecepient) getEmailRecepientDao().save(emailRecepient);
                        getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart",
                                getBaseDao().get(EmailType.class, EnumEmailType.NotifyUserEmail.getId()), emailRecepient, emailCampaign, "");
                        List<NotifyMe> notifyMeListForSameUser = new ArrayList<NotifyMe>();
                        notifyMeListForSameUser = getNotifyMeDao().getAllNotifyMeForSameUser(notifyMeObject.getEmail());
                        if (notifyMeListForSameUser != null) {
                            for (NotifyMe notifyMe : notifyMeListForSameUser) {
                                if (notifyMe.getProductVariant().getProduct().equals(notifyMeObject.getProductVariant().getProduct())) {
                                    notifyMe.setNotifiedByUser(notifiedByUser);
                                    notifyMe.setNotifiedDate(new Date());
                                    getNotifyMeDao().save(notifyMe);
                                }
                            }
                        }
                    }
                }
            }

        }
        return true;
    }

    public boolean sendEditTicketEmail(Ticket ticket, DisplayTicketHistoryDto displayTicketHistoryDto) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("ticket", ticket);
        valuesMap.put("displayTicketHistoryDto", displayTicketHistoryDto);
        User reporter = ticket.getReporter();
        User owner = ticket.getOwner();
        User changedBy = displayTicketHistoryDto.getChangedBy();
        boolean reporterEmail = emailService.sendHtmlEmail(EmailTemplateConstants.editTicketEmail, valuesMap, reporter.getEmail(), reporter.getName(), reporter.getEmail());
        boolean ownerEmail = true;
        boolean changedByEmail = true;
        if (!reporter.equals(owner)) {
            ownerEmail = emailService.sendHtmlEmail(EmailTemplateConstants.editTicketEmail, valuesMap, owner.getEmail(), owner.getName(), reporter.getEmail());
        }
        if (!reporter.equals(changedBy) && !owner.equals(changedBy)) {
            changedByEmail = emailService.sendHtmlEmail(EmailTemplateConstants.editTicketEmail, valuesMap, changedBy.getEmail(), changedBy.getName(), changedBy.getEmail());
        }
        return (reporterEmail && ownerEmail && changedByEmail);
    }

    public boolean sendDailyCategorySalesReportToCategoryManager(Map categoriesOrderReportDtosMap) {
        HashMap valuesMap = new HashMap();
        Date currentDate = new Date();
        Date yesterdayStartOfDayDate;
        Calendar currentDateCal = Calendar.getInstance();
        Calendar yesterdayEndOfDayCal = Calendar.getInstance();
        currentDate = currentDateCal.getTime();
        yesterdayStartOfDayDate = DateUtils.getStartOfPreviousDay(currentDate);
        yesterdayEndOfDayCal.setTime(yesterdayStartOfDayDate);
        Integer numberOfDaysInMonth = yesterdayEndOfDayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Map<String, Long> targetDailyMrpSalesMap = new HashMap<String, Long>();
        targetDailyMrpSalesMap.put(CategoryConstants.BABY, CategoryConstants.BABY_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.BEAUTY, CategoryConstants.BEAUTY_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.DIABETES, CategoryConstants.DIABETES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.EYE, CategoryConstants.EYE_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.HOME_DEVICES, CategoryConstants.HOME_DEVICES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.NUTRITION, CategoryConstants.NUTRITION_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.PERSONAL_CARE, CategoryConstants.PERSONAL_CARE_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.SERVICES, CategoryConstants.SERVICES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.SPORTS, CategoryConstants.SPORTS_TARGET_SALES / numberOfDaysInMonth);

        valuesMap.put(CategoryConstants.CATEGORIES_ORDER_REPORT_DTOS_MAP, categoriesOrderReportDtosMap);
        valuesMap.put(CategoryConstants.TARGET_MRP_SALES_MAP, CategoryConstants.targetMrpSalesMap);
        valuesMap.put(CategoryConstants.TARGET_ORDER_COUNT_MAP, CategoryConstants.targetOrderCountMap);
        valuesMap.put(CategoryConstants.YESTERDAY_DATE, DateUtils.getEndOfPreviousDay(currentDate));
        valuesMap.put(CategoryConstants.TARGET_DAILY_MRP_SALES_MAP, targetDailyMrpSalesMap);

        /* Report not being sent to all as Sale targets are based on MRP as opposted to Net Sales */

        // for(String categoryHealthkart: categoryHealthkartList)
        // emailService.sendHtmlEmail(EmailTemplateConstants.dailyCategorySalesReport, valuesMap, categoryHealthkart,
        // "Category Admin");
        boolean success = true;
        /*
         * for (String hkAdminEmail : hkAdminEmails) { boolean sent =
         * emailService.sendHtmlEmailNoReply(EmailTemplateConstants.dailyCategorySalesReport, valuesMap, hkAdminEmail,
         * "HK Admin"); if (!sent) success = false; }
         */
        for (String hkReportAdminEmail : hkReportAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.dailyCategorySalesReport, valuesMap, hkReportAdminEmail, "HK Report Admin");
            if (!sent)
                success = false;
        }
        return success;
    }

    public boolean sendSixHourlyCategorySalesReportToCategoryManager(Map categoriesOrderReportDtosMap) {
        HashMap valuesMap = new HashMap();
        Date currentDate = new Date();
        Date startOfPreviousSixHours;
        Date endOfPreviousSixHours;
        Calendar currentDateCal = Calendar.getInstance();
        currentDate = currentDateCal.getTime();
        Calendar startSixHourCal = Calendar.getInstance();
        Calendar endSixHourCal = Calendar.getInstance();

        endSixHourCal.set(Calendar.MINUTE, 0);
        endSixHourCal.set(Calendar.SECOND, 0);
        endSixHourCal.set(Calendar.MILLISECOND, 0);

        if (endSixHourCal.get(Calendar.HOUR_OF_DAY) == 0) {
            endSixHourCal.setTime(DateUtils.getEndOfPreviousDay(currentDate));
        }
        startSixHourCal.set(Calendar.HOUR, (currentDateCal.get(Calendar.HOUR) - 6));
        startSixHourCal.set(Calendar.MINUTE, 0);
        startSixHourCal.set(Calendar.SECOND, 0);
        startSixHourCal.set(Calendar.MILLISECOND, 0);

        startOfPreviousSixHours = startSixHourCal.getTime();
        endOfPreviousSixHours = endSixHourCal.getTime();

        valuesMap.put(CategoryConstants.SIX_HOURLY_CATEGORIES_ORDER_REPORT_DTOS_MAP, categoriesOrderReportDtosMap);
        valuesMap.put(CategoryConstants.START_SIX_HOUR, startOfPreviousSixHours.toString());
        valuesMap.put(CategoryConstants.END_SIX_HOUR, endOfPreviousSixHours.toString());

        emailService.sendHtmlEmail(EmailTemplateConstants.sixHourlyCategorySalesReport, valuesMap, "rahul.agarwal@healthkart.com", "Category Admin");
        emailService.sendHtmlEmail(EmailTemplateConstants.sixHourlyCategorySalesReport, valuesMap, "abhay.bihani@healthkart.com", "Category Admin");

        boolean success = true;
        /*
         * for (String hkAdminEmail : hkAdminEmails) { boolean sent =
         * emailService.sendHtmlEmailNoReply(EmailTemplateConstants.sixHourlyCategorySalesReport, valuesMap,
         * hkAdminEmail, "HK Admin"); if (!sent) success = false; }
         */
        return success;
    }

    public boolean sendDailyGoogleAdsBannedWords(List<GoogleBannedWordDto> googleBannedWordDtoList) {
        HashMap valuesMap = new HashMap();

        Date currentDate = new Date();
        valuesMap.put(GOOGLE_BANNED_WORD_LIST, googleBannedWordDtoList);
        valuesMap.put(CategoryConstants.YESTERDAY_DATE, DateUtils.getEndOfPreviousDay(currentDate));

        boolean success = emailService.sendHtmlEmail(EmailTemplateConstants.dailyGoogleBannedWordReport, valuesMap, "rahul.agarwal@healthkart.com", "Category Admin");
        // success = emailService.sendHtmlEmail(EmailTemplateConstants.dailyGoogleBannedWordReport, valuesMap,
        // "avinash@healthkart.com", "Category Admin");
        for (String marketingAdminEmail : marketingAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.dailyGoogleBannedWordReport, valuesMap, marketingAdminEmail, "HK Report Admin");
            if (!sent)
                success = false;
        }
        success = true;
        for (String hkReportAdminEmail : hkReportAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.dailyGoogleBannedWordReport, valuesMap, hkReportAdminEmail, "HK Report Admin");
            if (!sent)
                success = false;
        }
        return success;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public EmailerHistoryDao getEmailerHistoryDao() {
        return emailerHistoryDao;
    }

    public void setEmailerHistoryDao(EmailerHistoryDao emailerHistoryDao) {
        this.emailerHistoryDao = emailerHistoryDao;
    }

    public EmailRecepientDao getEmailRecepientDao() {
        return emailRecepientDao;
    }

    public void setEmailRecepientDao(EmailRecepientDao emailRecepientDao) {
        this.emailRecepientDao = emailRecepientDao;
    }

    public EmailCampaignDao getEmailCampaignDao() {
        return emailCampaignDao;
    }

    public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
        this.emailCampaignDao = emailCampaignDao;
    }

    public NotifyMeDao getNotifyMeDao() {
        return notifyMeDao;
    }

    public void setNotifyMeDao(NotifyMeDao notifyMeDao) {
        this.notifyMeDao = notifyMeDao;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

}
