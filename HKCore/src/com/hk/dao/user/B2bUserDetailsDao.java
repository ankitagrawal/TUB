package com.hk.dao.user;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;

@Repository
public class B2bUserDetailsDao extends BaseDaoImpl {

    public B2bUserDetails getB2bUserDetails(User user) {
        return (B2bUserDetails) getSession().createQuery("from B2bUserDetails u where u.user =:user").setParameter("user", user).uniqueResult();
    }

}
