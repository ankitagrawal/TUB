package com.hk.admin.pact.service.email;

import java.io.File;

import com.akube.framework.dao.Page;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;

public interface AdminEmailCampaignService {

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign);

  public Boolean uploadEmailContent(File emailContent);

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
}
