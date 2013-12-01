package com.hk.impl.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserAccountInfoDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;

@Repository
public class UserAccountInfoDaoImpl extends BaseDaoImpl implements UserAccountInfoDao {

    @Autowired
    private UserDao     userDao;
    @Autowired
    private UserService userService;

    @Transactional
    public UserAccountInfo getOrCreateUserAccountInfo(User user) {
        UserAccountInfo userAccountInfo = user.getUserAccountInfo();
        if (userAccountInfo == null) {
            userAccountInfo = new UserAccountInfo();
            userAccountInfo.setUser(user);
            userAccountInfo = save(userAccountInfo);
            user.getUserAccountInfos().add(userAccountInfo);
            userService.save(user);
        }
        return userAccountInfo;
    }

    @Transactional
    public UserAccountInfo save(UserAccountInfo userAccountInfo) {
        if (userAccountInfo != null) {
            if (userAccountInfo.getOverusedRewardPoints() == null)
                userAccountInfo.setOverusedRewardPoints(0D);
        }
        return (UserAccountInfo) super.save(userAccountInfo);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
