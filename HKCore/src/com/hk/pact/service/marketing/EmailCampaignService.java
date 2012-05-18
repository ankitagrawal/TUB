package com.hk.pact.service.marketing;

import com.hk.domain.email.EmailCampaign;

/**
 * Created by IntelliJ IDEA.
 * User: Rimal
 * Date: May 18, 2012
 * Time: 6:38:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailCampaignService {

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign);

}
