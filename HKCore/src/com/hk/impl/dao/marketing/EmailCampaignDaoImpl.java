package com.hk.impl.dao.marketing;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.marketing.EmailCampaignDao;

@SuppressWarnings("unchecked")
@Repository
public class EmailCampaignDaoImpl extends BaseDaoImpl implements EmailCampaignDao {

  @Transactional
  public EmailCampaign save(EmailCampaign emailCampaign) {
    if (emailCampaign != null) {
      if (emailCampaign.getCreateDate() == null)
        emailCampaign.setCreateDate(BaseUtils.getCurrentTimestamp());
    }
    return (EmailCampaign) super.save(emailCampaign);
  }

  @Transactional
  public EmailCampaign getOrCreateEmailCampaign(String campaignName, Long minDayGap, String emailTemplate) {
    EmailCampaign emailCampaign = null;
    emailCampaign = findCampaignByName(campaignName);
    if (emailCampaign == null) {
      emailCampaign = new EmailCampaign();
      emailCampaign.setName(campaignName);
      emailCampaign.setCreateDate(new Date());
      emailCampaign.setMinDayGap(minDayGap);
      emailCampaign.setTemplate(emailTemplate);
      emailCampaign = save(emailCampaign);
    }
    return emailCampaign;
  }

  @Transactional
  public EmailCampaign findCampaignByName(String campaignName) {
    Criteria criteria = getSession().createCriteria(EmailCampaign.class);
    criteria.add(Restrictions.eq("name", campaignName));
    return (EmailCampaign) criteria.uniqueResult();
  }

  public List<EmailCampaign> listAllExceptNotifyMe() {
    Criteria criteria = getSession().createCriteria(EmailCampaign.class);
    criteria.add(Restrictions.not(Restrictions.eq("template", "/notifyUserEmailProductNew.ftl")));
    criteria.add(Restrictions.not(Restrictions.eq("template", "/notifyUserEmailProduct.ftl")));
    criteria.add(Restrictions.not(Restrictions.eq("template", "/marketing/missYouUserEmailNew.ftl")));
    return criteria.list();
  }

  public List<EmailCampaign> listAllMissYouCampaigns() {
    Criteria criteria = getSession().createCriteria(EmailCampaign.class);
    criteria.add(Restrictions.eq("template", "/missYouUserEmailNew.ftl"));
    return criteria.list();
  }

  public Date getLastDateOfEmailCampaignMailSentToEmailRecepient(EmailCampaign emailCampaign, EmailRecepient emailRecepient) {
    String hqlQuery = "select eh.sendDate from EmailerHistory eh where eh.emailRecepient =:emailRecepient and eh.emailCampaign=:emailCampaign order by eh.sendDate desc";
    return (Date) getSession().createQuery(hqlQuery).setParameter("emailCampaign", emailCampaign).setParameter("emailRecepient", emailRecepient).setMaxResults(1).uniqueResult();
  }
}
