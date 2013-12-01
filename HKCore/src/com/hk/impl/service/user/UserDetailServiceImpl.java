package com.hk.impl.service.user;

import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.user.UserDetailDao;
import com.hk.pact.service.user.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    UserDetailDao userDetailsDao;

    public UserDetail save(UserDetail userDetails) {
        return userDetailsDao.save(userDetails);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete(UserDetail userDetail) {
        userDetailsDao.delete(userDetail);
    }

    public List<UserDetail> findByPhone(long phone) {
        return userDetailsDao.findByPhone(phone);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UserDetail findByPhoneAndUser(long phone, User user) {
        return userDetailsDao.findByUserAndPhone(phone, user);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UserDetail> getByPriority(int priority) {
         return userDetailsDao.findByPriority(priority);
    }
}
