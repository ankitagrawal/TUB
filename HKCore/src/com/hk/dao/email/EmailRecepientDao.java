package com.hk.dao.email;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.dao.user.UserDao;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.User;
import com.hk.util.TokenUtils;


@Repository
public class EmailRecepientDao extends BaseDaoImpl {

  @Autowired
  private UserDao userDao;


  public EmailRecepient getOrCreateEmailRecepient(String recepientEmail) {
    EmailRecepient emailRecepient = findByRecepient(recepientEmail);
    if(emailRecepient == null) {
      emailRecepient = new EmailRecepient();
      emailRecepient.setEmail(recepientEmail != null ? recepientEmail : "test@healthkart.com");
      emailRecepient.setSubscribed(true);
      emailRecepient.setBounce(false);
      emailRecepient.setInvalid(false);
      List<User> users = getUserDao().findByEmail(recepientEmail);
      emailRecepient.setUser(users != null && users.size() > 0 ? users.get(0) : null);
      emailRecepient.setEmailCount(0L);
      emailRecepient.setName(recepientEmail);
      emailRecepient.setUnsubscribeToken(TokenUtils.getTokenToUnsubscribeWommEmail(recepientEmail));
      emailRecepient = (EmailRecepient) save(emailRecepient);
    }
    return emailRecepient;
  }

  public EmailRecepient findByRecepient(String recepientEmail) {
    Criteria criteria = getSession().createCriteria(EmailRecepient.class);
    criteria.add(Restrictions.eq("email", recepientEmail));
    return (EmailRecepient) criteria.uniqueResult();
  }

  public EmailRecepient findByUnsubscribeToken(String unsubscribeToken) {
    return (EmailRecepient) getSession().createQuery("from EmailRecepient e where e.unsubscribeToken = :unsubscribeToken")
        .setString("unsubscribeToken", unsubscribeToken)
        .uniqueResult();
  }

public UserDao getUserDao() {
    return userDao;
}

public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
}
  
  
}
