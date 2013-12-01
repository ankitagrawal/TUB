package com.hk.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumEmailType;
import com.hk.domain.core.EmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.EmailerHistoryDao;

/**
 * User: rahul Time: 5 May, 2010 6:06:18 PM
 */
@Component
public class WOMMEmailManager {

    @Autowired
    private EmailRecepientDao emailRecepientDao;
    @Autowired
    private EmailerHistoryDao emailerHistoryDao;
    @Autowired
    private LinkManager       linkManager;
    @Autowired
    private EmailManager      emailManager;
    @Autowired
    private BaseDao           baseDao;

    public void sendEmails(User user, String senderName, String senderEmail, Coupon coupon, List<String> recepientEmailList, EnumEmailType emailType, String customText,
            EmailCampaign emailCampaign) {
        for (String recepientEmail : recepientEmailList) {
            EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(recepientEmail);
            if (emailRecepient.isEmailAllowed()) {
                getEmailerHistoryDao().createEmailerHistory(senderEmail, senderName, getBaseDao().get(EmailType.class, emailType.getId()), emailRecepient, emailCampaign,
                        customText);
                String unsubscribeLink = getLinkManager().getEmailUnsubscribeLink(emailRecepient);
                String signupLink = getLinkManager().getReferralSignupLink(user);

                // System.out.println("unsubscription liknk for user:" + emailRecepient.getEmail() + " is : " +
                // unsubscribeLink);

                getEmailManager().sendReferralEmail(emailRecepient.getEmail(), senderName, senderEmail, unsubscribeLink, signupLink, coupon.getCode(), customText);

            }
        }
    }

    public EmailRecepientDao getEmailRecepientDao() {
        return emailRecepientDao;
    }

    public void setEmailRecepientDao(EmailRecepientDao emailRecepientDao) {
        this.emailRecepientDao = emailRecepientDao;
    }

    public EmailerHistoryDao getEmailerHistoryDao() {
        return emailerHistoryDao;
    }

    public void setEmailerHistoryDao(EmailerHistoryDao emailerHistoryDao) {
        this.emailerHistoryDao = emailerHistoryDao;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
