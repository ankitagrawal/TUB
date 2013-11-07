package com.hk.pact.service;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/5/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserSearchService {
    List<User> searchUsers(UsersSearchCriteria criteria);
}
