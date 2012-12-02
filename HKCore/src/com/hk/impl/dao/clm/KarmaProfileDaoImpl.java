package com.hk.impl.dao.clm;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.clm.KarmaProfileDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 4:00:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
@SuppressWarnings("unchecked")
public class KarmaProfileDaoImpl extends BaseDaoImpl implements KarmaProfileDao {

  @Transactional
  public KarmaProfile save(KarmaProfile karmaProfile) {
    return (KarmaProfile) super.save(karmaProfile);
  }

   public KarmaProfile findByUser(User user) {
       
       return (KarmaProfile) findUniqueByNamedParams(" from KarmaProfile kp where kp.user.id = :userId ", new String[]{"userId"}, new Object[]{user.getId()});
     //return  (KarmaProfile) getSession().createQuery("from KarmaProfile kp where kp.user = :user ").setEntity("user", user).uniqueResult();
  }

}
