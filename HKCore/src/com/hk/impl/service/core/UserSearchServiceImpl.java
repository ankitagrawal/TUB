package com.hk.impl.service.core;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UsersDao;
import com.hk.pact.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/5/13
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserSearchServiceImpl implements UserSearchService {
    @Autowired
    UsersDao usersDao;

    @Override
    public List<User> searchUsers(UsersSearchCriteria criteria) {
        return usersDao.findBySearchCriteria(criteria);
    }

}
