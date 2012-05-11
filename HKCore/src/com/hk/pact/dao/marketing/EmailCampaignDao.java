package com.hk.pact.dao.marketing;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.BaseDao;

public interface EmailCampaignDao extends BaseDao {

    public EmailCampaign save(EmailCampaign emailCampaign);

    public EmailCampaign getOrCreateEmailCampaign(String campaignName, Long minDayGap, String emailTemplate);

    public List<EmailCampaign> listAllExceptNotifyMe();

    public List<EmailCampaign> listAllMissYouCampaigns();
    
    public Page getEmailCampaignByEmailType(EmailType emailType, int page, int perPage) ;
     
    public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) ;
     
    public Date getLastDateOfEmailCampaignMailSentToEmailRecepient(EmailCampaign emailCampaign, EmailRecepient emailRecepient) ;
     

}
