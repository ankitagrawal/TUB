package com.hk.pact.service.user;

import com.hk.domain.user.UserDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetailService {

    final int MAX_COUNT = 3;
    final int MIN_KARMA_POINTS = 300;
    public UserDetail save(UserDetail userDetails);

    public List<UserDetail> findByPhone(long phone);

    public List<UserDetail> getByPriority(int priority);
}
