package com.hk.pact.dao.user;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UsersDao extends BaseDao {
    public List<User> findUserBySearchCriteria(UsersSearchCriteria criteria);
    public List<Object[]> findUserInfoBySearchCriteria(UsersSearchCriteria criteria);
}
