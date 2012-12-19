package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.MailingListManager;
import com.hk.admin.manager.SendCampaignResult;
import com.hk.admin.pact.service.email.AdminEmailCampaignService;
import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.cache.CategoryCache;
import com.hk.cache.RoleCache;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.util.ParseCsvFile;
import com.hk.util.SendGridUtil;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.SEND_MARKETING_MAILS }, authActionBean = AdminPermissionAction.class)
@Component
public class SendEmailNewsletterCampaign extends BasePaginatedAction {

    List<EmailCampaign>               emailCampaigns;

    @Validate(required = true)
    EmailCampaign                     emailCampaign;

    @Validate(required = true, on = { "testCampaign" })
    String                            testEmails;

    @Validate(required = true, on = { "confirmCampaign", "sendCampaign" })
    String                            categories;

    private Long                      userCount      = 0L;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                            adminUploadsPath;

    FileBean                          fileBean;
    FileBean                          fileBeanForCustomExcel;
    FileBean                          fileBeanForUserList;

    private Logger                    logger         = LoggerFactory.getLogger(SendEmailNewsletterCampaign.class);

    @Autowired
    private UserService               userService;
    @Autowired
    private CategoryService           categoryService;
    @Autowired
    private AdminEmailManager         adminEmailManager;
    @Autowired
    private MailingListManager        mailingListManager;
    @Autowired
    private AdminEmailService         adminEmailService;

    // @Autowired
    private EmailManager              emailManager;
    EmailType                         emailType;
    String                            sheetName;
    String                            mailGunCampaignId;
    Page                              emailCampaignPage;
    private Integer                   defaultPerPage = 20;
    private final int                 COMMIT_COUNT   = 100;
    // private final int INITIAL_LIST_SIZE = 100;

    @Autowired
    private RoleDao                   roleDao;
    private final int                 maxResultCount = 500;
    @Autowired
    private EmailRecepientDao         emailRecepientDao;
    @Autowired
    private AdminEmailCampaignService adminEmailCampaignService;

    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyEmail + "']}")
    String                            senderEmail;

    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyName + "']}")
    String                            senderName;

    String                            replyToEmail   = "info@healthkart.com";

    boolean                           sendHeaders    = true;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        emailCampaignPage = adminEmailCampaignService.getEmailCampaignByEmailType(emailType, getPageNo(), getPerPage());
        if (emailCampaignPage != null) {
            emailCampaigns = emailCampaignPage.getList();
        }
        return new ForwardResolution("/pages/admin/newsletter/sendEmailNewsletterCampaign.jsp");
    }

    public Resolution selectCampaign() {
        return new ForwardResolution("/pages/admin/newsletter/selectAudience.jsp");
    }

    public Resolution confirmCampaign() {

        String[] categoryArray = StringUtils.split(categories);

        if (categories.equalsIgnoreCase("all")) {
            // userCount = getAdminEmailService().getAllMailingListCount(emailCampaign,
            // Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER)));
            Role hkUserRole = RoleCache.getInstance().getRoleByName(EnumRole.HK_USER).getRole();
            userCount = getAdminEmailService().getAllMailingListCount(emailCampaign, Arrays.asList(hkUserRole));
        } else if (categories.equalsIgnoreCase("all-unverified")) {
            // userCount = getAdminEmailService().getAllMailingListCount(emailCampaign,
            // Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)));
            Role hkUnverifiedRole = RoleCache.getInstance().getRoleByName(EnumRole.HK_UNVERIFIED).getRole();
            userCount = getAdminEmailService().getAllMailingListCount(emailCampaign, Arrays.asList(hkUnverifiedRole));
        } else {
            for (String categoryName : categoryArray) {
                // Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
                Category category = CategoryCache.getInstance().getCategoryByName(StringUtils.trim(categoryName)).getCategory();
                if (category != null) {
                    userCount += getAdminEmailService().getMailingListCountByCategory(emailCampaign, category);
                }
            }
        }
        logger.info(" user list size " + userCount);
        return new ForwardResolution("/pages/admin/newsletter/confirmSendCampaign.jsp");
    }

    public Resolution testCampaign() {
        String[] emails = StringUtils.split(testEmails, ',');
        Set<User> users = new HashSet<User>();

        for (String email : emails) {
            List<User> usersByEmail = getUserService().findByEmail(StringUtils.trim(email));
            if (usersByEmail != null && usersByEmail.size() > 0) {
                users.add(usersByEmail.get(0));
            }
        }

        // send test emails to campaign
        getAdminEmailManager().sendTestCampaignMails(users, emailCampaign);

        addRedirectAlertMessage(new SimpleMessage("Test emails sent : " + emailCampaign.getName()));
        return selectCampaign();
    }

    private List<String> getListOfStringFromCsvFile(FileBean fileBean) throws IOException {
        String excelFilePath = adminUploadsPath + "/emailList/" + System.currentTimeMillis() + ".txt";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        List<String> csvToStringList = ParseCsvFile.getStringListFromCsv(excelFilePath);
        return csvToStringList;
    }

    public Resolution sendCampaignViaCsvUserIDs() throws IOException {
        List<String> userIdList = getListOfStringFromCsvFile(fileBeanForUserList);
        List<Long> longUserIdList = new ArrayList<Long>();

        getAdminEmailManager().populateEmailRecepient(userIdList, maxResultCount);
        for (String userId : userIdList) {
            longUserIdList.add(Long.parseLong(userId));
            if (longUserIdList.size() == maxResultCount) {
                getAdminEmailManager().sendCampaignByUploadingFile(longUserIdList, null, emailCampaign, maxResultCount, senderEmail, senderName, replyToEmail);
                longUserIdList.clear();
            }
        }
        if (longUserIdList.size() > 0) {
            getAdminEmailManager().sendCampaignByUploadingFile(longUserIdList, null, emailCampaign, maxResultCount, senderEmail, senderName, replyToEmail);
        }

        addRedirectAlertMessage(new SimpleMessage("Sending campaign in progress : " + emailCampaign.getName()));
        return new RedirectResolution(EmailNewsletterAdmin.class);
    }

    public Resolution sendCampaignViaCsvUserEmails() throws IOException {
        List<String> userEmails = getListOfStringFromCsvFile(fileBean);
        List<String> emailIdsInEmailRecepient = getEmailRecepientDao().findEmailIdsPresentInEmailRecepient(userEmails);

        userEmails.removeAll(emailIdsInEmailRecepient);

        List<EmailRecepient> emailRecepientRecs = new ArrayList<EmailRecepient>();

        int counter = 0;
        for (String emailId : userEmails) {
            EmailRecepient emailRecepient = getEmailRecepientDao().createEmailRecepientObject(emailId);
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

        userEmails.addAll(emailIdsInEmailRecepient);
        List<String> userEmailsList = new ArrayList<String>();
        for (String emailId : userEmails) {
            userEmailsList.add(emailId);
            if (userEmailsList.size() == maxResultCount) {
                getAdminEmailManager().sendCampaignByUploadingFile(null, userEmailsList, emailCampaign, maxResultCount, senderEmail, senderName, replyToEmail);
                userEmailsList.clear();
            }
        }

        if (userEmailsList.size() > 0) {
            getAdminEmailManager().sendCampaignByUploadingFile(null, userEmailsList, emailCampaign, maxResultCount, senderEmail, senderName, replyToEmail);
        }

        addRedirectAlertMessage(new SimpleMessage("Sending campaign in progress : " + emailCampaign.getName()));
        return new RedirectResolution(EmailNewsletterAdmin.class);
    }

    public Resolution sendCampaign() {

        getAdminEmailManager().populateEmailRecepient(null, maxResultCount);

        String[] categoryArray = StringUtils.split(categories);
        List<String> finalCategories = new ArrayList<String>();

        if (categories.equalsIgnoreCase("all")) {
            finalCategories.add("all_categories");
        } else if (categories.equalsIgnoreCase("all-unverified")) {
            finalCategories.add("all_categories");
            finalCategories.add("unverified");
        } else {
            for (String categoryName : categoryArray) {
                // Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
                Category category = CategoryCache.getInstance().getCategoryByName(StringUtils.trim(categoryName)).getCategory();
                if (category != null) {
                    finalCategories.add(category.getName());
                }
            }
        }
        String xsmtpapi = "";
        if (sendHeaders) {
            xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);
        }
        List<EmailRecepient> emailRecepients = new ArrayList<EmailRecepient>();
        Long emailRecepientCount = getAdminEmailService().getMailingListCountByCampaign(emailCampaign);
        Long MAX_EMAILS = 200000L; // todo: HACKY-Code to save us from going into infinite loop..Need better
        // implementation here
        Long usersBrowsed = 0L;
        int pageCount = 0;
        do {
            if (categories.equalsIgnoreCase("all")) {
                // emailRecepients = getAdminEmailService().getAllMailingList(emailCampaign,
                // Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER)), pageCount, maxResultCount);
                Role hkUserRole = RoleCache.getInstance().getRoleByName(EnumRole.HK_USER).getRole();
                emailRecepients = getAdminEmailService().getAllMailingList(emailCampaign, Arrays.asList(hkUserRole), pageCount, maxResultCount);
            } else if (categories.equalsIgnoreCase("all-unverified")) {
                Role hkUnverifiedRole = RoleCache.getInstance().getRoleByName(EnumRole.HK_UNVERIFIED).getRole();
                emailRecepients = getAdminEmailService().getAllMailingList(emailCampaign, Arrays.asList(hkUnverifiedRole), maxResultCount);
            }
            if ((emailRecepients == null) || emailRecepients.isEmpty()) {
                break;
            }
            List<String> emailRecepientsWithHistory = getAdminEmailService().getEmailRecepientsByEmailIds(emailCampaign, emailRecepients);
            Set<String> historyEmails = new HashSet<String>();
            historyEmails.addAll(emailRecepientsWithHistory);
            List<EmailRecepient> validEmailRecepients = new ArrayList<EmailRecepient>();

            SendCampaignResult sendCampaignResult = new SendCampaignResult();
            for (EmailRecepient emailRecepient : emailRecepients) {
                // remove if this user is already sent email for this campaign
                if (!historyEmails.contains(emailRecepient.getEmail()) && !sendCampaignResult.getErrorEmails().contains(emailRecepient.getEmail())) {
                    validEmailRecepients.add(emailRecepient);
                }
            }

            if (validEmailRecepients.size() > 0) {
                logger.info(" user list size " + validEmailRecepients.size());
                getAdminEmailManager().sendCampaignMails(validEmailRecepients, emailCampaign, senderEmail, senderName, replyToEmail, xsmtpapi, sendCampaignResult);
                if (!sendCampaignResult.isCampaignSentSuccess()) {
                    logger.error("Error in sending file upload campaing to :" + sendCampaignResult.getErrorEmails().toString());
                }
            }
            pageCount++;
            usersBrowsed += maxResultCount;

        } while ((usersBrowsed < emailRecepientCount) && (emailRecepientCount < MAX_EMAILS));

        if (!categories.equalsIgnoreCase("all") && !categories.equalsIgnoreCase("all-unverified")) {
            for (String categoryName : categoryArray) {
                // Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
                Category category = CategoryCache.getInstance().getCategoryByName(StringUtils.trim(categoryName)).getCategory();
                if (category != null) {
                    SendCampaignResult sendCampaignResult = new SendCampaignResult();
                    do {
                        emailRecepients.clear();
                        emailRecepients = getAdminEmailService().getMailingListByCategory(emailCampaign, category, maxResultCount);
                        if (emailRecepients.size() > 0) {
                            List<EmailRecepient> emailRecepientsToSend = new ArrayList<EmailRecepient>();

                            for (EmailRecepient emailRecepient : emailRecepients) {
                                if (!sendCampaignResult.getErrorEmails().contains(emailRecepient.getEmail())) {
                                    emailRecepientsToSend.add(emailRecepient);
                                }
                            }

                            if (emailRecepientsToSend.size() > 0) {
                                logger.info(" user list size " + emailRecepients.size());
                                getAdminEmailManager().sendCampaignMails(emailRecepientsToSend, emailCampaign, senderEmail, senderName, replyToEmail, xsmtpapi, sendCampaignResult);
                                logger.info(" send campaign Result " + sendCampaignResult.isCampaignSentSuccess() + " error " + sendCampaignResult.getErrorEmails().toString());
                                if (!sendCampaignResult.isCampaignSentSuccess()) {
                                    logger.error("Error in sending file upload campaing to :" + sendCampaignResult.getErrorEmails().toString());
                                }
                            } else {
                                logger.info(" no valid email recipents so breaking out");
                                break;
                            }

                        }

                    } while (emailRecepients.size() > 0);
                }
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Sending campaign in progress : " + emailCampaign.getName()));
        return new RedirectResolution(EmailNewsletterAdmin.class);
    }

    public Resolution sendEmailViaExcel() throws IOException {
        String excelFilePath = adminUploadsPath + "/emailList/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBeanForCustomExcel.save(excelFile);

        Boolean success = getAdminEmailManager().sendMailMergeCampaign(emailCampaign, excelFilePath, sheetName, mailGunCampaignId);
        if (success) {
            addRedirectAlertMessage(new SimpleMessage("Email sending for campaign:" + emailCampaign.getName() + "in progress...."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("Email sending for campaign:" + emailCampaign.getName() + "failed!"));
        }
        return new ForwardResolution(SendEmailNewsletterCampaign.class, "selectCampaign");
    }

    @SuppressWarnings("unchecked")
    public Resolution getSentCountForEmailCampaign() {
        Map datamap = new HashMap();
        Long sentCount = adminEmailCampaignService.getEmailCampaignSentCount(emailCampaign);
        datamap.put("sentCountValue", sentCount);
        datamap.put("emailCampaignId", emailCampaign.getId().toString());
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Sent Count Generated for Email Campaign", datamap);
        return new JsonResolution(healthkartResponse);
    }

    public List<EmailCampaign> getEmailCampaigns() {
        return emailCampaigns;
    }

    public EmailCampaign getEmailCampaign() {
        return emailCampaign;
    }

    public void setEmailCampaign(EmailCampaign emailCampaign) {
        this.emailCampaign = emailCampaign;
    }

    public String getTestEmails() {
        return testEmails;
    }

    public void setTestEmails(String testEmails) {
        this.testEmails = testEmails;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Long getUserCount() {
        return userCount;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public FileBean getFileBeanForUserList() {
        return fileBeanForUserList;
    }

    public void setFileBeanForUserList(FileBean fileBeanForUserList) {
        this.fileBeanForUserList = fileBeanForUserList;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

    public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
        this.adminEmailManager = adminEmailManager;
    }

    public MailingListManager getMailingListManager() {
        return mailingListManager;
    }

    public void setMailingListManager(MailingListManager mailingListManager) {
        this.mailingListManager = mailingListManager;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return emailCampaignPage == null ? 0 : emailCampaignPage.getTotalPages();
    }

    public int getResultCount() {
        return emailCampaignPage == null ? 0 : emailCampaignPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("emailType");
        return params;
    }

    public FileBean getFileBeanForCustomExcel() {
        return fileBeanForCustomExcel;
    }

    public void setFileBeanForCustomExcel(FileBean fileBeanForCustomExcel) {
        this.fileBeanForCustomExcel = fileBeanForCustomExcel;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public EmailRecepientDao getEmailRecepientDao() {
        return emailRecepientDao;
    }

    public void setEmailRecepientDao(EmailRecepientDao emailRecepientDao) {
        this.emailRecepientDao = emailRecepientDao;
    }

    public AdminEmailService getAdminEmailService() {
        return adminEmailService;
    }

    public void setAdminEmailService(AdminEmailService adminEmailService) {
        this.adminEmailService = adminEmailService;
    }

    public AdminEmailCampaignService getEmailCampaignService() {
        return adminEmailCampaignService;
    }

    public void setEmailCampaignService(AdminEmailCampaignService adminEmailCampaignService) {
        this.adminEmailCampaignService = adminEmailCampaignService;
    }

    public String getMailGunCampaignId() {
        return mailGunCampaignId;
    }

    public void setMailGunCampaignId(String mailGunCampaignId) {
        this.mailGunCampaignId = mailGunCampaignId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public boolean isSendHeaders() {
        return sendHeaders;
    }

    public void setSendHeaders(boolean sendHeaders) {
        this.sendHeaders = sendHeaders;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }
}
