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

    /**
     * @param product product that was ordered
     * @return Users who have bought any variants of this product
     */
    public List<User> findByProductID(Product product);

    /**
     * @param productVariant product variant that was ordered
     * @return Users who have bought this or any other variants of this product variant's corresponding product
     */
    public List<User> findByProductInvariantID(ProductVariant productVariant);


    public List<User> findBySearchCriteria(UsersSearchCriteria criteria);


}
