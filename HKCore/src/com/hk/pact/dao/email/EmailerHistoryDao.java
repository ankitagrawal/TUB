package com.hk.pact.dao.email;

import java.util.List;

import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailerHistory;
import com.hk.pact.dao.BaseDao;

public interface EmailerHistoryDao extends BaseDao {

    public EmailerHistory save(EmailerHistory emailerHistory);

    public EmailerHistory createEmailerHistory(String senderEmail, String senderName, EmailType emailType, EmailRecepient emailRecepient, EmailCampaign emailCampaign,
            String customText);

    public EmailerHistory createEmailerHistoryObject(String senderEmail, String senderName, EmailType emailType, EmailRecepient emailRecepient, EmailCampaign emailCampaign,
            String customText);

    public List<EmailerHistory>  findEmailRecipientByCampaign(EmailRecepient emailRecepient, EmailCampaign emailCampaign);

    public EmailerHistory findEmailRecipientByCampaignAndEmailType(EmailRecepient emailRecepient, EmailCampaign emailCampaign, EmailType emailType);

}
