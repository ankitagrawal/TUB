package com.hk.pact.dao.user;

import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;
import com.hk.pact.dao.BaseDao;

public interface UserAccountInfoDao extends BaseDao {

    @Transactional
    public UserAccountInfo getOrCreateUserAccountInfo(User user);

    @Transactional
    public UserAccountInfo save(UserAccountInfo userAccountInfo);

}
