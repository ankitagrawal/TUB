package com.hk.impl.dao.user;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UsersDao;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public List<User> findUserBySearchCriteria(UsersSearchCriteria criteria) {
        DetachedCriteria dc = criteria.getSearchCriteria(false);
        //List<User> users = getHibernateTemplate().findByCriteria(dc);
        List<User> users = findByCriteria(dc);
        return users;
    }


    @Override
    public List<Object[]> findUserInfoBySearchCriteria(UsersSearchCriteria criteria){
        DetachedCriteria dc = criteria.getSearchCriteria(true);
        //List<User> users = getHibernateTemplate().findByCriteria(dc);
        List<Object[]> info = findByCriteria(dc);
        return info;
    }

}
