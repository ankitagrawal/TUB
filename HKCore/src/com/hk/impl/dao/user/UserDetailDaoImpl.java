package com.hk.impl.dao.user;

import com.hk.constants.core.EnumCallPriority;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserDetailDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDetailDaoImpl  extends BaseDaoImpl implements UserDetailDao {

    public UserDetail save(UserDetail userDetails) {
        if(userDetails.getPriority() == null){
            userDetails.setPriority(EnumCallPriority.PRIORITY_ONE.getValue());
        }
       return (UserDetail)super.save(userDetails);
    }

    public List<UserDetail>  findByPhone(Long phone) {
        List<UserDetail> userDetailList = new ArrayList<UserDetail>();
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("phone", phone));
        List<UserDetail> users = super.findByCriteria(criteria);
        if (users != null){
            userDetailList = users;
        }
        return userDetailList;
    }

    public List<UserDetail> findByPriority(int priority) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("priority", priority));
        return (List<UserDetail>)super.findByCriteria(criteria);
    }
}
