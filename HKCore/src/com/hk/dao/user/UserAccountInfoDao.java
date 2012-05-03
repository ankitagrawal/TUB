package com.hk.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;

@Repository
public class UserAccountInfoDao extends BaseDaoImpl {

    @Autowired
    private UserDaoImpl userDao;

    @Transactional
    public UserAccountInfo getOrCreateUserAccountInfo(User user) {
        UserAccountInfo userAccountInfo = user.getUserAccountInfo();
        if (userAccountInfo == null) {
            userAccountInfo = new UserAccountInfo();
            userAccountInfo.setUser(user);
            userAccountInfo = save(userAccountInfo);
            user.getUserAccountInfos().add(userAccountInfo);
            userDao.save(user);
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

    public UserDaoImpl getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

}
