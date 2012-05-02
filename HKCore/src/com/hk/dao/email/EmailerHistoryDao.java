package com.hk.dao.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailerHistory;
import com.hk.service.UserService;

@Repository
public class EmailerHistoryDao extends BaseDaoImpl {

    @Autowired
    private UserService userService;

    /*
     * public EmailerHistoryDao() { super(EmailerHistory.class); }
     */

    @Transactional
    public EmailerHistory save(EmailerHistory emailerHistory) {
        if (emailerHistory != null) {
            if (emailerHistory.getSendDate() == null) {
                emailerHistory.setSendDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return (EmailerHistory) super.save(emailerHistory);
    }

    public EmailerHistory createEmailerHistory(String senderEmail, String senderName, EmailType emailType, EmailRecepient emailRecepient, EmailCampaign emailCampaign,
            String customText) {
        EmailerHistory emailerHistory = new EmailerHistory();
        emailerHistory.setEmailRecepient(emailRecepient);
        emailerHistory.setEmailCampaign(emailCampaign);
        emailerHistory.setCustomMessage(customText);
        emailerHistory.setSenderEmail(senderEmail);
        emailerHistory.setSenderName(senderName);
        emailerHistory.setEmailType(emailType);
        emailerHistory.setUser(getUserService().findByLogin(senderEmail));
        return save(emailerHistory);
    }

    public EmailerHistory findEmailRecipientByCampaign(EmailRecepient emailRecepient, EmailCampaign emailCampaign) {
        return (EmailerHistory) getSession().createQuery("from EmailerHistory where emailRecepient =:emailRecepient and emailCampaign =:emailCampaign").setParameter(
                "emailCampaign", emailCampaign).setParameter("emailRecepient", emailRecepient).uniqueResult();
    }

    public EmailerHistory findEmailRecipientByCampaignAndEmailType(EmailRecepient emailRecepient, EmailCampaign emailCampaign, EmailType emailType) {
        return (EmailerHistory) getSession().createQuery("from EmailerHistory where emailRecepient =:emailRecepient and emailCampaign =:emailCampaign and emailType != :emailType").setParameter(
                "emailCampaign", emailCampaign).setParameter("emailRecepient", emailRecepient).setParameter("emailType", emailType).uniqueResult();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

   
    
}
