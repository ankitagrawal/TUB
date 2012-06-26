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

        String query = "select er from EmailRecepient er, User u join u.roles r " +
          " where er.subscribed = true and er.email = u.email " +
          " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )" +
          " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
          " and r in (:roleList) and u.store.id in (:storeIdList)" ;

        List<EmailRecepient> emailRecepients = ( List<EmailRecepient> )getSession().createQuery(query)
          .setParameter("emailCampaign", emailCampaign)
          .setParameter("emailCampaign", emailCampaign)
          .setParameterList("roleList", roleList)
          .setParameterList("storeIdList", Arrays.asList(1L, null))
          .setMaxResults(maxResult).list();

        return emailRecepients;
    }

    public List<EmailRecepient> getUserMailingList(EmailCampaign emailCampaign, List<Long> userList, int maxResult) {
        String query = "select er from EmailRecepient er, User u " +
          " where er.subscribed = true and er.email = u.email " +
          " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )" +
          " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
          " and u.id in (:userList)" ;

        List<EmailRecepient> emailRecepients = ( List<EmailRecepient> )getSession().createQuery(query)
          .setParameter("emailCampaign", emailCampaign)
          .setParameter("emailCampaign", emailCampaign)
          .setParameterList("userList", userList)
          .setMaxResults(maxResult).list();

        return emailRecepients;
    }

    public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult) {

        List<EmailRecepient> emailRecepients = getSession().createQuery("select er from EmailRecepient er " +
          " where er.subscribed = true " +
          " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )" +
          " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign ) " +
          " and er.email in (:emailList)")
          .setParameterList("emailList", emailList)
          .setParameter("emailCampaign", emailCampaign)
          .setParameter("emailCampaign", emailCampaign)
          .setMaxResults(maxResult).list();

        return emailRecepients;
    }

    public Long getAllMailingListCount(EmailCampaign emailCampaign, List<Role> roleList) {
        String query = "select count(*) from EmailRecepient er, User u join u.roles r " +
          " where er.subscribed = true and er.email = u.email " +
          " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )" +
          " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaignInner ) " +
          " and r in (:roleList)" ;

        Long userListCount = (Long) findUniqueByNamedParams(query, new String[]{"emailCampaign", "emailCampaignInner", "roleList"}, new Object[]{emailCampaign, emailCampaign, roleList});

        return userListCount;
    }

    public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult) {
        String query = "select distinct er from OrderCategory oc join oc.order.user u join u.roles r, "
          + " EmailRecepient er " + "where er.email = u.email and oc.category in (:categoryList) " + "and r in (:roleList)"
          + " and er.subscribed = true "
          + " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )"
          + " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign )";

        List<EmailRecepient> userIdsByCategory = getSession().createQuery(query)
          .setParameterList("categoryList", Arrays.asList(category))
          .setParameterList("roleList",Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER), getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)))
          .setParameter("emailCampaign", emailCampaign)
          .setParameter("emailCampaign", emailCampaign).setMaxResults(maxResult).list();

        return userIdsByCategory;
    }

    public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category) {

        String query = "select count(distinct u.id) from OrderCategory oc join oc.order.user u join u.roles r, "
                  + " EmailRecepient er " + "where er.email = u.email and oc.category in (:categoryList) " + "and r in (:roleList)"
                  + " and er.subscribed = true "
                  + " and (er.lastEmailDate is null or (er.lastEmailDate is not null and (date(current_date()) - date(er.lastEmailDate) >= (select ec.minDayGap from EmailCampaign ec where ec = :emailCampaign))) )"
                  + " and er not in (select eh.emailRecepient from EmailerHistory eh where eh.emailCampaign = :emailCampaign )";

        Long userIdsByCategoryCount = (Long)getSession().createQuery(query)
          .setParameterList("categoryList", Arrays.asList(category))
          .setParameterList("roleList",Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER), getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED)))
          .setParameter("emailCampaign", emailCampaign)
          .setParameter("emailCampaign", emailCampaign).uniqueResult();
                
        return userIdsByCategoryCount;
    }

    public List<User> findAllUsersNotInEmailRecepient(int maxResult, List<String> userIdList) {
//        String query = "select u.* from user u left join email_recepient er on (u.email = er.email) where er.email is null and u.email is not null ";
        String query = "select distinct(u) from User u where u.email not in (select er.email from EmailRecepient er) group by u.email";
        return getSession().createQuery(query).setMaxResults(maxResult).list();

//        if(userIdList != null){
//            query += "and u.id in (:userIdList)" ;
//        }
//        Query sqlQuery = getSession().createSQLQuery(query).addEntity(User.class);
//        Query sqlQuery = getSession().createSQLQuery(query).addEntity(User.class);
//        if(userIdList != null) {
//            sqlQuery = sqlQuery.setParameterList("userIdList", userIdList);
//        }
//        return sqlQuery.setMaxResults(maxResult).list();
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
        }
    }


    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
