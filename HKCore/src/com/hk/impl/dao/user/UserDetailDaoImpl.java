package com.hk.impl.dao.user;

import com.hk.constants.core.EnumCallPriority;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserDetailDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
    public UserDetail save(UserDetail userDetails) {
        if(userDetails.getPriority() == null){
            userDetails.setPriority(EnumCallPriority.PRIORITY_ONE.getValue());
        }
       return (UserDetail)super.save(userDetails);
    }

    @SuppressWarnings("unchecked")
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

    public UserDetail findByUserAndPhone(Long phone, User user){
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("phone", phone));
        criteria.add(Restrictions.eq("user", user));
        List<UserDetail> users = super.findByCriteria(criteria);
        if ((users == null) || users.isEmpty())
            return null;
        return users.get(0);
    }

    public List<UserDetail> findByPriority(int priority) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserDetail.class);
        criteria.add(Restrictions.eq("priority", priority));
        return (List<UserDetail>)super.findByCriteria(criteria);
    }

    public void delete(UserDetail userDetail) {
        super.delete(userDetail);
    }
}
