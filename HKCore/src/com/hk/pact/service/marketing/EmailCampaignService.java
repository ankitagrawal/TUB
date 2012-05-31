package com.hk.pact.service.marketing;

import com.hk.domain.email.EmailCampaign;

public interface EmailCampaignService {

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign);

}
