package com.hk.impl.dao.user;

import org.springframework.stereotype.Repository;

import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.B2bUserDetailsDao;

@Repository
public class B2bUserDetailsDaoImpl extends BaseDaoImpl implements B2bUserDetailsDao {

    public B2bUserDetails getB2bUserDetails(User user) {
        return (B2bUserDetails) getSession().createQuery("from B2bUserDetails u where u.user =:user").setParameter("user", user).uniqueResult();
    }

}
