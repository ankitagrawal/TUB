package com.hk.admin.pact.dao.email;

import com.akube.framework.dao.Page;
import com.hk.domain.core.EmailType;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.BaseDao;

public interface AdminEmailCampaignDao extends BaseDao {
  /**
   * returns email campaigns belonging to a particular email type. the result is reverse sorted on basis of create date
   *
   * @param emailType
   * @param page
   * @param perPage
   * @return
   */
    public Page getEmailCampaignByEmailType(EmailType emailType, int page, int perPage) ;


  /**
   * returns sent count of a particular email campaign based on the count of entries in the emailer history
   *
   * @param emailCampaign
   * @return
   */
    public Long getEmailCampaignSentCount(EmailCampaign emailCampaign) ;
}
