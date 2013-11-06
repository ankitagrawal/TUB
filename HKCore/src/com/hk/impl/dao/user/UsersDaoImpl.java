package com.hk.impl.dao.user;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UsersDao;
import org.hibernate.criterion.DetachedCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersDaoImpl extends BaseDaoImpl implements UsersDao {
    public List<User> findByProductID(String productId) {
        List<User> users = new ArrayList<User>();
        try {
            UsersSearchCriteria criteria = new UsersSearchCriteria();
            criteria.setProductId(productId);
            DetachedCriteria dc = criteria.getSearchCriteria();

            users = findByCriteria(dc);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> findByProductID(Product product) {
        return findByProductID(product.getId());
    }

    @Override
    public List<User> findByProductInvariantID(ProductVariant productVariant) {
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        DetachedCriteria dc = criteria.getSearchCriteria();
        List<User> users = getHibernateTemplate().findByCriteria(dc);
        List<User> users2 =findByCriteria(dc);

        return users;
    }

    @Override
    public List<User> findBySearchCriteria(UsersSearchCriteria criteria) {
        DetachedCriteria dc = criteria.getSearchCriteria();
        List<User> users = getHibernateTemplate().findByCriteria(dc);
        List<User> users2 =findByCriteria(dc);
        return users;
    }
}
