package com.hk.impl.dao.user;

import org.springframework.stereotype.Repository;

import com.hk.domain.user.B2bUser;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.B2bUserDetailsDao;

@Repository
public class B2bUserDetailsDaoImpl extends BaseDaoImpl implements B2bUserDetailsDao {

    public B2bUser getB2bUserDetails(User user) {
        return (B2bUser) getSession().createQuery("from B2bUser u where u.user =:user").setParameter("user", user).uniqueResult();
    }

}
