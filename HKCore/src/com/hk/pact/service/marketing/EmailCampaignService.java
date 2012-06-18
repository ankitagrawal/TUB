package com.hk.pact.service.marketing;

import com.hk.domain.email.EmailCampaign;
import com.hk.constants.EnumS3UploadStatus;

import java.io.File;
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//
//import org.jets3t.service.ServiceException;

public interface EmailCampaignService {

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign);

  public EnumS3UploadStatus uploadEmailContent(File emailContent, EmailCampaign emailCampaign) throws Exception;
}
