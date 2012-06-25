package com.hk.web.action.admin.newsletter;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.MailingListManager;
import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.util.ParseCsvFile;
import com.hk.util.SendGridUtil;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.SEND_MARKETING_MAILS}, authActionBean = AdminPermissionAction.class)
@Component
public class SendEmailNewsletterCampaign extends BasePaginatedAction {

    List<EmailCampaign> emailCampaigns;

    @Validate(required = true)
    EmailCampaign emailCampaign;

    @Autowired
    EmailCampaignDao emailCampaignDao;

    @Validate(required = true, on = {"testCampaign"})
    String testEmails;

    @Validate(required = true, on = {"confirmCampaign", "sendCampaign"})
    String categories;

    private Long userCount = 0L;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    FileBean fileBean;
    FileBean fileBeanForCustomExcel;
    FileBean fileBeanForUserList;

    private Logger logger = LoggerFactory.getLogger(SendEmailNewsletterCampaign.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AdminEmailManager adminEmailManager;
    @Autowired
    private MailingListManager mailingListManager;
    @Autowired
    private AdminEmailService adminEmailService;

    //@Autowired
    private EmailManager emailManager;
    EmailType emailType;
    String sheetName;
    Page emailCampaignPage;
    private Integer defaultPerPage = 20;
    private final int COMMIT_COUNT = 100;
    private final int INITIAL_LIST_SIZE = 100;

    @Autowired
    private RoleDao roleDao;
    private final int maxResultCount = 5000;
    @Autowired
    private EmailRecepientDao emailRecepientDao;
    @Autowired
    private EmailCampaignService emailCampaignService;
    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        emailCampaignPage = emailCampaignDao.getEmailCampaignByEmailType(emailType, getPageNo(), getPerPage());
        if (emailCampaignPage != null) {
            emailCampaigns = emailCampaignPage.getList();
        }
        // emailCampaigns = getEmailCampaignDao().listAllExceptNotifyMe();
        return new ForwardResolution("/pages/admin/newsletter/sendEmailNewsletterCampaign.jsp");
    }

    public Resolution selectCampaign() {
        return new ForwardResolution("/pages/admin/newsletter/selectAudience.jsp");
    }

    public Resolution confirmCampaign() {

        String[] categoryArray = StringUtils.split(categories);

        if (categories.equalsIgnoreCase("all")) {
            userCount = getAdminEmailService().getAllMailingListCount(emailCampaign, Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER)));
        } else if (categories.equalsIgnoreCase("all-unverified")) {
            userCount = getAdminEmailService().getAllMailingListCount(emailCampaign, Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)));
        }else {
            for (String categoryName : categoryArray) {
                Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
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
        for(String userId : userIdList) {
            longUserIdList.add(Long.parseLong(userId));
            if(longUserIdList.size() == maxResultCount) {
                getAdminEmailManager().sendCampaignByUploadingFile(longUserIdList, null, emailCampaign, maxResultCount);
                longUserIdList.clear();
            }
        }
        if(longUserIdList.size() > 0) {
            getAdminEmailManager().sendCampaignByUploadingFile(longUserIdList, null, emailCampaign, maxResultCount);
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
        for(String emailId : userEmails) {
            EmailRecepient emailRecepient = getEmailRecepientDao().createEmailRecepient(emailId);
            emailRecepientRecs.add(emailRecepient);
            if(counter == COMMIT_COUNT) {
                getEmailRecepientDao().saveOrUpdate(emailRecepientRecs);
                counter = 0;
                emailRecepientRecs.clear();
            }
            counter++;
        }
        if(counter > 0) {
            getEmailRecepientDao().saveOrUpdate(emailRecepientRecs);
        }

        userEmails.addAll(emailIdsInEmailRecepient);
        List<String> userEmailsList = new ArrayList<String>();
        for(String emailId : userEmails) {
            userEmailsList.add(emailId);
            if(userEmailsList.size() == maxResultCount) {
                getAdminEmailManager().sendCampaignByUploadingFile(null, userEmailsList, emailCampaign, maxResultCount);
                userEmailsList.clear();
            }
        }

        if(userEmailsList.size() > 0) {
            getAdminEmailManager().sendCampaignByUploadingFile(null, userEmailsList, emailCampaign, maxResultCount);
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
                Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
                if (category != null) {
                    finalCategories.add(category.getName());
                }
            }
        }
        List<EmailRecepient> emailRecepients = new ArrayList<EmailRecepient>();
        do {
            if (categories.equalsIgnoreCase("all")) {
                emailRecepients = getAdminEmailService().getAllMailingList(emailCampaign, Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER)), maxResultCount);
            } else if (categories.equalsIgnoreCase("all-unverified")) {
                emailRecepients = getAdminEmailService().getAllMailingList(emailCampaign, Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)), maxResultCount);
            }

            if (emailRecepients.size() > 0) {
                logger.info(" user list size " + emailRecepients.size());
                String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);
                getAdminEmailManager().sendCampaignMails(emailRecepients, emailCampaign, xsmtpapi);
            }

        } while(emailRecepients.size() > 0);

        if (!categories.equalsIgnoreCase("all") && !categories.equalsIgnoreCase("all-unverified")) {
            for (String categoryName : categoryArray) {
                Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
                if (category != null) {
                    do {
                        emailRecepients.clear();
                        emailRecepients = getAdminEmailService().getMailingListByCategory(emailCampaign, category, maxResultCount);
                        if (emailRecepients.size() > 0) {
                            logger.info(" user list size " + emailRecepients.size());
                            String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);
                            getAdminEmailManager().sendCampaignMails(emailRecepients, emailCampaign, xsmtpapi);
                        }

                    } while(emailRecepients.size() > 0);
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

        getAdminEmailManager().sendMailMergeCampaign(emailCampaign, excelFilePath, sheetName);
        return new ForwardResolution(SendEmailNewsletterCampaign.class, "selectCampaign");
    }

    public Resolution getSentCountForEmailCampaign() {
        Map datamap = new HashMap();
        Long sentCount = emailCampaignService.getEmailCampaignSentCount(emailCampaign);
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

    public EmailCampaignDao getEmailCampaignDao() {
        return emailCampaignDao;
    }

    public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
        this.emailCampaignDao = emailCampaignDao;
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

    public EmailCampaignService getEmailCampaignService() {
        return emailCampaignService;
    }

    public void setEmailCampaignService(EmailCampaignService emailCampaignService) {
        this.emailCampaignService = emailCampaignService;
    }
}
