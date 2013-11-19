package com.hk.admin.manager;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.admin.dto.inventory.PurchaseOrderDto;
import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.admin.pact.service.email.ProductVariantNotifyMeEmailService;

import com.hk.admin.util.PurchaseOrderPDFGenerator;
import com.hk.cache.RoleCache;
import com.hk.constants.EnumJitShippingOrderMailToCategoryReason;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.catalog.image.EnumImageSize;
import com.hk.constants.core.EnumEmailType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.StateList;
import com.hk.constants.email.EmailMapKeyConstants;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.constants.warehouse.EnumWarehouseIdentifier;
import com.hk.domain.accounting.DebitNote;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.SimilarProduct;
import com.hk.domain.core.EmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.Courier;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailerHistory;
import com.hk.domain.email.OrderEmailExclusion;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.user.Role;
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
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.service.impl.FreeMarkerService;
import com.hk.util.HKImageUtils;
import com.hk.util.SendGridUtil;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKFileWriter;
import com.hk.util.io.HKRow;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
@Component
public class AdminEmailManager {

    private static Logger logger = LoggerFactory.getLogger(EmailManager.class);

    public static final String GOOGLE_BANNED_WORD_LIST = "googleBannedWordList";
    public static final String PURCHASE_REPORTING_EMAIL = "purchase.reporting@healthkart.com";
    public static final String WAREHOUSE_PURCHASE_EMAIL = "warehouse.purchase@healthkart.com";

    public static final String WAREHOUSE_JIT_EMAIL = "ops.jit@healthkart.com";
    private Set<String> hkReportAdminEmails = null;
    private Set<String> marketingAdminEmails = null;

    @Value("#{hkEnvProps['" + Keys.Env.hkReportAdminEmails + "']}")
    private String hkReportAdminEmailsString = null;
    @Value("#{hkEnvProps['" + Keys.Env.marketingAdminEmails + "']}")
    private String marketingAdminEmailsString = null;
    @Value("#{hkEnvProps['" + Keys.Env.logisticsOpsEmails + "']}")
    private String logisticsOpsEmails;
    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                                    adminDownloads;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

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
    @Autowired
    private EmailManager emailManager;
    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    ProductVariantNotifyMeEmailService productVariantNotifyMeEmailService;
    @Autowired
    private PurchaseOrderManager purchaseOrderManager;
    @Autowired
    PurchaseOrderPDFGenerator purchaseOrderPDFGenerator;



    private PurchaseOrderDto purchaseOrderDto;
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
    public boolean sendCampaignMails(List<EmailRecepient> emailersList, EmailCampaign emailCampaign, String senderEMail,
                                     String senderName, String replyToEmail, String xsmtpapi, SendCampaignResult sendCampaignResult) {
        boolean isCampaignSentSuccess = true;
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);
        headerMap.put("X-Mailgun-Variables", xsmtpapi);

        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();


        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(BaseUtils.newline + emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        } else {
            return false;
        }

        List<Map<String, HtmlEmail>> emailList = new ArrayList<Map<String, HtmlEmail>>();
        List<EmailerHistory> emailHistoryRecs = new ArrayList<EmailerHistory>(INITIAL_LIST_SIZE);
        List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>(INITIAL_LIST_SIZE);
        int emailCount = 0;
        int breakFromLoop = emailersList.size() < COMMIT_COUNT ? emailersList.size() : COMMIT_COUNT;

        Session session = baseDao.getHibernateTemplate().getSessionFactory().openSession();
        List<Map<String, HtmlEmail>> tempEmailList = new ArrayList<Map<String, HtmlEmail>>();

        for (EmailRecepient emailRecepient : emailersList) {
            try {
                // values that may be used in FTL
                HashMap valuesMap = new HashMap();
                valuesMap.put(EmailMapKeyConstants.unsubscribeLink, getLinkManager().getEmailUnsubscribeLink(emailRecepient));

                valuesMap.put(EmailMapKeyConstants.name, emailRecepient.getName());

                Map<String, HtmlEmail> email = emailService.createHtmlEmail(freemarkerTemplate, valuesMap, senderEMail, senderName, emailRecepient.getEmail(), emailRecepient.getName(), replyToEmail, "", headerMap);
                if (email != null) {
                    tempEmailList.add(email);
                    // keep a record in history
                    emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                    emailRecepient.setLastEmailDate(new Date());
                    emailRecepientRecs.add(emailRecepient);

                    EmailerHistory emailerHistory = getEmailerHistoryDao().createEmailerHistoryObject(senderEMail, senderName,
                            getBaseDao().get(EmailType.class, emailCampaign.getEmailType().getId()), emailRecepient, emailCampaign, "");
                    emailHistoryRecs.add(emailerHistory);

                    emailCount++;
                    if (emailCount % breakFromLoop == 0 || emailCount == emailersList.size()) {
                        boolean emailRecipientsRecorded = getAdminEmailService().saveOrUpdate(session, emailRecepientRecs);
                        boolean emailHistoryRecorded = getAdminEmailService().saveOrUpdate(session, emailHistoryRecs);

                        if (emailHistoryRecorded && emailRecipientsRecorded) {
                            emailList.addAll(tempEmailList);
                        } else {
                            sendCampaignResult.setCampaignSentSuccess(false);
                            for (Map<String, HtmlEmail> map : tempEmailList) {
                                List<String> emailListInMap = new ArrayList<String>(map.keySet());
                                sendCampaignResult.addErrorEmaiList(emailListInMap);
                            }
                        }

                        tempEmailList.clear();
                        emailHistoryRecs.clear();
                        emailRecepientRecs.clear();
                    }
                } else {
                    logger.info("Unable to send email to " + emailRecepient.getEmail());
                }

            } catch (Exception e) {
                logger.info("Some exception occured while sending email to one of the user with email id" + emailRecepient.getEmail(), e);
            }
        }


        emailService.sendBulkHtmlEmail(emailList, emailCampaign);
        session.close();
        return isCampaignSentSuccess;
    }


    public void sendCampaignByUploadingFile(List<Long> userIds, List<String> emailIds, EmailCampaign emailCampaign, int maxResultCount, String senderEmail, String senderName, String replyToEmail) {
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
                SendCampaignResult sendCampaignResult = new SendCampaignResult();
                sendCampaignMails(filteredUsers, emailCampaign, senderEmail, senderName, replyToEmail, xsmtpapi, sendCampaignResult);
                if (!sendCampaignResult.isCampaignSentSuccess()) {
                    logger.error("Error in sending file upload campaing to :" + sendCampaignResult.getErrorEmails().toString());
                }
            }
        } while (filteredUsers.size() > 0);
    }


    public void populateEmailRecepient(List<String> userIdList, int maxResultCount) {
        if (userIdList != null) {
            List<User> usersNotInEmailRecepient = getAdminEmailService().findAllUsersNotInEmailRecepient(maxResultCount, userIdList);
            if (usersNotInEmailRecepient.size() > 0) {
                List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>(INITIAL_LIST_SIZE);
                int counter = 0;
                for (User user : usersNotInEmailRecepient) {
                    EmailRecepient emailRecepient = getEmailRecepientDao().createEmailRecepient(user.getEmail());
                    emailRecepientRecs.add(emailRecepient);
                    counter++;
                    if (counter % COMMIT_COUNT == 0 || counter == userIdList.size()) {
                        getEmailRecepientDao().saveOrUpdate(emailRecepientRecs);
                        emailRecepientRecs.clear();
                    }

                }
            }
        }
    }

    public boolean sendGRNEmail(GoodsReceivedNote grn) {
        HashMap valuesMap = new HashMap();
        List<SkuGroup> skuGroups = skuGroupService.getAllCheckedInBatchForGrn(grn);
        valuesMap.put("grn", grn);
        valuesMap.put("skuGroups", skuGroups);
        boolean success = true;
        if (grn.getGrnLineItems() != null && grn.getGrnLineItems().get(0) != null) {
            Category category = grn.getGrnLineItems().get(0).getSku().getProductVariant().getProduct().getPrimaryCategory();
            Set<String> categoryAdmins = emailManager.categoryAdmins(category);
            Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.grnEmail);
            for (String emailString : categoryAdmins) {
                boolean sent = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailString,
                        category.getName() + " Category Admin");
                if (!sent) {
                    success = false;
                }
            }
            boolean sent = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, PURCHASE_REPORTING_EMAIL,
                    category.getName() + " Purchase Report Admin");
            if (!sent) {
                success = false;
            }
            return success;
        } else {
            return false;
        }
    }


    public int sendNotifyUserMailsForDeletedOOSHiddenProducts(Map<String, List<NotifyMe>> userNotifyMeListMap) {
        HashMap valuesMap = new HashMap();
        User notifedByuser = userService.getLoggedInUser();
        int countOfSentMail = 0;
        for (String emailId : userNotifyMeListMap.keySet()) {
            Boolean mailSentSuccessfully = false;
            List<NotifyMe> notifyMeListPerUser = userNotifyMeListMap.get(emailId);
            NotifyMe notifyMeObject = notifyMeListPerUser.get(0);
            User user = userService.findByLogin(emailId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());
            Map<String, ProductVariant> productPriceRangeMap = new HashMap<String, ProductVariant>();
            valuesMap.put("currentDate", currentDate);
            /* find existing recipients or create recipients through the emails ids passed */
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(emailId);
            if (user != null) {
                valuesMap.put("unsubscribeLink", getLinkManager().getUnsubscribeLink(user));
            } else {
                valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            }
            valuesMap.put("notifiedUser", notifyMeObject);
            if (notifyMeListPerUser.size() > 1) {
                /*User has asked for multiple variant notification  */
                Map<String, List<Product>> productSimilarProductMap = new HashMap<String, List<Product>>();
                List<NotifyMe> notifyMeListFinal = new ArrayList<NotifyMe>();
                for (NotifyMe notifyMe : notifyMeListPerUser) {
                    List<Product> similarProductList = productVariantNotifyMeEmailService.getSimilarProductsWithMaxUnbookedInvn(notifyMe.getProductVariant(), 3);
                    if (similarProductList != null && similarProductList.size() > 0) {
                        notifyMeListFinal.add(notifyMe);
                        productSimilarProductMap.put(notifyMe.getProductVariant().getProduct().getId(), similarProductList);
                        for (Product product : similarProductList) {
                            productPriceRangeMap.put(product.getId(), product.getMaximumDiscountProducVariant());
                            valuesMap.put("productPriceMap", productPriceRangeMap);
                        }

                    }
                }
                valuesMap.put("productNotifyList", notifyMeListFinal);
                /*similarProductMap  KEY: OOS product user asked for notification   VALUE: list of  3 similar products(first three max inv products) */
                if (productSimilarProductMap.size() > 0) {
                    valuesMap.put("similarProductMap", productSimilarProductMap);
                    Template freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + EmailTemplateConstants.notifyUserForSimilarProductsForMultipleVariants);
                    mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailId, notifyMeObject.getName(), "info@healthkart.com");
                }

            } else {
                /*Single variant notification*/
                valuesMap.put("product", notifyMeObject.getProductVariant().getProduct());
                List<Product> similarProductList = productVariantNotifyMeEmailService.getSimilarProductsWithMaxUnbookedInvn(notifyMeObject.getProductVariant(), 3);
                if (similarProductList != null && similarProductList.size() > 0) {
                    valuesMap.put("similarProductList", similarProductList);
                    for (Product product : similarProductList) {
                        productPriceRangeMap.put(product.getId(), product.getMaximumDiscountProducVariant());
                        valuesMap.put("productPriceMap", productPriceRangeMap);
                    }
                    Template freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + EmailTemplateConstants.notifyUserForSimilarProductsForSingleVariants);
                    mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailId, notifyMeObject.getName(), "info@healthkart.com");
                }
            }

            if (mailSentSuccessfully) {
                countOfSentMail++;
                for (NotifyMe notifyMe : notifyMeListPerUser) {
                    {
                        notifyMe.setNotifiedByUser(notifedByuser);
                        notifyMe.setNotifiedDate(new Date());
                        getNotifyMeDao().save(notifyMe);
                    }
                }
            }

        }
        return countOfSentMail;

    }


    public int sendNotifyUsersMails(Map<String, List<NotifyMe>> userNotifyMeListMap) {
        int countOfSentMail = 0;
        HashMap valuesMap = new HashMap();
        User notifedByuser = userService.getLoggedInUser();

        for (String emailId : userNotifyMeListMap.keySet()) {

            Boolean mailSentSuccessfully = false;
            List<NotifyMe> notifyMeListPerUser = userNotifyMeListMap.get(emailId);
            NotifyMe notifyMeObject = notifyMeListPerUser.get(0);
            User user = userService.findByLogin(emailId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());
            valuesMap.put("currentDate", currentDate);
            // find existing recipients or create recipients through the emails ids passed
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(emailId);
            if (user != null) {
                valuesMap.put("unsubscribeLink", getLinkManager().getUnsubscribeLink(user));
            } else {
                valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            }

            valuesMap.put("notifiedUser", notifyMeObject);
            if (notifyMeListPerUser.size() > 1) {
                //User has asked for multiple variant notification
                valuesMap.put("notifyList", notifyMeListPerUser);
                Template freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + EmailTemplateConstants.notifyUserEmailForMultipleVariants);
                mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailId, notifyMeObject.getName(), "info@healthkart.com");

            } else {
                valuesMap.put("product", notifyMeObject.getProductVariant().getProduct());
                Template freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + EmailTemplateConstants.notifyUserEmail);
                mailSentSuccessfully = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailId, notifyMeObject.getName(), "info@healthkart.com");
            }
            if (mailSentSuccessfully) {
                countOfSentMail++;
                for (NotifyMe notifyMe : notifyMeListPerUser) {
                    {
                        notifyMe.setNotifiedByUser(notifedByuser);
                        notifyMe.setNotifiedDate(new Date());
                        getNotifyMeDao().save(notifyMe);
                    }
                }
            }

        }
        return countOfSentMail;
    }


    public boolean sendNotifyUsersMails(List<NotifyMe> notifyMeList, EmailCampaign emailCampaign, String xsmtpapi, Product product, ProductVariant productVariant,
                                        User notifedByuser) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);
        headerMap.put("X-Mailgun-Variables", xsmtpapi);

        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate("/newsletters/" + emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(BaseUtils.newline + emailCampaignTemplateContents);
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
    public Boolean sendMailMergeCampaign(EmailCampaign emailCampaign, String excelFilePath, String sheetName, String mailGunCampaignId) {

        ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
        Iterator<HKRow> rowIterator = parser.parse();
        String campaignReportPath = adminUploadsPath + "/emailReports/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String reportName = "EmailReport_" + emailCampaign.getId().toString() + sdf.format(new Date());
        File failedEmailLogFile = HKFileWriter.getFileStream(campaignReportPath, reportName, "csv");
        Writer failedEmailLog = HKFileWriter.getFileWriter(failedEmailLogFile);

        Template freemarkerTemplate = generateFreeMarkerTemplate(emailCampaign);
        if (freemarkerTemplate == null) {
            return false;
        }

        TemplateHashModel hkImageUtils = null;
        TemplateHashModel hkPriceUtils = null;
        try {
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            //wrapper.getOuterIdentity().wrap("com.hk.domain.catalog.product.Product");
            TemplateHashModel staticModels = wrapper.getStaticModels();
            hkImageUtils = (TemplateHashModel) staticModels.get("com.hk.util.HKImageUtils");
            hkPriceUtils = (TemplateHashModel) staticModels.get("com.hk.util.HKPriceUtils");
        } catch (TemplateModelException ex) {
            //Only expected reason for this exception is when com.hk.util.HKImageUtils is missing
            logger.error("Unable to get static methods definition in HKImageUtils", ex);
            return false;
        }

        try {
            Map<String, Product> productsMap = new HashMap<String, Product>();
            while (rowIterator != null && rowIterator.hasNext()) {
                HashMap excelMap = new HashMap();
                int i = 0;
                HKRow curHkRow = rowIterator.next();
                while ((null != curHkRow) && (curHkRow.columnValues != null) && i < curHkRow.columnValues.length) {
                    String key = parser.getHeadingNames(i);
                    String value = curHkRow.getColumnValue(i);
                    if (StringUtils.isNotBlank(value)) {
                        excelMap.put(key.toLowerCase(), value);
                    }
                    i++;
                }
                if (excelMap.isEmpty()) {
                    break;
                }
                excelMap.put("HKImageUtils", hkImageUtils);
                excelMap.put("HKPriceUtils", hkPriceUtils);
                sendMailMergeCampaign(excelMap, productsMap, emailCampaign, freemarkerTemplate, failedEmailLog, mailGunCampaignId);
            }
        } finally {

            boolean isReportSent = emailService.sendHtmlEmail("Email Campaign report",
                    "Following recepients did not email \r\n", "prateek.verma@healthkart.com", "Prateek Verma", failedEmailLogFile.getAbsolutePath());
            HKFileWriter.close(failedEmailLog);
            if (isReportSent) {
                failedEmailLogFile.delete();
            }
        }

        return true;
    }

    private Template generateFreeMarkerTemplate(EmailCampaign emailCampaign) {
        String emailCampaignTemplate = emailCampaign.getTemplate();
        String emailCampaignTemplateContents = emailCampaign.getTemplateFtl();

        Template freemarkerTemplate = null;
        if (emailCampaignTemplate != null && StringUtils.isNotBlank(emailCampaignTemplate)) {
            freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaignTemplate);
        } else if (emailCampaignTemplateContents != null && StringUtils.isNotBlank(emailCampaignTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(emailCampaign.getSubject());
            finalContents.append(BaseUtils.newline + emailCampaignTemplateContents);
            freemarkerTemplate = freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
        }
        return freemarkerTemplate;
    }

    private boolean sendMailMergeCampaign(HashMap excelMap, Map<String, Product> productsMap, EmailCampaign emailCampaign,
                                          Template freemarkerTemplate, Writer failedEmailLog,
                                          String mailGunCampaignId) {

        String userEmail = excelMap.get(EmailMapKeyConstants.emailId).toString();
        List<User> users = userService.findByEmail(userEmail);
        if (users != null && users.size() > 0) {
            excelMap.put(EmailMapKeyConstants.user, users.get(0));
        } else {
            String message = String.format("%s, %s", userEmail, "does not exist");
            HKFileWriter.writeToStream(failedEmailLog, message);
            return false;
        }

        EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(excelMap.get(EmailMapKeyConstants.emailId).toString());


        List<String> tags = new ArrayList<String>();
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
                Boolean sendAlternateTemplate = Boolean.FALSE;
                List result = getExtraMapEntriesForMailMerge(excelMap, productsMap);
                sendAlternateTemplate = Boolean.parseBoolean(result.get(0).toString());
                String failureMessage = result.get(1).toString();
                //If alternate template has to be sent...(In case when similar products are all out of stock)
                if (sendAlternateTemplate) {
                    String message = String.format("%s,%s", emailRecepient.getEmail(), "Sending alternate email reporder_reminder_general");
                    HKFileWriter.writeToStream(failedEmailLog, message);
                    //We do not want to consider similar productId now as they are either out_of_stock or does not exist
                    //Without this code it will become an infinite loop
                    excelMap.remove(EmailMapKeyConstants.similarProductId);
                    //Find the alternate email and send it..It has to be hard-coded
                    EmailCampaign alternateCampaign = getEmailCampaignDao().findCampaignByName("reporder_reminder_general");
                    if (alternateCampaign != null) {
                        sendMailMergeCampaign(excelMap, productsMap, alternateCampaign, generateFreeMarkerTemplate(alternateCampaign), failedEmailLog, mailGunCampaignId);
                    } else {
                        return false;
                    }
                }
                if (!failureMessage.trim().equals("")) {
                    String message = String.format("%s,%s", emailRecepient.getEmail(), result);
                    HKFileWriter.writeToStream(failedEmailLog, message);
                    return false;
                }

                excelMap.put(EmailMapKeyConstants.unsubscribeLink, linkManager.getEmailUnsubscribeLink(emailRecepient));

                if (excelMap.containsKey(EmailMapKeyConstants.tags)) {
                    tags = Arrays.asList(StringUtils.split(excelMap.get(EmailMapKeyConstants.tags).toString(), ","));
                }

                // construct the headers to send
                String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(tags, emailCampaign);
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("X-SMTPAPI", xsmtpapi);
                headerMap.put("X-Mailgun-Variables", xsmtpapi);
                if (StringUtils.isNotBlank(mailGunCampaignId)) {
                    headerMap.put("X-Mailgun-Variables", xsmtpapi);
                }

                boolean isSent = emailService.sendHtmlEmail(freemarkerTemplate, excelMap, (String) excelMap.get(EmailMapKeyConstants.emailId), "", "info@healthkart.com", headerMap);

                if (isSent) {
                    emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
                    emailRecepient.setLastEmailDate(new Date());
                    getEmailRecepientDao().save(emailRecepient);
                    getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", emailCampaign.getEmailType(), emailRecepient, emailCampaign, "");
                } else {
                    String message = String.format("%s,%s", emailCampaign.getId().toString(), emailRecepient.getEmail());
                    HKFileWriter.writeToStream(failedEmailLog, message);
                }
                return isSent;
            }
        }

        return false;
    }

    /**
     * updates the map with extra properties
     *
     * @param excelMap
     * @return result with failure message if any
     */
    private List getExtraMapEntriesForMailMerge(HashMap excelMap, Map<String, Product> productsMap) {

        boolean sendAlternateTemplate = Boolean.FALSE;
        List result = new ArrayList();
        result.add(0, sendAlternateTemplate);
        result.add(1, "");
        StringBuilder errorBuilder = new StringBuilder();
        List<ProductVariant> validProductVariants = new ArrayList<ProductVariant>();
        if (excelMap.containsKey(EmailMapKeyConstants.couponCode)) {
            Coupon coupon = couponService.findByCode(excelMap.get(EmailMapKeyConstants.couponCode).toString());
            excelMap.put(EmailMapKeyConstants.coupon, coupon);
        }
        if (excelMap.containsKey(EmailMapKeyConstants.productVariantId)) {
            String[] productVariantIds = excelMap.get(EmailMapKeyConstants.productVariantId).toString().split(",");
            Product product = null;
            for (String pvId : productVariantIds) {
                ProductVariant productVariant = productVariantService.getVariantById(pvId);
                if (productVariant != null) {
                    if (productsMap.containsKey(pvId)) {
                        product = productsMap.get(pvId);
                    } else {
                        product = productVariant.getProduct();
                        productsMap.put(pvId, product);
                    }
                } else {
                    errorBuilder.append(String.format("Product Variant %s is wrong", pvId));
                    continue;
                }

                if (product == null) {
                    errorBuilder.append(String.format("Product for variant %s is wrong", productVariant.getId()));
                    continue;
                }

                if (product.isOutOfStock()) {
                    errorBuilder.append(String.format("Product %s is out of stock", product.getId()));
                    continue;
                }
                product.setProductURL(convertToWww(getProductService().getProductUrl(product, false)));
                //OK..now we have a valid product variant
                validProductVariants.add(productVariant);

                if (excelMap.containsKey(EmailMapKeyConstants.similarProductId)) {
                    sendAlternateTemplate = Boolean.TRUE;
                    Object similarIds = excelMap.get(EmailMapKeyConstants.similarProductId);
                    List<Product> similarProducts = new ArrayList<Product>();
                    if (similarProducts != null &&
                            StringUtils.isNotBlank(similarProducts.toString())) {
                        if (similarIds.toString().trim().equals("auto")) {
                            List<SimilarProduct> similarProductList = product.getSimilarProducts();
                            //Todo: Check this logic and re-implement
                            /*for (SimilarProduct similarProduct : similarProductList){
                                Product simProduct = similarProduct.getSimilarProduct();
                                if ((simProduct!= null) && !simProduct.isOutOfStock()
                                        && !productService.isComboInStock(simProduct.getId())){
                                    {
                                        simProduct.setProductURL(convertToWww(getProductService().getProductUrl(simProduct,false)));
                                        similarProducts.add(simProduct);
                                        sendAlternateTemplate = Boolean.FALSE;
                                    }
                                }
                            };*/
                        } else {
                            String[] similarProductIds = similarIds.toString().split(",");
                            for (String productId : similarProductIds) {
                                Product simProduct = null;
                                if (productsMap.containsKey(productId)) {
                                    simProduct = productsMap.get(productId);
                                } else {
                                    simProduct = getProductService().getProductById(productId);
                                    if (simProduct != null) {
                                        productsMap.put(productId, simProduct);
                                    }
                                }

                                boolean isProductInStock = !simProduct.getOutOfStock();
                                isProductInStock = productService.isComboInStock(simProduct);

                                if ((simProduct != null) && isProductInStock) {
                                    simProduct.setProductURL(convertToWww(getProductService().getProductUrl(simProduct, false)));
                                    similarProducts.add(simProduct);
                                    sendAlternateTemplate = Boolean.FALSE;
                                }
                            }
                        }
                        result.set(0, sendAlternateTemplate);
                    }

                    excelMap.put(EmailMapKeyConstants.similarProductId, similarProducts);
                }
            }

            excelMap.put(EmailMapKeyConstants.productVariant, validProductVariants);

            if (product != null) {
                Long productMainImageId = product.getMainImageId();
                excelMap.put(EmailMapKeyConstants.product, product);
                //excelMap.put(EmailMapKeyConstants.productUrl, productService.getProductUrl(product));
                excelMap.put(EmailMapKeyConstants.productUrl, convertToWww(getProductService().getProductUrl(product, false)));

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
        }
        return result;
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
                        finalContents.append(BaseUtils.newline + emailCampaignTemplateContents);
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
        targetDailyMrpSalesMap.put(CategoryConstants.HEALTH_DEVICES, CategoryConstants.HEALTH_DEVICES_TARGET_SALES / numberOfDaysInMonth);
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

    public boolean sendAwbStatusEmail(Courier courier, ShippingOrder shippingOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("courier", courier);
        valuesMap.put("shippingOrder", shippingOrder);
        if (shippingOrder.isCOD()) {
            valuesMap.put("cod", "Yes");
        } else {
            valuesMap.put("cod", "No");
        }

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.awbStatusEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, logisticsOpsEmails, EmailTemplateConstants.operationsTeam);
    }

    public boolean sendNoShipmentEmail(String message, ShippingOrder shippingOrder, Order baseOrder) {
        HashMap valuesMap = new HashMap();
        if (shippingOrder != null) {
            valuesMap.put("orderId", shippingOrder.getGatewayOrderId());
        } else {
            valuesMap.put("orderId", baseOrder.getGatewayOrderId());
        }
        valuesMap.put("message", message);

        Template freemarTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.courierShipmentFail);
        return emailService.sendHtmlEmail(freemarTemplate, valuesMap, logisticsOpsEmails, EmailTemplateConstants.operationsTeam);
    }

    public boolean sendOrderDeliveredEmail(Order order) {
        List<OrderEmailExclusion> orderEmailExclusionList =
                getBaseDao().findByNamedQueryAndNamedParam("orderExclusionfindByEmail", new String[]{"email"}, new Object[]{order.getUser().getEmail()});

        if (orderEmailExclusionList != null && orderEmailExclusionList.size() > 0) {
            OrderEmailExclusion orderEmailExclusion = orderEmailExclusionList.get(0);
            if (orderEmailExclusion.isDeliveryMailExcluded()) {
                return false;
            }
        }

        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        String feedbackPageUrl = getLinkManager().getFeedbackPage();
        //feedbackPageUrl = convertToWww(feedbackPageUrl);  Handled on Link manager Page

        valuesMap.put(EmailMapKeyConstants.feedbackPage, feedbackPageUrl);
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderDeliveredEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderInstalltionEmail(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderInstalledEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    //todo : isko thik kar do - for now hardcoding logic to convert admin.healthkart.com to www.healthkart.com
    public static String convertToWww(String productUrl) {
        return productUrl.replaceAll("admin\\.healthkart\\.com", "www.healthkart.com");
    }

    public boolean sendPOApprovedEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);
        //Mail to Ajeet if anyone  approves PO  other than Sachin Hans
        User user = purchaseOrder.getApprovedBy();
        Role poApproverRole = RoleCache.getInstance().getRoleByName(EnumRole.PO_APPROVER).getRole();
        List<User> approverUserList = userService.findByRole(poApproverRole);
        if (user != null) {
            if (!(approverUserList.contains(user) && !user.equals(userService.getAdminUser()))) {
                HashMap valuesMapAt = new HashMap();
                valuesMapAt.put("purchaseOrder", purchaseOrder);
                valuesMapAt.put("user", user);
                Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poApprovedByWrongPerson);
                String ajeetMail = "ajeet@healthkart.com";
                emailService.sendHtmlEmail(freemarkerTemplate, valuesMapAt, ajeetMail, "Ajeet");
            }
        }
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poApprovedEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, purchaseOrder.getCreatedBy().getEmail(), purchaseOrder.getCreatedBy().getName());
    }

	public boolean sendPOMailToSupplier(PurchaseOrder purchaseOrder, String supplierEmail) {
		//TODO
		HashMap valuesMap = new HashMap();
		String warehouseName = "", warehouseAddress = "";
		DateTime dt = new DateTime();
		LocalDate ld = dt.toLocalDate();
		String date = ld.getDayOfMonth() + "-" + ld.getMonthOfYear() + "-" + ld.getYear();
		valuesMap.put("purchaseOrder", purchaseOrder);
		valuesMap.put("date", date);
		if (purchaseOrder.getWarehouse().getIdentifier().equalsIgnoreCase(EnumWarehouseIdentifier.GGN_Bright_Warehouse.getName())) {
			warehouseName = "Bright Lifecare Private Limited, Gurgaon Warehouse";
			warehouseAddress = "Khasra No. 146/25/2/1, Village Badshahpur, Distt Gurgaon, Haryana-122101; TIN Haryana - 06101832036";
		} else if (purchaseOrder.getWarehouse().getIdentifier().equalsIgnoreCase(EnumWarehouseIdentifier.MUM_Bright_Warehouse.getName())) {
			warehouseName = "Bright Lifecare Private Limited, Mumbai Warehouse";
			warehouseAddress = "Safexpress Private Limited,Mumbai Nashik Highway N.H-3, Walsind, Lonad, District- Thane- 421302, Maharashtra";
		} else if (purchaseOrder.getWarehouse().getIdentifier().equalsIgnoreCase(EnumWarehouseIdentifier.DEL_Punjabi_Bagh_Aqua_Store.getName())) {
			warehouseName = "Aquamarine Healthcare Private Limited, Delhi Punjabi Bagh Warehouse";
			warehouseAddress = "Shop No 15, Ground Floor, North west Avenue, Club road, Punjabi Bagh Extn, Delhi- 110026, Delhi";
		} else if (purchaseOrder.getWarehouse().getIdentifier().equalsIgnoreCase(EnumWarehouseIdentifier.DEL_Kapashera_Bright_Warehouse.getName())) {
			warehouseName = "Bright Lifecare Private Limited, Delhi Kapashera Warehouse";
			warehouseAddress = "2nd Floor, Safexpress Cargo Complex , 971/1, Opposite Fun and Food Village, Kapashera, New Delhi, Delhi- 110037, Delhi";
		}
		valuesMap.put("warehouseName", warehouseName);
		valuesMap.put("warehouseAddress", warehouseAddress);

		String fromPurchaseEmail = "purchase@healthkart.com";
		Set<String> categoryAdmins = new HashSet<String>();
        Product product = purchaseOrder.getPoLineItems().get(0).getSku().getProductVariant().getProduct();
        if (purchaseOrder.getPoLineItems() != null && purchaseOrder.getPoLineItems().get(0) != null) {
			Category category = product.getPrimaryCategory();
            categoryAdmins = emailManager.categoryAdmins(category);
		}
		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poMailToSupplier);
        if(purchaseOrder.getPurchaseOrderType().getName().equals(EnumPurchaseOrderType.JIT.getName()) || purchaseOrder.getPurchaseOrderType().getName().equals(EnumPurchaseOrderType.DROP_SHIP.getName())) {
            categoryAdmins.add(WAREHOUSE_JIT_EMAIL);
        }

        else{
            categoryAdmins.add(WAREHOUSE_PURCHASE_EMAIL);
        }

		File pdfFile = null;
		File xlsFile = null;
		try {
			String purchaseOrdertype = "";
			if (purchaseOrder.getPurchaseOrderType() != null && purchaseOrder.getPurchaseOrderType().getId().equals(EnumPurchaseOrderType.JIT.getId())) {
				purchaseOrdertype = "JIT";
			} else if (purchaseOrder.getPurchaseOrderType() != null && purchaseOrder.getPurchaseOrderType().getId().equals(EnumPurchaseOrderType.DROP_SHIP.getId())) {
				purchaseOrdertype = "DS";
			}
			pdfFile = new File(adminDownloads + "/reports/PO-" + purchaseOrder.getId() + " "+purchaseOrdertype + " -Dt- " + date + ".pdf");
			pdfFile.getParentFile().mkdirs();
			purchaseOrderDto = getPurchaseOrderManager().generatePurchaseOrderDto(purchaseOrder);
			getPurchaseOrderPDFGenerator().generatePurchaseOrderPdf(pdfFile.getPath(), purchaseOrderDto);

			xlsFile = new File(adminDownloads + "/reports/PO-" + purchaseOrder.getId() + " "+purchaseOrdertype +" -Dt- " + date + ".xls");
			xlsFile.getParentFile().mkdirs();
			xlsFile = getPurchaseOrderManager().generatePurchaseOrderXls(xlsFile.getPath(), purchaseOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File newPdfFile = new File(pdfFile.getAbsolutePath());
		File newXlsFile = new File(xlsFile.getAbsolutePath());
		 if(newPdfFile.exists()&& newXlsFile.exists() && newPdfFile.getName().contains(purchaseOrder.getId().toString())&& newXlsFile.getName().contains(purchaseOrder.getId().toString())){
			 return emailService.sendEmail(freemarkerTemplate, valuesMap, fromPurchaseEmail, "purchase@healthkart.com", supplierEmail, purchaseOrder.getSupplier().getName(), null, null, categoryAdmins, null, pdfFile.getAbsolutePath(), xlsFile.getAbsolutePath()); 
		 }
		 else{
			 return poMailNotSentToSupplier(purchaseOrder);
		 }
		
	}
	
	public boolean poMailNotSentToSupplier(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);
        Set<String> categoryAdmins = new HashSet<String>();
		if (purchaseOrder.getPoLineItems() != null && purchaseOrder.getPoLineItems().get(0) != null) {
			Category category = purchaseOrder.getPoLineItems().get(0).getSku().getProductVariant().getProduct().getPrimaryCategory();
			categoryAdmins = emailManager.categoryAdmins(category);
		}
		String fromPurchaseEmail = "purchase@healthkart.com";
		User user = userService.getAdminUser();
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poMailNotSentToSupplier);
        return emailService.sendEmail(freemarkerTemplate, valuesMap, user.getEmail(),user.getName() ,fromPurchaseEmail, fromPurchaseEmail, null, null, categoryAdmins, null, null, null);
    }


	public boolean sendDebitNoteMail(DebitNote debitNote){
    	HashMap valuesMap = new HashMap();
    	valuesMap.put("debitNote", debitNote);
    	return false;
    }
    
	public boolean sendJitShippingCancellationMail(ShippingOrder shippingOrder, ShippingOrder splitShippingOrder,  EnumJitShippingOrderMailToCategoryReason reason){
    	HashMap valuesMap = new HashMap();
    	Set<String> emailIds = new HashSet<String>();
    	valuesMap.put("shippingOrder", shippingOrder);
    	if(splitShippingOrder!=null){
    		valuesMap.put("splitShippingOrder", splitShippingOrder);
    	}
    	String soCancellationReason = null;
    	if(reason.getId().equals(EnumJitShippingOrderMailToCategoryReason.SO_CANCELLED.getId())){
    		soCancellationReason="Following Shipping Orders has been cancelled. Information of corresponding POs has been mentioned below. Kindly take the required actions.";
    	}else
    		if(reason.getId().equals(EnumJitShippingOrderMailToCategoryReason.SO_WAREHOUSE_FLIPPED.getId())){
    			soCancellationReason="Warehouse for the following Shipping Orders has been flipped. Information of corresponding POs has been mentioned below. Kindly take the required actions.";
    		}
    		else
    			if(splitShippingOrder!=null && reason.getId().equals(EnumJitShippingOrderMailToCategoryReason.SO_SPLITTED.getId())){
    			soCancellationReason="Shipping Order - "+shippingOrder.getId()+" has been split. The new splitted Shipping Order Id - "+splitShippingOrder.getId()+" Information of corresponding POs has been mentioned below. Kindly take the required actions.";
    		}
    	valuesMap.put("soCancellationReason", soCancellationReason);
    	
    	for(LineItem lineItem : shippingOrder.getLineItems()){
    		if(lineItem!=null && lineItem.getSku().getProductVariant().getProduct().getCategories()!=null && lineItem.getSku().getProductVariant().getProduct().getCategories().size()>0){
    			Category category = lineItem.getSku().getProductVariant().getProduct().getCategories().get(0);
    			emailIds.addAll(emailManager.categoryAdmins(category));
    		}
    	}
    	
    	String fromPurchaseEmail = "purchase@healthkart.com";
    	emailIds.add(fromPurchaseEmail);
    	Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.jitShippingOrderStatusChangeMail);
    	User user = userService.getAdminUser();
    	boolean sent = emailService.sendEmail(freemarkerTemplate, valuesMap, user.getEmail(),"HK Admin", fromPurchaseEmail, "", null, null, emailIds, null, null, null);
    	return sent;
    }

    public void sendManualRefundTaskToAdmin(Double amount, String gatewayOrderId, String gateway) {
        HashMap valueMap = new HashMap();
        valueMap.put("username","Admin");
        valueMap.put("gatewayOrderId", gatewayOrderId);
        valueMap.put("amount",amount);
        valueMap.put("gateway",gateway);
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.manualRefundTaskToAdminMail);
        emailService.sendHtmlEmail(freemarkerTemplate, valueMap, "cs.payments@healthkart.com", "Admin");
    }


    static enum Product_Status {

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

	public PurchaseOrderManager getPurchaseOrderManager() {
		return purchaseOrderManager;
	}

	public void setPurchaseOrderManager(PurchaseOrderManager purchaseOrderManager) {
		this.purchaseOrderManager = purchaseOrderManager;
	}

	public PurchaseOrderPDFGenerator getPurchaseOrderPDFGenerator() {
		return purchaseOrderPDFGenerator;
	}

	public void setPurchaseOrderPDFGenerator(PurchaseOrderPDFGenerator purchaseOrderPDFGenerator) {
		this.purchaseOrderPDFGenerator = purchaseOrderPDFGenerator;
	}
}