package com.hk.impl.dao.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailerHistory;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.email.EmailerHistoryDao;
import com.hk.pact.service.UserService;

@Repository
public class EmailerHistoryDaoImpl extends BaseDaoImpl implements EmailerHistoryDao {

    @Autowired
    private UserService userService;

    @Transactional
    public EmailerHistory save(EmailerHistory emailerHistory) {
        if (emailerHistory != null) {
            if (emailerHistory.getSendDate() == null) {
                emailerHistory.setSendDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return emailerHistory; //super.save(emailerHistory);
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

    @SuppressWarnings("unchecked")
    public List<EmailerHistory> findEmailRecipientByCampaign(EmailRecepient emailRecepient, EmailCampaign emailCampaign) {
        return getSession().createQuery("from EmailerHistory eh where eh.emailRecepient.email =:email and eh.emailCampaign.id =:emailCampaignId").setParameter("email",
                emailRecepient.getEmail()).setParameter("emailCampaignId", emailCampaign.getId()).list();
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
