package com.hk.impl.service.marketing;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.pact.service.marketing.EmailCampaignService;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.domain.email.EmailCampaign;

@Service
public class EmailCampaignServiceImpl implements EmailCampaignService {

  @Autowired
  private EmailCampaignDao emailCampaignDao;

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return emailCampaignDao.getEmailCampaignSentCount(emailCampaign);
  }

  public EmailCampaignDao getEmailCampaignDao() {
    return emailCampaignDao;
  }

  public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
    this.emailCampaignDao = emailCampaignDao;
  }
}
