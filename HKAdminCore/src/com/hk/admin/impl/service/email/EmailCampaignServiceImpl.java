package com.hk.admin.impl.service.email;

import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.domain.email.EmailCampaign;
import com.hk.constants.EnumS3UploadStatus;
import com.hk.constants.core.Keys;
import com.hk.admin.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jets3t.service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
public class EmailCampaignServiceImpl implements EmailCampaignService {

  @Value("#{hkEnvProps['" + Keys.Env.accessKey + "']}")
  String awsAccessKey;

  @Value("#{hkEnvProps['" + Keys.Env.secretKey + "']}")
  String awsSecretKey;

  public static String awsBucket = "healthkart-rimal";

  S3Utils s3Utils;

  @Autowired
  EmailCampaignDao emailCampaignDao;

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return getEmailCampaignDao().getEmailCampaignSentCount(emailCampaign);
  }

  public void uploadEmailContent(File emailContent, EmailCampaign emailCampaign) throws NoSuchAlgorithmException, IOException, ServiceException {
    s3Utils.uploadMultipleData(awsAccessKey,awsSecretKey,emailContent,awsBucket);
  }

  public EmailCampaignDao getEmailCampaignDao() {
    return emailCampaignDao;
  }

  public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
    this.emailCampaignDao = emailCampaignDao;
  }
}
