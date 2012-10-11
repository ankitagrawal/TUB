package com.hk.pact.dao.user;

import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetailsDao extends BaseDao {
    public UserDetail save(UserDetail userDetails);

    public UserDetail findByPhone(int phone);
}
