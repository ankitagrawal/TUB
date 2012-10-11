package com.hk.impl.dao.user;

import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserDetailsDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDetailDaoImpl  extends BaseDaoImpl implements UserDetailsDao{

    public UserDetail save(UserDetail userDetails) {
       return (UserDetail)super.save(userDetails);
    }

    public UserDetail findByPhone(int phone) {
       DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
       criteria.add(Restrictions.eq("phone", phone));
       return (UserDetail)super.findByCriteria(criteria).get(0);
    }
}
