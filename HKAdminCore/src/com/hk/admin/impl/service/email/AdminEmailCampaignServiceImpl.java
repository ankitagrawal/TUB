package com.hk.admin.impl.service.email;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.email.AdminEmailCampaignDao;
import com.hk.admin.pact.service.email.AdminEmailCampaignService;
import com.hk.admin.util.S3Utils;
import com.hk.constants.core.Keys;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;

@Service
public class AdminEmailCampaignServiceImpl implements AdminEmailCampaignService {

  @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
  String awsAccessKey;

  @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
  String awsSecretKey;

   @Value("#{hkEnvProps['" + Keys.Env.bucket + "']}")
   String awsBucket;

  @Autowired
  AdminEmailCampaignDao adminEmailCampaignDao;

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return getAdminEmailCampaignDao().getEmailCampaignSentCount(emailCampaign);
  }

  public Boolean uploadEmailContent(File emailContent){
    return S3Utils.uploadMultipleData(awsAccessKey, awsSecretKey, emailContent, awsBucket);
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

  public AdminEmailCampaignDao getAdminEmailCampaignDao() {
    return adminEmailCampaignDao;
  }

  public void setAdminEmailCampaignDao(AdminEmailCampaignDao adminEmailCampaignDao) {
    this.adminEmailCampaignDao = adminEmailCampaignDao;
  }
}
