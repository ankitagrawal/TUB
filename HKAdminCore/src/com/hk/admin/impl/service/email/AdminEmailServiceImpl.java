package com.hk.admin.impl.service.email;

import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.admin.pact.dao.email.AdminEmailDao;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.domain.user.Role;

import java.util.List;
import java.util.Collection;
import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: Jun 13, 2012
 * Time: 4:13:36 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class AdminEmailServiceImpl implements AdminEmailService{
  @Autowired
  private AdminEmailDao adminEmailDao;

  public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult) {
    return getAdminEmailDao().getMailingListByCategory(emailCampaign, category, maxResult);
  }

  public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category) {
    return getAdminEmailDao().getMailingListCountByCategory(emailCampaign, category);
  }
  
  public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, List<Role> roleList, int maxResult) {
    return getAdminEmailDao().getAllMailingList(emailCampaign, roleList, maxResult);
  }

  public List<EmailRecepient> getUserMailingList(EmailCampaign emailCampaign, List<Long> userList, int maxResult) {
    return getAdminEmailDao().getUserMailingList(emailCampaign, userList, maxResult);
  }

  public BigInteger getAllMailingListCount(EmailCampaign emailCampaign, String [] roles) {
    return getAdminEmailDao().getAllMailingListCount(emailCampaign, roles);
  }

  public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult) {
    return getAdminEmailDao().getMailingListByEmailIds(emailCampaign,  emailList, maxResult);
  }

  public List<User> findAllUsersNotInEmailRecepient(int maxResult, List<String> userIdList) {
    return getAdminEmailDao().findAllUsersNotInEmailRecepient(maxResult, userIdList);
  }

  public void saveOrUpdate(Session session, Collection entities) throws DataAccessException {
    getAdminEmailDao().saveOrUpdate(session, entities);
  }

  public AdminEmailDao getAdminEmailDao() {
    return adminEmailDao;
  }

  public void setAdminEmailDao(AdminEmailDao adminEmailDao) {
    this.adminEmailDao = adminEmailDao;
  }
}
