package com.hk.admin.impl.dao.email;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.domain.user.Role;
import com.hk.constants.core.EnumRole;
import com.hk.admin.pact.dao.email.AdminEmailDao;
import com.hk.pact.dao.RoleDao;
import com.akube.framework.util.BaseUtils;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.math.BigInteger;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

/**
 * Created by IntelliJ IDEA.
 * User: Rohit
 * Date: Jun 13, 2012
 * Time: 3:52:17 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Repository
public class AdminEmailDaoImpl extends BaseDaoImpl implements AdminEmailDao {

  @Autowired
  private RoleDao roleDao;

  public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, List<Role> roleList, int maxResult) {
    /*String query = "select  er.*" +
        "     from user u left join email_recepient er on (u.email = er.email and er.subscribed > 0) " +
        "     left join emailer_history eh on (er.id = eh.email_recepient_id " +
        "     and eh.email_campaign_id = %s ),  " +
        "     user_has_role ur, email_campaign ec   " +
        "     where u.id = ur.user_id  " +
        "     and ur.role_name IN ('%s')  " +
        "     and ec.id = %s  " +
        "     and (er.last_email_date is null or (datediff(date(sysdate()), er.last_email_date) >= ec.min_day_gap)) Order By u.id  ";
    query = String.format(query, emailCampaign.getId(), BaseUtils.getCommaSeparatedString(roles), emailCampaign.getId());

    //Session session = getSession(true);
    List<EmailRecepient> emailList = getSession().createSQLQuery(query).addEntity(EmailRecepient.class).setMaxResults(maxResult).list();*/
    //session.flush();
    //session.clear();
    //session.close();

   String query = "select er from EmailRecepient er, User u join u.roles r " +
        " where er.subscribed = true and er.email = u.email and coalesce((date(current_date()) - date(er.lastEmailDate)), 0) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign)" +
        " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
        " and r in (:roleList)" ;
    //Session session =  getSessionFactory().openSession();
    List<EmailRecepient> emailRecepients = ( List<EmailRecepient> )getSession().createQuery(query)
        .setParameter("emailCampaign", emailCampaign)
        .setParameter("emailCampaign", emailCampaign)
        .setParameterList("roleList", roleList)
        .setMaxResults(maxResult).list();
   // session.close();
    return emailRecepients;
  }

  public List<EmailRecepient> getUserMailingList(EmailCampaign emailCampaign, List<Long> userList, int maxResult) {
    String query = "select er from EmailRecepient er, User u " +
        " where er.subscribed = true and er.email = u.email and coalesce((date(current_date()) - date(er.lastEmailDate)), 0) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign)" +
        " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
        " and u.id in (:userList)" ;

    List<EmailRecepient> emailRecepients = ( List<EmailRecepient> )getSession().createQuery(query)
        .setParameter("emailCampaign", emailCampaign)
        .setParameter("emailCampaign", emailCampaign)
        .setParameterList("userList", userList)
        .setMaxResults(maxResult).list();

    return emailRecepients;
    /*String query = "select  er.*" +
        "     from user u left join email_recepient er on (u.email = er.email and er.subscribed > 0) " +
        "     left join emailer_history eh on (er.id = eh.email_recepient_id and eh.email_campaign_id = %s )," +
        "     email_campaign ec   " +
        "     where ec.id = %s  " +
        "     and (er.last_email_date is null or (datediff(date(sysdate()), er.last_email_date) >= ec.min_day_gap))  " +
        "     and u.id in ( '%s' ) " +
        "     Order By u.id ";
    query = String.format(query, emailCampaign.getId(), emailCampaign.getId(), BaseUtils.getCommaSeparatedString(userIds));

    Session session = getSession(true);

    List<EmailRecepient> userList = session.createSQLQuery(query).addEntity(EmailRecepient.class).setMaxResults(maxResult).list();
    session.flush();
    session.clear();
    session.close();
    return userList;*/
  }

  public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult) {

    List<EmailRecepient> emailRecepients = getSession().createQuery("select er from EmailRecepient er " +
        " where er.subscribed = true and coalesce((date(current_date()) - date(er.lastEmailDate)), 0) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign)" +
        " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
        " and er.email in (:emailList)")
        .setParameterList("emailList", emailList)
        .setParameter("emailCampaign", emailCampaign)
        .setParameter("emailCampaign", emailCampaign)
        .setMaxResults(maxResult).list();
    
    return emailRecepients;
  }

  public BigInteger getAllMailingListCount(EmailCampaign emailCampaign, String [] roles) {       
    String query = "select  count(u.id)" +
        "     from user u left join email_recepient er on (u.email = er.email and er.subscribed > 0) " +
        "     left join emailer_history eh on (er.id = eh.email_recepient_id " +
        "     and eh.email_campaign_id = %s ),  " +
        "     user_has_role ur, email_campaign ec   " +
        "     where u.id = ur.user_id  " +
        "     and ur.role_name IN ('%s')  " +
        "     and ec.id = %s  " +
        "     and (er.last_email_date is null or (date(sysdate()) - er.last_email_date >= ec.min_day_gap))  ";
    query = String.format(query, emailCampaign.getId(), BaseUtils.getCommaSeparatedString(roles), emailCampaign.getId());

    BigInteger userListCount = (BigInteger) getSession().createSQLQuery(query).uniqueResult();
    return userListCount;
  }

  public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult) {

    String query = "select distinct er from LineItem li left join li.sku.productVariant.product.categories c"
        + " left join li.shippingOrder.baseOrder.user u left join u.roles r, EmailRecepient er " + "where er.email = u.email and c in (:categoryList) " + "and r in (:roleList)"
        + " and er.subscribed = true and coalesce((date(current_date()) - date(er.lastEmailDate)), 0) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign)"
        + " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign )";

    List<EmailRecepient> userIdsByCategory = getSession().createQuery(query)
        .setParameterList("categoryList", Arrays.asList(category))
        .setParameterList("roleList",Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER), getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)))
        .setParameter("emailCampaign", emailCampaign)
        .setParameter("emailCampaign", emailCampaign).setMaxResults(maxResult).list();

    return userIdsByCategory;
  }

  public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category) {

    String query = "select count(distinct u.id) from LineItem li left join li.sku.productVariant.product.categories c"
        + " left join li.shippingOrder.baseOrder.user u left join u.roles r, EmailRecepient er " + "where er.email = u.email and c in (:categoryList) " + "and r in (:roleList)"
        + " and er.subscribed = true and coalesce((date(current_date()) - date(er.lastEmailDate)), 0) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign)"
        + " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign )";

    Long userIdsByCategoryCount = (Long)getSession().createQuery(query)
        .setParameterList("categoryList", Arrays.asList(category))
        .setParameterList("roleList",Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER), getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)))
        .setParameter("emailCampaign", emailCampaign)
        .setParameter("emailCampaign", emailCampaign).uniqueResult();

    return userIdsByCategoryCount;
  }

  public List<User> findAllUsersNotInEmailRecepient(int maxResult, List<String> userIdList) {
    String query = "select u.* from user u left join email_recepient er on (u.email = er.email) where er.email is null and u.email is not null ";

    if(userIdList != null){
      query += "and u.id in (:userIdList)" ;
    }
    Query sqlQuery = getSession().createSQLQuery(query).addEntity(User.class);
    if(userIdList != null) {
      sqlQuery = sqlQuery.setParameterList("userIdList", userIdList);
    }
    
    return sqlQuery.setMaxResults(maxResult).list();
  }

  @SuppressWarnings("unchecked")
  public void saveOrUpdate(Session session, Collection entities) throws DataAccessException {

    Transaction transaction = session.beginTransaction();
    try {
      for (Object object : entities) {
        session.saveOrUpdate(object);
      }
      session.flush();
      session.clear();
      transaction.commit();
    }catch(Exception ex){
      transaction.rollback();
    }finally{


    }
  }


  public RoleDao getRoleDao() {
    return roleDao;
  }

  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
  }
}
