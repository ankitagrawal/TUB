package com.hk.impl.service.user;

import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.user.UserDetailsDao;
import com.hk.pact.service.user.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    UserDetailsDao userDetailsDao;

    public UserDetail save(UserDetail userDetails) {
        return userDetailsDao.save(userDetails);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserDetail findByPhone(int phone) {
        return userDetailsDao.findByPhone(phone);  //To change body of implemented methods use File | Settings | File Templates.
    }

}
