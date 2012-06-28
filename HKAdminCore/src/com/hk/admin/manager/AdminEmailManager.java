package com.hk.admin.manager;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.admin.dto.DisplayTicketHistoryDto;
import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.admin.pact.service.email.AdminEmailService;
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
import com.hk.service.impl.FreeMarkerService;
import com.hk.util.HKImageUtils;
import com.hk.util.SendGridUtil;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
@Component
public class AdminEmailManager {

    private static Logger logger = LoggerFactory.getLogger(EmailManager.class);

    public static final String GOOGLE_BANNED_WORD_LIST = "googleBannedWordList";

    private Set<String> hkReportAdminEmails = null;
    private Set<String> marketingAdminEmails = null;

    @Value("#{hkEnvProps['" + Keys.Env.hkReportAdminEmails + "']}")
    private String hkReportAdminEmailsString = null;
    @Value("#{hkEnvProps['" + Keys.Env.marketingAdminEmails + "']}")
    private String marketingAdminEmailsString = null;

    @Autowired
    private EmailService emailService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private EmailerHistoryDao emailerHistoryDao;
    @Autowired
    private EmailRecepientDao emailRecepientDao;
    @Autowired
    private EmailCampaignDao emailCampaignDao;
    @Autowired
    private NotifyMeDao notifyMeDao;
    @Autowired
    private LinkManager linkManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private UserService userService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private FreeMarkerService freeMarkerService;
    @Autowired
    private AdminEmailService adminEmailService;

    private final int COMMIT_COUNT = 100;
    private final int INITIAL_LIST_SIZE = 100;

    @PostConstruct
    public void postConstruction() {
        this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmailsString, ",");
        this.marketingAdminEmails = BaseUtils.split(marketingAdminEmailsString, ",");
    }


    public boolean sendTestCampaignMails(Set<User> emailersList, EmailCampaign emailCampaign) {
        //template path
        String emailCampaignTemplate = emailCampaign.getTemplate();

        //template contents from db
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

        Template freemarkerTemplate = null;
        //if template path is not null then find template from codebase else, directly process the string available
        // as template contents
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(BaseUtils.newline + emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        } else {
            return false;
        }
        for (User user : emailersList) {
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            valuesMap.put("user", user);
            emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(), user.getName(), "info@healthkart.com");
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean sendCampaignMails(List<EmailRecepient> emailersList, EmailCampaign emailCampaign, String xsmtpapi) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();


        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        } else {
            return false;
        }

        List<Map<String, HtmlEmail>> emailList = new ArrayList<Map<String, HtmlEmail>>();
        List<EmailerHistory> emailHistoryRecs = new ArrayList<EmailerHistory>(INITIAL_LIST_SIZE);
        List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>(INITIAL_LIST_SIZE);
        int commitCount = 0;
        int breakFromLoop = emailersList.size() < COMMIT_COUNT ? emailersList.size() : COMMIT_COUNT;

        Session session = baseDao.getHibernateTemplate().getSessionFactory().openSession();
        for (EmailRecepient emailRecepient : emailersList) {

            try {

                // values that may be used in FTL
                HashMap valuesMap = new HashMap();
                valuesMap.put(EmailMapKeyConstants.unsubscribeLink, getLinkManager().getEmailUnsubscribeLink(emailRecepient));

                valuesMap.put(EmailMapKeyConstants.name, emailRecepient.getName());

                Map<String, HtmlEmail> email = emailService.createHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(), emailRecepient.getName(), "info@healthkart.com", headerMap);
                emailList.add(email);
                // keep a record in history
                emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                emailRecepient.setLastEmailDate(new Date());
                emailRecepientRecs.add(emailRecepient);

                EmailerHistory emailerHistory = getEmailerHistoryDao().createEmailerHistoryObject("no-reply@healthkart.com", "HealthKart",
                        getBaseDao().get(EmailType.class, emailCampaign.getEmailType().getId()), emailRecepient, emailCampaign, "");
                emailHistoryRecs.add(emailerHistory);

                commitCount++;
                if (commitCount == breakFromLoop) {
                    getAdminEmailService().saveOrUpdate(session, emailRecepientRecs);
                    getAdminEmailService().saveOrUpdate(session, emailHistoryRecs);
                    commitCount = 0;
                    emailHistoryRecs.clear();
                    emailRecepientRecs.clear();
                }

            } catch (Exception e) {
                logger.info("Some exception occured while sending email to one of the user with email id" + emailRecepient.getEmail(), e);
            }
        }











        emailService.sendBulkHtmlEmail(emailList, emailCampaign);
        session.close();
        return true;
    }




    public void sendCampaignByUploadingFile(List<Long> userIds, List<String> emailIds, EmailCampaign emailCampaign, int maxResultCount) {
        List<String> finalCategories = new ArrayList<String>();
        finalCategories.add("User Ids Excel");
        String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);
        List<EmailRecepient> filteredUsers = new ArrayList<EmailRecepient>();
        do {
            filteredUsers.clear();
            if (userIds != null) {
                filteredUsers = getAdminEmailService().getUserMailingList(emailCampaign, userIds, maxResultCount);
            }





            if (emailIds != null) {
                filteredUsers = getAdminEmailService().getMailingListByEmailIds(emailCampaign, emailIds, maxResultCount);
            }







            if (filteredUsers.size() > 0) {
                logger.info(" user list size " + filteredUsers.size());
                sendCampaignMails(filteredUsers, emailCampaign, xsmtpapi);
            }
        } while (filteredUsers.size() > 0);
    }




    public void populateEmailRecepient(List<String> userIdList, int maxResultCount) {
        List<User> usersNotInEmailRecepient = new ArrayList<User>();
        do {
            usersNotInEmailRecepient.clear();
            usersNotInEmailRecepient = getAdminEmailService().findAllUsersNotInEmailRecepient(maxResultCount, userIdList);












            List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>(INITIAL_LIST_SIZE);
            int counter = 0;
            for (User user : usersNotInEmailRecepient) {
                EmailRecepient emailRecepient = getEmailRecepientDao().createEmailRecepient(user.getEmail());
                emailRecepientRecs.add(emailRecepient);
                if (counter == COMMIT_COUNT) {
                    getEmailRecepientDao().saveOrUpdate(emailRecepientRecs);
                    counter = 0;
                    emailRecepientRecs.clear();
                }
                counter++;
            }
            if (counter > 0) {
                getEmailRecepientDao().saveOrUpdate(emailRecepientRecs);
            }
        } while (usersNotInEmailRecepient.size() > 0);

    }

    public boolean sendGRNEmail(GoodsReceivedNote grn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("grn", grn);

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.grnEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, grn.getPurchaseOrder().getCreatedBy().getEmail(),
                grn.getPurchaseOrder().getCreatedBy().getName());
    }

    public boolean sendNotifyUsersMails(List<NotifyMe> notifyMeList, EmailCampaign emailCampaign, String xsmtpapi, Product product, ProductVariant productVariant,
                                        User notifedByuser) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        } else {
            return false;
        }

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
                    Boolean mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(),
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
     * @param emailCampaign
     * @param excelFilePath
     * @param sheetName
     * @return
     */
    public Boolean sendMailMergeCampaign(EmailCampaign emailCampaign, String excelFilePath, String sheetName) {
        List<String> tags = new ArrayList<String>();
        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        int i;

        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        } else {
            return false;
        }





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
            if (emailRecepient.isEmailAllowed()) {
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

                    emailService.sendHtmlEmail(freemarkerTemplate, excelMap, (String) excelMap.get(EmailMapKeyConstants.emailId), "", "info@healthkart.com", headerMap);

                    emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                    emailRecepient.setLastEmailDate(new Date());
                    getEmailRecepientDao().save(emailRecepient);
                    getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", emailCampaign.getEmailType(), emailRecepient, emailCampaign, "");
                }
            }
        }
        return true;
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

                    String emailCampaignTemplate = emailCampaign.getTemplate();
                    String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

                    Template freemarkerTemplate = null;
                    if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
                        freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + emailCampaignTemplate);
                    } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
                        StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
                        finalContents.append(emailCampaignTemplateContents);
                        freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
                    } else {
                        return false;
                    }

                    Boolean mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(),
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

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.dailyCategorySalesReport);
        for (String hkReportAdminEmail : hkReportAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate
                    , valuesMap, hkReportAdminEmail, "HK Report Admin");
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

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.sixHourlyCategorySalesReport);
        emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "rahul.agarwal@healthkart.com", "Category Admin");




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

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.dailyGoogleBannedWordReport);
        boolean success = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "rahul.agarwal@healthkart.com", "Category Admin");
        // success = emailService.sendHtmlEmail(EmailTemplateConstants.dailyGoogleBannedWordReport, valuesMap,
        // "avinash@healthkart.com", "Category Admin");
        for (String marketingAdminEmail : marketingAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, marketingAdminEmail, "HK Report Admin");
            if (!sent)
                success = false;
        }
        success = true;
        for (String hkReportAdminEmail : hkReportAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, hkReportAdminEmail, "HK Report Admin");
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

    public AdminEmailService getAdminEmailService() {
        return adminEmailService;

    }

    public void setAdminEmailService(AdminEmailService adminEmailService) {
        this.adminEmailService = adminEmailService;

    }
}