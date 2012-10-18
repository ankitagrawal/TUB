package com.hk.impl.dao.user;

import com.hk.constants.core.EnumCallPriority;
import com.hk.domain.user.UserDetail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserDetailDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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

    public UserDetail findByPhone(Long phone) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("phone", phone));
        List<UserDetail> userDetailList = super.findByCriteria(criteria);
        if (userDetailList != null && !userDetailList.isEmpty()){
            return userDetailList.get(0);
        }
        return null;
    }

    public List<UserDetail> findByPriority(int priority) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("priority", priority));
        return (List<UserDetail>)super.findByCriteria(criteria);
    }
}
