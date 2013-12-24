package com.hk.impl.dao.user;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.dto.user.UserDTO;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UsersDao;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository
public class UsersDaoImpl extends BaseDaoImpl implements UsersDao {
    @Override
    public List<UserDTO> findUserInfoBySearchCriteria(UsersSearchCriteria criteria){
        DetachedCriteria dc = criteria.getSearchCriteria();
        List<UserDTO> info = findByCriteria(dc);
        return info;
    }
}
