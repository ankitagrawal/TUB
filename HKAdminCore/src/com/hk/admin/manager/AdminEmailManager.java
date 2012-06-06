package com.hk.admin.manager;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.admin.dto.DisplayTicketHistoryDto;
import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.EnumEmailType;
import com.hk.constants.core.Keys;
import com.hk.constants.email.EmailMapKeyConstants;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.Ticket;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.EmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailerHistory;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.EmailerHistoryDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.EmailService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.discount.CouponService;
import com.hk.util.HKImageUtils;
import com.hk.util.SendGridUtil;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.service.impl.FreeMarkerService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import freemarker.template.Template;

@SuppressWarnings("unchecked")
@Component
public class AdminEmailManager {

  private static Logger         logger                        = LoggerFactory.getLogger(EmailManager.class);

  public static final String    GOOGLE_BANNED_WORD_LIST       = "googleBannedWordList";

  //private Set<String>           hkAdminEmails                 = null;
  private Set<String>           hkReportAdminEmails           = null;
  /*private Set<String>           babyAdminEmails               = null;
private Set<String>           beautyAdminEmails             = null;
private Set<String>           diabetesAdminEmails           = null;
private Set<String>           eyeAdminEmails                = null;
private Set<String>           homeDevicesAdminEmails        = null;
private Set<String>           nutritionAdminEmails          = null;
private Set<String>           personalCareAdminEmails       = null;
private Set<String>           logisticsAdminEmails          = null;
private Set<String>           sportsAdminEmails             = null;*/
  //private Set<String>           servicesAdminEmails           = null;
  private Set<String>           marketingAdminEmails          = null;
  //private Set<String>           categoryHealthkartList        = null;

  /* @Value("#{hkEnvProps['" + Keys.Env.hkAdminEmails + "']}")
private String                hkAdminEmailsString;*/
  @Value("#{hkEnvProps['" + Keys.Env.hkReportAdminEmails + "']}")
  private String                hkReportAdminEmailsString     = null;
  /*;
@Value("#{hkEnvProps['" + Keys.Env.babyAdminEmails + "']}")
private String                babyAdminEmailsString         = null;
@Value("#{hkEnvProps['" + Keys.Env.beautyAdminEmails + "']}")
private String                beautyAdminEmailsString       = null;
@Value("#{hkEnvProps['" + Keys.Env.diabetesAdminEmails + "']}")
private String                diabetesAdminEmailsString     = null;
@Value("#{hkEnvProps['" + Keys.Env.eyeAdminEmails + "']}")
private String                eyeAdminEmailsString          = null;
@Value("#{hkEnvProps['" + Keys.Env.homeDevicesAdminEmails + "']}")
private String                homeDevicesAdminEmailsString  = null;
@Value("#{hkEnvProps['" + Keys.Env.nutritionAdminEmails + "']}")
private String                nutritionAdminEmailsString    = null;
@Value("#{hkEnvProps['" + Keys.Env.personalCareAdminEmails + "']}")
private String                personalCareAdminEmailsString = null;
@Value("#{hkEnvProps['" + Keys.Env.logisticsAdminEmails + "']}")
private String                logisticsAdminEmailsString    = null;
@Value("#{hkEnvProps['" + Keys.Env.sportsAdminEmails + "']}")
private String                sportsAdminEmailsString       = null;
@Value("#{hkEnvProps['" + Keys.Env.servicesAdminEmails + "']}")
private String                servicesAdminEmailsString     = null;*/
  @Value("#{hkEnvProps['" + Keys.Env.marketingAdminEmails + "']}")
  private String                marketingAdminEmailsString    = null;
  /* @Value("#{hkEnvProps['" + Keys.Env.categoryHealthkart + "']}")
private String                categoryHealthkartListString  = null;*/

  @Autowired
  private EmailService          emailService;
  @Autowired
  private BaseDao               baseDao;
  @Autowired
  private EmailerHistoryDao     emailerHistoryDao;
  @Autowired
  private EmailRecepientDao     emailRecepientDao;
  @Autowired
  private EmailCampaignDao      emailCampaignDao;
  @Autowired
  private NotifyMeDao           notifyMeDao;
  @Autowired
  private LinkManager           linkManager;
  @Autowired
  private ProductService        productService;
  @Autowired
  private ProductVariantService productVariantService;
  @Autowired
  private UserService           userService;
  @Autowired
  private CouponService         couponService;
  @Autowired
  private FreeMarkerService     freeMarkerService;
  private final int             COMMIT_COUNT = 100;
  @PostConstruct
  public void postConstruction() {
    this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmailsString, ",");
    /* this.hkAdminEmails = BaseUtils.split(hkAdminEmailsString, ",");
 this.babyAdminEmails = BaseUtils.split(babyAdminEmailsString, ",");
 this.beautyAdminEmails = BaseUtils.split(beautyAdminEmailsString, ",");
 this.diabetesAdminEmails = BaseUtils.split(diabetesAdminEmailsString, ",");
 this.eyeAdminEmails = BaseUtils.split(eyeAdminEmailsString, ",");
 this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmailsString, ",");
 this.nutritionAdminEmails = BaseUtils.split(nutritionAdminEmailsString, ",");
 this.personalCareAdminEmails = BaseUtils.split(personalCareAdminEmailsString, ",");
 this.logisticsAdminEmails = BaseUtils.split(logisticsAdminEmailsString, ",");
 this.sportsAdminEmails = BaseUtils.split(sportsAdminEmailsString, ",");
 this.servicesAdminEmails = BaseUtils.split(servicesAdminEmailsString, ",");*/
    this.marketingAdminEmails = BaseUtils.split(marketingAdminEmailsString, ",");
    /*this.categoryHealthkartList = BaseUtils.split(categoryHealthkartListString, ",");*/
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
      //find existing recipients or create recipients through the emails ids passed
      EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(email);
      //values that may be used in FTL
      HashMap valuesMap = new HashMap();
      valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
      valuesMap.put("user", email);
      // subscribed user + same campaign mail not yet sent
      List<EmailerHistory> emailerHistoryList = getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign);
      if (emailRecepient.isSubscribed()) {
        if (emailerHistoryList != null && emailerHistoryList.isEmpty()) {
          // last mail date null or  last mail date > campaign min date
          if (emailRecepient.getLastEmailDate() == null || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
            emailService.sendHtmlEmail(emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), email, "info@healthkart.com", headerMap);
            //keep a record in history
            emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
            emailRecepient.setLastEmailDate(new Date());
            getEmailRecepientDao().save(emailRecepient);
            getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", getBaseDao().get(EmailType.class, EnumEmailType.CampaignEmail.getId()), emailRecepient, emailCampaign, "");
          }
        }
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public boolean sendCampaignMails(List<User> emailersList, EmailCampaign emailCampaign, String xsmtpapi) {
    Map<String, String> headerMap = new HashMap<String, String>();
    headerMap.put("X-SMTPAPI", xsmtpapi);
    Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaign.getTemplate());
    List<Map<String, HtmlEmail>> emailList = new ArrayList<Map<String, HtmlEmail>>();
    List<EmailerHistory> emailHistoryRecs = new ArrayList<EmailerHistory>(100);
    List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>(100);
    int commitCount = 0;
    int breakFromLoop = emailersList.size() < COMMIT_COUNT ? emailersList.size() : COMMIT_COUNT;

    for (User user : emailersList) {
      try {
        // find exisitng receipients or create recepients thru the emails ids passed
        EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
        // values that may be used in FTL
        HashMap valuesMap = new HashMap();
        valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
        valuesMap.put("user", user);

        Map<String, HtmlEmail> email = emailService.createHtmlEmail(emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), user.getName(), "info@healthkart.com", headerMap, freemarkerTemplate);
        emailList.add(email);
        // keep a record in history
        emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
        emailRecepient.setLastEmailDate(new Date());
        emailRecepientRecs.add(emailRecepient);

        EmailerHistory emailerHistory = getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart",
            getBaseDao().get(EmailType.class, EnumEmailType.CampaignEmail.getId()), emailRecepient, emailCampaign, "");
        emailHistoryRecs.add(emailerHistory);

        commitCount++;
        if( commitCount == breakFromLoop ) {
          getEmailRecepientDao().saveOrUpdate(emailRecepient);
          getEmailerHistoryDao().saveOrUpdate(emailHistoryRecs);

          commitCount = 0;
          emailHistoryRecs = new ArrayList<EmailerHistory>(100);
          emailRecepientRecs = new ArrayList<EmailRecepient>(100);
        }

      } catch (Exception e) {
        logger.info("Some exception occured while sending email to one of the uses, user id being" + user.getId(), e);
      }
    }

    emailService.sendBulkHtmlEmail(emailList, emailCampaign);
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
      List<EmailerHistory> emailerHistoryList = getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign);
      if (emailerHistoryList != null && emailerHistoryList.isEmpty() && notifyMeObject.getNotifiedByUser() == null) {
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

  /**
   * email_id is a mandatory header for the excel if a user doesnt exist for an email_id,that entry will be removed in
   * case the excel file contains product or product variant: the headers need to be product_id,product_variant_id
   * respectively
   *
   * @throws IOException
   */
  public void sendMailMergeCampaign(EmailCampaign emailCampaign, String excelFilePath, String sheetName) throws IOException {
    List<String> tags = new ArrayList<String>();
    ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
    Iterator<HKRow> rowIterator = parser.parse();
    int i;

    while (rowIterator != null && rowIterator.hasNext()) {
      HashMap excelMap = new HashMap();
      i = 0;
      HKRow curHkRow = rowIterator.next();
      while (null != curHkRow && curHkRow.columnValues != null && i < curHkRow.columnValues.length) {
        String key = parser.getHeadingNames(i);
        String value = curHkRow.getColumnValue(i);
        excelMap.put(key.toLowerCase(), value);
        i++;
      }

      EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(excelMap.get(EmailMapKeyConstants.emailId).toString());
      if (emailRecepient.isSubscribed()) {
        Boolean emailSentToRecepientRecently = Boolean.FALSE;
        if (emailRecepient.getLastEmailDate() != null) {
          Date lastDateCampaignMailSentToEmailRecepient = getEmailCampaignDao().getLastDateOfEmailCampaignMailSentToEmailRecepient(emailCampaign, emailRecepient);
          if (lastDateCampaignMailSentToEmailRecepient != null) {
            emailSentToRecepientRecently = new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isBefore(
                lastDateCampaignMailSentToEmailRecepient.getTime());
          }
        }
        if (!emailSentToRecepientRecently) {
          HashMap extraMapEntries = getExtraMapEntriesForMailMerge(excelMap);
          if (extraMapEntries != null) {
            excelMap.putAll(extraMapEntries);
          } else {
            excelMap.clear();
            continue;
          }

          excelMap.put(EmailMapKeyConstants.unsubscribeLink, linkManager.getEmailUnsubscribeLink(emailRecepient));

          if (excelMap.containsKey(EmailMapKeyConstants.tags)) {
            tags = Arrays.asList(StringUtils.split(excelMap.get(EmailMapKeyConstants.tags).toString(), ","));
          }

          // construct the headers to send
          String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(tags, emailCampaign);
          Map<String, String> headerMap = new HashMap<String, String>();
          headerMap.put("X-SMTPAPI", xsmtpapi);

          emailService.sendHtmlEmail(emailCampaign.getTemplate(), excelMap, (String) excelMap.get(EmailMapKeyConstants.emailId), "", "info@healthkart.com", headerMap);

          emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
          emailRecepient.setLastEmailDate(new Date());
          getEmailRecepientDao().save(emailRecepient);
          getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", emailCampaign.getEmailType(), emailRecepient, emailCampaign, "");
        }
      }
    }
  }

  private HashMap getExtraMapEntriesForMailMerge(HashMap excelMap) {
    List<User> users = userService.findByEmail(excelMap.get(EmailMapKeyConstants.emailId).toString());
    if (users != null && users.size() > 0) {
      excelMap.put(EmailMapKeyConstants.user, users.get(0));
    } else {
      return null;
    }

    if (excelMap.containsKey(EmailMapKeyConstants.couponCode)) {
      Coupon coupon = couponService.findByCode(excelMap.get(EmailMapKeyConstants.couponCode).toString());
      excelMap.put(EmailMapKeyConstants.coupon, coupon);
    }

    if (excelMap.containsKey(EmailMapKeyConstants.productId)) {
      Product product = getProductService().getProductById(excelMap.get(EmailMapKeyConstants.productId).toString());
      if (product != null) {
        Long productMainImageId = product.getMainImageId();
        excelMap.put(EmailMapKeyConstants.product, product);
        //excelMap.put(EmailMapKeyConstants.productUrl, productService.getProductUrl(product));
        excelMap.put(EmailMapKeyConstants.productUrl, convertToWww(getProductService().getProductUrl(product)));


        if (productMainImageId != null) {
          excelMap.put(EmailMapKeyConstants.productImageUrlMedium, HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, productMainImageId));
          excelMap.put(EmailMapKeyConstants.productImageUrlTiny, HKImageUtils.getS3ImageUrl(EnumImageSize.TinySize, productMainImageId));
          excelMap.put(EmailMapKeyConstants.productImageUrlSmall, HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, productMainImageId));
        } else {
          excelMap.put(EmailMapKeyConstants.productImageUrlMedium, "");
          excelMap.put(EmailMapKeyConstants.productImageUrlTiny, "");
          excelMap.put(EmailMapKeyConstants.productImageUrlSmall, "");
        }
      } else {
        excelMap.put(EmailMapKeyConstants.product, null);
        excelMap.put(EmailMapKeyConstants.productUrl, "");
        excelMap.put(EmailMapKeyConstants.productImageUrlMedium, "");
        excelMap.put(EmailMapKeyConstants.productImageUrlTiny, "");
        excelMap.put(EmailMapKeyConstants.productImageUrlSmall, "");
      }
    }

    if (excelMap.containsKey(EmailMapKeyConstants.productVariantId)) {
      ProductVariant productVariant = productVariantService.getVariantById(excelMap.get(EmailMapKeyConstants.productVariantId).toString());
      if (productVariant != null) {
        excelMap.put(EmailMapKeyConstants.productVariant, productVariant);

        if (!excelMap.containsKey(EmailMapKeyConstants.productId)) {
          Product product = productVariant.getProduct();
          Long productMainImageId = product.getMainImageId();

          excelMap.put(EmailMapKeyConstants.product, product);
          //excelMap.put(EmailMapKeyConstants.productUrl, productService.getProductUrl(product));
          excelMap.put(EmailMapKeyConstants.productUrl, convertToWww(getProductService().getProductUrl(product)));

          if (productMainImageId != null) {
            excelMap.put(EmailMapKeyConstants.productImageUrlMedium, HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, productMainImageId));
            excelMap.put(EmailMapKeyConstants.productImageUrlTiny, HKImageUtils.getS3ImageUrl(EnumImageSize.TinySize, productMainImageId));
            excelMap.put(EmailMapKeyConstants.productImageUrlSmall, HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, productMainImageId));
          } else {
            excelMap.put(EmailMapKeyConstants.productImageUrlMedium, "");
            excelMap.put(EmailMapKeyConstants.productImageUrlTiny, "");
            excelMap.put(EmailMapKeyConstants.productImageUrlSmall, "");
          }
        }
      } else {
        excelMap.put(EmailMapKeyConstants.productVariant, null);
        if (!excelMap.containsKey(EmailMapKeyConstants.productId)) {
          excelMap.put(EmailMapKeyConstants.product, null);
          excelMap.put(EmailMapKeyConstants.productUrl, "");
          excelMap.put(EmailMapKeyConstants.productImageUrlMedium, "");
          excelMap.put(EmailMapKeyConstants.productImageUrlTiny, "");
          excelMap.put(EmailMapKeyConstants.productImageUrlSmall, "");
        }
      }
    }
    return excelMap;
  }

  public boolean sendNotifyUserMailsForPVInStock(List<NotifyMe> notifyMeList, User notifiedByUser) {
    Map<String, String> headerMap = new HashMap<String, String>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    for (NotifyMe notifyMeObject : notifyMeList) {

      String emailCampaignName = notifyMeObject.getProductVariant().getProduct() != null ? notifyMeObject.getProductVariant().getProduct().getId()
          : notifyMeObject.getProductVariant() != null ? notifyMeObject.getProductVariant().getProduct().getId() : "";
      emailCampaignName += "_" + sdf.format(new Date());
      EmailCampaign emailCampaign = getEmailCampaignDao().getOrCreateEmailCampaign(emailCampaignName, 0l, EmailTemplateConstants.notifyUserEmail);
      //String xsmtpapi = SendGridUtil.getNotifyMeSendGridHeaderJson(notifyMeObject.getProductVariant().getProduct(), notifyMeObject.getProductVariant(), emailCampaign);
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


  //todo : isko thik kar do - for now hardcoding logic to convert admin.healthkart.com to www.healthkart.com
  private static String convertToWww(String productUrl) {
    return productUrl.replaceAll("admin\\.healthkart\\.com", "www.healthkart.com");
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

  public ProductService getProductService() {
    return productService;
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }
}
