package com.hk.admin.pact.service.email;

import com.hk.domain.email.EmailCampaign;
import com.hk.domain.core.EmailType;
import com.hk.constants.EnumS3UploadStatus;
import com.akube.framework.dao.Page;

import java.io.File;
import java.util.List;

public interface AdminEmailCampaignService {

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign);

  public void uploadEmailContent(File emailContent);

  public void uploadHtml(File htmlFile, String key);

  /**
   * returns email campaigns belonging to a particular email type. the result is reverse sorted on basis of create date
   *
   * @param emailType
   * @param page
   * @param perPage
   * @return
   */
  public Page getEmailCampaignByEmailType(EmailType emailType, int page, int perPage);

  /**
   * returns all the email campaigns which have their data uplaoded on Amazon S3
   *
   * @return
   */
  public List<EmailCampaign> getAmazonS3EmailCampaigns();
}
