package com.hk.impl.service.marketing;

import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.domain.email.EmailCampaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailCampaignServiceImpl implements EmailCampaignService {

  @Autowired
  EmailCampaignDao emailCampaignDao;

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return getEmailCampaignDao().getEmailCampaignSentCount(emailCampaign);
  }

  public EmailCampaignDao getEmailCampaignDao() {
    return emailCampaignDao;
  }

  public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
    this.emailCampaignDao = emailCampaignDao;
  }
}
