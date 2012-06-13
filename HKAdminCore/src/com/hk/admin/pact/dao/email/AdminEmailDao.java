package com.hk.admin.pact.dao.email;

import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;

import java.util.List;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: Jun 13, 2012
 * Time: 4:09:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AdminEmailDao {
  public List<EmailRecepient> getUserMailingList(EmailCampaign emailCampaign, String[] userIds, int maxResult);

  public BigInteger getAllMailingListCount(EmailCampaign emailCampaign, String [] roles);

  public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult);

  public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category);

  public List<User> findAllUsersNotInEmailRecepient(int maxResult, List<String> userIdList);

  public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult);

  public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, String [] roles, int maxResult);
}
