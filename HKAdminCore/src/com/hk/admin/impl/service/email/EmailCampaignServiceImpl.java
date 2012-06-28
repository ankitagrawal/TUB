package com.hk.admin.impl.service.email;

import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.core.EmailType;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.admin.util.S3Utils;
import com.hk.admin.pact.dao.email.AdminEmailCampaignDao;
import com.akube.framework.dao.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jets3t.service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class EmailCampaignServiceImpl implements EmailCampaignService {

  @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
  String awsAccessKey;

  @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
  String awsSecretKey;

  public static String awsBucket = "healthkart-pratham";

  @Autowired
  AdminEmailCampaignDao adminEmailCampaignDao;

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return getAdminEmailCampaignDao().getEmailCampaignSentCount(emailCampaign);
  }

  public void uploadEmailContent(File emailContent){
    S3Utils.uploadMultipleData(awsAccessKey, awsSecretKey, emailContent, awsBucket);
  }

  public void uploadHtml(File htmlFile, String key){
    S3Utils.uploadData(awsAccessKey, awsSecretKey, htmlFile, key, awsBucket);
  }

  /**
   * returns email campaigns belonging to a particular email type. the result is reverse sorted on basis of create date
   *
   * @param emailType
   * @param page
   * @param perPage
   * @return
   */
  public Page getEmailCampaignByEmailType(EmailType emailType, int page, int perPage) {
    return getAdminEmailCampaignDao().getEmailCampaignByEmailType(emailType, page, perPage);
  }

  /**
   * returns all the email campaigns which have their data uplaoded on Amazon S3
   *
   * @return
   */
  public List<EmailCampaign> getAmazonS3EmailCampaigns() {
    return getAdminEmailCampaignDao().getAmazonS3EmailCampaigns();
  }

  public AdminEmailCampaignDao getAdminEmailCampaignDao() {
    return adminEmailCampaignDao;
  }

  public void setAdminEmailCampaignDao(AdminEmailCampaignDao adminEmailCampaignDao) {
    this.adminEmailCampaignDao = adminEmailCampaignDao;
  }
}
