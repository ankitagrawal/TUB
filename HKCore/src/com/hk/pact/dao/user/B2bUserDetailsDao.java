package com.hk.pact.dao.user;

import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface B2bUserDetailsDao extends BaseDao {

    public B2bUserDetails getB2bUserDetails(User user) ;

}
