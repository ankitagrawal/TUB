package com.hk.web.action.admin.newsletter;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.sourceforge.stripes.action.*;
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
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.util.ParseCsvFile;
import com.hk.util.SendGridUtil;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;

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

  int userCount;
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
  private EmailCampaignService emailCampaignService;
  @Autowired
  private AdminEmailManager adminEmailManager;
  @Autowired
  private MailingListManager mailingListManager;
  //@Autowired
  private EmailManager emailManager;
  EmailType emailType;
  String sheetName;
  Page emailCampaignPage;
  private Integer defaultPerPage = 20;

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
    Set<User> users = new HashSet<User>();
    Set<User> allUsers = new HashSet<User>();
    Set<User> tempUsers = new HashSet<User>();
    int perPage = 499;
    int pageNo = 1;
    boolean firstTime = true;

    while (tempUsers.size() > 0 || firstTime) {
      users.clear();
      if (categories.equalsIgnoreCase("all")) {
        users.addAll(mailingListManager.getAllUserList(pageNo, perPage));
      } else if (categories.equalsIgnoreCase("all-unverified")) {
        users.addAll(mailingListManager.getAllUnverifiedUserList(pageNo, perPage));
      }

      if (users.size() > 0) {
        logger.info("page no " + pageNo + " user list size " + users.size() + users.iterator().next().getEmail());
      }

      if (users.size() == 0 || users.size() < perPage) {
        break;
      }
      tempUsers = users;
      allUsers.addAll(users);
      firstTime = false;
      ++pageNo;
    }

    pageNo = 1;

    if (!categories.equalsIgnoreCase("all") && !categories.equalsIgnoreCase("all-unverified")) {
      for (String categoryName : categoryArray) {
        Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
        if (category != null) {
          while (tempUsers.size() > 0 || firstTime) {
            users.clear();
            users.addAll(mailingListManager.getUserList(category, pageNo, perPage));
            if (users.size() > 0) {
              logger.info("page no " + pageNo + " user list size " + users.size() + users.iterator().next().getEmail());
            }
            if (users.size() == 0 || users.size() < perPage) {
              break;
            }
            tempUsers = users;
            allUsers.addAll(users);
            firstTime = true;
            ++pageNo;
          }
        }
      }
    }
    userCount = allUsers.size();
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

  public Resolution sendCampaignViaCsvUserIDs() throws IOException {
    String excelFilePath = adminUploadsPath + "/emailList/" + System.currentTimeMillis() + ".txt";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    fileBeanForUserList.save(excelFile);
    Set<User> users = new HashSet<User>();

    for (String userId : ParseCsvFile.getStringListFromCsv(excelFilePath)) {
      User user = getUserService().getUserById(Long.valueOf(userId));
      if (user != null) {
        users.add(user);
      }
    }
    List<String> finalCategories = new ArrayList<String>();
    finalCategories.add("User Ids Excel");

    // construct the headers to send
    String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);

    // send campaign to user emails
    getAdminEmailManager().sendCampaignMails(users, emailCampaign, xsmtpapi);

    addRedirectAlertMessage(new SimpleMessage("Sending campaign in progress : " + emailCampaign.getName()));
    return new RedirectResolution(EmailNewsletterAdmin.class);
  }

  public Resolution sendCampaignViaCsvUserEmails() throws IOException {
    String excelFilePath = adminUploadsPath + "/emailList/" + System.currentTimeMillis() + ".txt";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    fileBean.save(excelFile);
    List<String> users = new ArrayList<String>();
    users.addAll(ParseCsvFile.getStringListFromCsv(excelFilePath));

    List<String> finalCategories = new ArrayList<String>();
    finalCategories.add("User Ids Excel");

    // construct the headers to send
    String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);

    // send campaign to user emails
    getAdminEmailManager().sendCampaignMailsToListOfEmailIds(users, emailCampaign, xsmtpapi);

    addRedirectAlertMessage(new SimpleMessage("Sending campaign in progress : " + emailCampaign.getName()));
    return new RedirectResolution(EmailNewsletterAdmin.class);
  }

  public Resolution sendCampaign() {
    String[] categoryArray = StringUtils.split(categories);
    Set<User> users = new HashSet<User>();
    Set<User> tempUsers = new HashSet<User>();

    int perPage = 499;
    int pageNo = 1;

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

    String xsmtpapi = SendGridUtil.getSendGridEmailNewsLetterHeaderJson(finalCategories, emailCampaign);
    boolean firstTime = true;

    while (tempUsers.size() > 0 || firstTime) {
      // logger.info("Reached Level 0");
      users.clear();
      if (categories.equalsIgnoreCase("all")) {
        // logger.info("Reached Level 1");
        users.addAll(mailingListManager.getAllUserList(pageNo, perPage));
      } else if (categories.equalsIgnoreCase("all-unverified")) {
        // logger.info("Reached Level 2");
        users.addAll(mailingListManager.getAllUnverifiedUserList(pageNo, perPage));
      }

      if (users.size() > 0) {
        logger.info("page no " + pageNo + " user list size " + users.size() + users.iterator().next().getEmail());
      }

      getAdminEmailManager().sendCampaignMails(users, emailCampaign, xsmtpapi);
      // logger.info("Reached Level 4");
      if (users.size() == 0 || users.size() < perPage) {
        // logger.info("Reached Level 5");
        break;
      }
      // logger.info("Reached Level 6");
      tempUsers = users;
      firstTime = false;
      ++pageNo;
    }

    firstTime = true;

    pageNo = 1;

    if (!categories.equalsIgnoreCase("all") && !categories.equalsIgnoreCase("all-unverified")) {
      logger.info("Reached Level 7");
      for (String categoryName : categoryArray) {
        logger.info("Reached Level 8");
        Category category = getCategoryService().getCategoryByName(StringUtils.trim(categoryName));
        if (category != null) {
          logger.info("Reached Level 9");
          while (tempUsers.size() > 0 || firstTime) {
            logger.info("Reached Level 10");
            users.clear();
            users.addAll(mailingListManager.getUserList(category, pageNo, perPage));
            logger.info("page no " + pageNo + " user list size " + users.size());

            getAdminEmailManager().sendCampaignMails(users, emailCampaign, xsmtpapi);
            if (users.size() == 0 || users.size() < perPage) {
              logger.info("Reached Level 11");
              break;
            }
            tempUsers = new HashSet<User>(users);
            logger.info("level 12, page no " + pageNo + " user list size " + tempUsers.size());
            firstTime = false;
            ++pageNo;
            users.clear();
          }
          logger.info("Reached Level 13");
        }
        logger.info("Reached Level 14");
      }
      logger.info("Reached Level 15");
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

  public int getUserCount() {
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

  public EmailCampaignService getEmailCampaignService() {
    return emailCampaignService;
  }

  public void setEmailCampaignService(EmailCampaignService emailCampaignService) {
    this.emailCampaignService = emailCampaignService;
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
}
