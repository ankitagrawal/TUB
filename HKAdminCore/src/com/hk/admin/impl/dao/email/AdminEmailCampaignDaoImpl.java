package com.hk.admin.impl.dao.email;

import com.akube.framework.dao.Page;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.email.AdminEmailCampaignDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminEmailCampaignDaoImpl extends BaseDaoImpl implements AdminEmailCampaignDao {
  public Page getEmailCampaignByEmailType(EmailType emailType, int page, int perPage) {
    DetachedCriteria criteria = DetachedCriteria.forClass(EmailCampaign.class);
    criteria.add(Restrictions.eq("emailType", emailType));
    criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
    return list(criteria, page, perPage);
  }

  public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) {
    return (Long) 
        getSession().createQuery("select count(eh.id) from EmailerHistory eh where eh.emailCampaign =:emailCampaign").setParameter("emailCampaign",
        emailCampaign).uniqueResult();
  }

  public List<EmailCampaign> getAmazonS3EmailCampaigns(){
    Criteria criteria = getSession().createCriteria(EmailCampaign.class);
    criteria.add(Restrictions.isNotNull("templateFtl"));
    criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
    return criteria.list();
  }
}
