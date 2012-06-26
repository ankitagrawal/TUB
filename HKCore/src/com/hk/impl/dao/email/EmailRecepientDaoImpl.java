package com.hk.impl.dao.email;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.util.TokenUtils;

@Repository
public class EmailRecepientDaoImpl extends BaseDaoImpl implements EmailRecepientDao {

    @Autowired
    private UserDao userDao;

    public EmailRecepient getOrCreateEmailRecepient(String recepientEmail) {
        EmailRecepient emailRecepient = findByRecepient(recepientEmail);
        if (emailRecepient == null) {
            return createEmailRecepient(recepientEmail);
        }
        return emailRecepient;
    }

    public EmailRecepient createEmailRecepient(String recepientEmail) {

        EmailRecepient emailRecepient = new EmailRecepient();
        emailRecepient.setEmail(recepientEmail != null ? recepientEmail : "test@healthkart.com");
        emailRecepient.setSubscribed(true);
        emailRecepient.setBounce(false);
        emailRecepient.setInvalid(false);
        List<User> users = getUserDao().findByEmail(recepientEmail);
        emailRecepient.setUser(users != null && users.size() > 0 ? users.get(0) : null);
        emailRecepient.setEmailCount(0L);
        emailRecepient.setName(users != null && users.size() > 0 ? users.get(0).getName() : recepientEmail);
        emailRecepient.setUnsubscribeToken(TokenUtils.getTokenToUnsubscribeWommEmail(recepientEmail));
        emailRecepient = (EmailRecepient) save(emailRecepient);

        return emailRecepient;
    }

    public EmailRecepient createEmailRecepientObject(String recepientEmail) {

        EmailRecepient emailRecepient = new EmailRecepient();
        emailRecepient.setEmail(recepientEmail != null ? recepientEmail : "test@healthkart.com");
        emailRecepient.setSubscribed(true);
        emailRecepient.setBounce(false);
        emailRecepient.setInvalid(false);
        List<User> users = getUserDao().findByEmail(recepientEmail);
        emailRecepient.setUser(users != null && users.size() > 0 ? users.get(0) : null);
        emailRecepient.setEmailCount(0L);
        emailRecepient.setName(users != null && users.size() > 0 ? users.get(0).getName() : recepientEmail);
        emailRecepient.setUnsubscribeToken(TokenUtils.getTokenToUnsubscribeWommEmail(recepientEmail));

        return emailRecepient;
    }

    public EmailRecepient findByRecepient(String recepientEmail) {
        Criteria criteria = getSession().createCriteria(EmailRecepient.class);
        criteria.add(Restrictions.eq("email", recepientEmail));
        return (EmailRecepient) criteria.uniqueResult();
    }

    public List<String> findEmailIdsPresentInEmailRecepient(List<String> mailingList) {

        String query = "select er.email from EmailRecepient er where er.email in (:mailingList)";
        List<String> emailIdsPresentInEmailReccepient = getSession().createQuery(query).setParameterList("mailingList", mailingList).list();
        return emailIdsPresentInEmailReccepient;
    }

    public EmailRecepient findByUnsubscribeToken(String unsubscribeToken) {
        return (EmailRecepient) getSession().createQuery("from EmailRecepient e where e.unsubscribeToken = :unsubscribeToken").setString("unsubscribeToken", unsubscribeToken).uniqueResult();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
