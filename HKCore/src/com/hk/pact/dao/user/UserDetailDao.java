package com.hk.pact.dao.user;

import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/18/12
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetailDao {

    UserDetail save(UserDetail userDetails);
    List<UserDetail> findByPhone(Long phone);
    List<UserDetail> findByPriority(int priority);
    void delete(UserDetail userDetail);
    UserDetail findByUserAndPhone(Long phone, User user);
}
